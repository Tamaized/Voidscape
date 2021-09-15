package tamaized.voidscape.turmoil;

import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.registry.ModAttributes;
import tamaized.voidscape.registry.ModDamageSource;
import tamaized.voidscape.turmoil.abilities.TurmoilAbility;
import tamaized.voidscape.turmoil.abilities.TurmoilAbilityInstance;
import tamaized.voidscape.turmoil.skills.TurmoilSkill;
import tamaized.voidscape.turmoil.skills.TurmoilSkills;
import tamaized.voidscape.world.InstanceChunkGenerator;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class TurmoilStats implements SubCapability.ISubCap.ISubCapData.All {

	public static final ResourceLocation ID = new ResourceLocation(Voidscape.MODID, "turmoilstats");

	private TurmoilAbilityInstance[] slots = new TurmoilAbilityInstance[9];

	private int voidicPower = 0;
	private int nullPower = 0;
	private int insanePower = 0;

	private TurmoilSkill.Stats stats = TurmoilSkill.Stats.empty();

	private Map<TurmoilAbility.Toggle, TurmoilAbility> toggles = new HashMap<>();

	private boolean voidmancerStance = false;
	private float ramDamage = 0;
	private Vec3 ramTarget;
	private int ramTimeout;

	private boolean dirty = false;

	private static void applyAttribute(LivingEntity living, Attribute attribute, UUID modifier, String name, double value, AttributeModifier.Operation op) {
		AttributeInstance a = living.getAttribute(attribute);
		if (a != null) {
			a.removeModifier(modifier);
			a.addTransientModifier(new AttributeModifier(modifier, name, value, op));
		}
	}

	public boolean toggleAbility(TurmoilAbility ability) {
		TurmoilAbility o = toggles.get(ability.toggle());
		if (o == null) {
			toggles.put(ability.toggle(), ability);
			markDirty();
			return true;
		}
		if (ability == o) {
			toggles.remove(ability.toggle());
			markDirty();
			return true;
		}
		return false;
	}

	private void resetStats() {
		stats = TurmoilSkill.Stats.empty();
	}

	private void recalculateStatsAndApply(Entity parent) {
		resetStats();
		parent.getCapability(SubCapability.CAPABILITY).ifPresent(cap -> cap.get(Voidscape.subCapTurmoilData).
				ifPresent(data -> data.getSkills().forEach(skill -> stats = stats.add(skill.getStats()))));
		if (parent instanceof LivingEntity living) {
			applyAttribute(living, Attributes.MAX_HEALTH, TurmoilSkill.Stats.HEALTH, "Voidic Health Boost", stats.heart * 2, AttributeModifier.Operation.ADDITION);
			applyAttribute(living, ModAttributes.VOIDIC_DMG.get(), TurmoilSkill.Stats.VOIDIC_DAMAGE, "Voidic Damage", stats.voidicDamage, AttributeModifier.Operation.ADDITION);
			applyAttribute(living, ModAttributes.VOIDIC_DMG.get(), TurmoilSkill.Stats.VOIDIC_DAMAGE_PERC, "Voidic Damage Percentage", stats.voidicDamagePercent, AttributeModifier.Operation.MULTIPLY_TOTAL);
			applyAttribute(living, ModAttributes.VOIDIC_RES.get(), TurmoilSkill.Stats.VOIDIC_RESISTANCE, "Voidic Resistance", stats.voidicDamageReduction, AttributeModifier.Operation.ADDITION);
			applyAttribute(living, ModAttributes.VOIDIC_RES.get(), TurmoilSkill.Stats.VOIDIC_RESISTANCE_PERC, "Voidic Resistance Percentage", stats.voidicDamageReductionPercentage, AttributeModifier.Operation.MULTIPLY_TOTAL);
		}
		markDirty();
	}

	public void reset() {
		voidicPower = 0;
		nullPower = 0;
		insanePower = 0;
		resetStats();
		voidmancerStance = false;
		ramDamage = 0;
		ramTarget = null;
		slots = new TurmoilAbilityInstance[9];
		dirty = true;
	}

	@Override
	public void tick(Entity parent) {
		if (parent.tickCount % 20 * 10 == 0)
			recalculateStatsAndApply(parent);
		if (!parent.level.isClientSide() && dirty && parent instanceof ServerPlayer) {
			sendToClient((ServerPlayer) parent);
			dirty = false;
		}
		Optional<Turmoil> data = parent.getCapability(SubCapability.CAPABILITY).map(cap -> cap.get(Voidscape.subCapTurmoilData)).orElse(Optional.empty());
		if (voidicPower < 1000)
			voidicPower += 1 + stats.rechargeRate;
		if ((!data.isPresent() || (!data.get().hasSkill(TurmoilSkills.HEALER_SKILLS.VOIDS_FAVOR_1) && !data.get().hasSkill(TurmoilSkills.TANK_SKILLS.TACTICIAN_1))) &&

				nullPower > 0 && parent.tickCount % ((voidmancerStance ? 2 : 1) * (1 + stats.rechargeRate)) == 0)
			nullPower--;
		if ((!data.isPresent() || (!data.get().hasSkill(TurmoilSkills.HEALER_SKILLS.MAD_PRIEST_1) && !data.get().hasSkill(TurmoilSkills.TANK_SKILLS.INSANE_BEAST_1))) &&

				insanePower > 0 && parent.tickCount % 10 == 0)
			insanePower--;
		if (parent.getCapability(SubCapability.CAPABILITY).map(cap -> cap.get(Voidscape.subCapTurmoilTracked).map(tracked -> tracked.incapacitated).orElse(false)).orElse(false))
			voidicPower = nullPower = insanePower = 0;
		if (parent.level instanceof ServerLevel && ramTarget != null && ramTimeout-- > 0) {
			Vec3 dist = ramTarget.subtract(parent.position());
			if ((dist.x * dist.x + dist.y * dist.y + dist.z * dist.z) / 2D <= 1D) {
				final boolean flag = ((ServerLevel) parent.level).getChunkSource().getGenerator() instanceof InstanceChunkGenerator;
				parent.level.getEntities(parent, parent.getBoundingBox().inflate(1F, 1F, 1F), e -> !flag || !(e instanceof Player)).forEach(e -> {
					e.hurt(parent instanceof LivingEntity ? ModDamageSource.VOIDIC_WITH_ENTITY.apply((LivingEntity) parent) : ModDamageSource.VOIDIC, ramDamage);
					for (int i = 0; i < 10; i++) {
						Vec3 pos = new Vec3(0.25F + parent.level.getRandom().nextFloat() * 0.75F, 0, 0).
								yRot((float) Math.toRadians(parent.level.getRandom().nextInt(360))).
								xRot((float) Math.toRadians(parent.level.getRandom().nextInt(360))).
								add(e.getX(), e.getEyeY() - 0.5F + parent.level.getRandom().nextFloat(), e.getZ());
						((ServerLevel) parent.level).sendParticles(ParticleTypes.CRIT, pos.x, pos.y, pos.z, 0, 0, 0, 0, 1);
					}
				});
				parent.level.playSound(null, parent, SoundEvents.BLAZE_HURT, SoundSource.PLAYERS, 1F, 0.75F + parent.level.getRandom().nextFloat() * 0.5F);
				ramTarget = null;
				ramDamage = 0;
				ramTimeout = 0;
			} else if (!parent.getDeltaMovement().equals(dist.normalize())) {
				parent.setDeltaMovement(dist.normalize());
				if (parent instanceof ServerPlayer)
					((ServerPlayer) parent).connection.send(new ClientboundSetEntityMotionPacket(parent));
			}
		}
	}

	@Override
	public ResourceLocation id() {
		return ID;
	}

	@Override
	public CompoundTag write(CompoundTag nbt, @Nullable Direction side) {
		nbt.putIntArray("slots", Arrays.stream(slots).mapToInt(slot -> slot == null ? -1 : slot.ability().id()).toArray());
		return nbt;
	}

	@Override
	public void read(CompoundTag nbt, @Nullable Direction side) {
		int[] s = nbt.getIntArray("slots");
		Map<TurmoilAbility, TurmoilAbilityInstance> cache = new HashMap<>();
		for (int i = 0; i < s.length; i++) {
			TurmoilAbility ability = TurmoilAbility.getFromID(s[i]);
			if (ability == null)
				slots[i] = null;
			else {
				if (!cache.containsKey(ability))
					cache.put(ability, new TurmoilAbilityInstance(ability));
				slots[i] = cache.get(ability);
			}
		}
		dirty = true;
	}

	@Override
	public void write(FriendlyByteBuf buffer) {
		buffer.writeInt(voidicPower);
		buffer.writeInt(nullPower);
		buffer.writeInt(insanePower);
		stats.encode(buffer);
		for (TurmoilAbility.Toggle toggle : TurmoilAbility.Toggle.VALUES) {
			TurmoilAbility ability = toggles.get(toggle);
			for (int i = 0; i < 9; i++) {
				TurmoilAbilityInstance inst = getAbility(i);
				if (inst != null && ability == inst.ability())
					break;
				if (i == 8) {
					ability = null;
					toggles.remove(toggle);
				}
			}
			buffer.writeInt(ability == null ? -1 : ability.id());
		}
		for (TurmoilAbilityInstance slot : slots) {
			if (slot == null) {
				buffer.writeInt(-1);
				buffer.writeLong(0L);
			} else
				slot.encode(buffer);
		}
	}

	@Override
	public void read(FriendlyByteBuf buffer) {
		voidicPower = buffer.readInt();
		nullPower = buffer.readInt();
		insanePower = buffer.readInt();
		stats = TurmoilSkill.Stats.decode(buffer);
		toggles.clear();
		for (TurmoilAbility.Toggle toggle : TurmoilAbility.Toggle.VALUES) {
			TurmoilAbility ability = TurmoilAbility.getFromID(buffer.readInt());
			if (ability != null)
				toggles.put(toggle, ability);
		}
		Map<TurmoilAbility, TurmoilAbilityInstance> cache = new HashMap<>();
		for (int i = 0; i < 9; i++) {
			TurmoilAbilityInstance instance = TurmoilAbilityInstance.decode(buffer);
			if (instance != null) {
				if (cache.containsKey(instance.ability()))
					instance = cache.get(instance.ability());
				else
					cache.put(instance.ability(), instance);
			}
			slots[i] = instance;
		}
	}

	public boolean isActive(TurmoilAbility ability) {
		for (TurmoilAbility a : toggles.values())
			if (ability == a)
				return true;
		return false;
	}

	public boolean isActive(TurmoilAbility.Toggle toggle) {
		return toggles.get(toggle) != null;
	}

	public int getVoidicPower() {
		return voidicPower;
	}

	public void setVoidicPower(int a) {
		voidicPower = a;
		dirty = true;
	}

	public int getNullPower() {
		return nullPower;
	}

	public void setNullPower(int a) {
		nullPower = a;
		dirty = true;
	}

	public int getInsanePower() {
		return insanePower;
	}

	public void setInsanePower(int a) {
		insanePower = a;
		dirty = true;
	}

	public TurmoilSkill.Stats stats() {
		return stats;
	}

	public void setSlot(@Nullable TurmoilAbilityInstance ability, int slot) {
		if (slot < 0 || slot >= 9)
			return;
		slots[slot] = ability;
		dirty = true;
	}

	@Nullable
	public TurmoilAbilityInstance getAbility(int slot) {
		if (slot < 0 || slot >= 9)
			return null;
		return slots[slot];
	}

	public void executeAbility(LivingEntity caster, int slot) {
		if (!caster.canUpdate())
			return;
		TurmoilAbilityInstance a = getAbility(slot);
		if (a != null) {
			if (caster.level.isClientSide()) {
				a.executeClientSide(this, caster, slot);
				return;
			}
			a.execute(caster);
		}
	}

	public void resetCooldowns() {
		for (TurmoilAbilityInstance slot : slots) {
			if (slot != null)
				slot.resetCooldown();
		}
	}

	public void ramTowards(Vec3 target, float damage) {
		ramTarget = target;
		ramDamage = damage;
		ramTimeout = 20 * 3;
	}

	public void markDirty() {
		dirty = true;
	}

	@Override
	public void clone(SubCapability.ISubCap.ISubCapData old, boolean death) {
		if (old instanceof TurmoilStats) {
			TurmoilStats o = (TurmoilStats) old;
			System.arraycopy(o.slots, 0, slots, 0, slots.length);
			if (!death) {
				voidicPower = o.voidicPower;
				nullPower = o.nullPower;
				insanePower = o.insanePower;
			}
			stats = o.stats;
		}
	}

	public void increasePowerFromAbilityDamage(LivingEntity caster, LivingEntity target, TurmoilAbility ability) {
		caster.getCapability(SubCapability.CAPABILITY).ifPresent(cap -> cap.get(Voidscape.subCapTurmoilData).ifPresent(data -> {
			if (ability.costType() == TurmoilAbility.Type.Voidic && (data.hasSkill(TurmoilSkills.MAGE_SKILLS.INSANE_MAGE_1)))
				insanePower = Math.min(1000, insanePower + ability.cost());
		}));
	}
}
