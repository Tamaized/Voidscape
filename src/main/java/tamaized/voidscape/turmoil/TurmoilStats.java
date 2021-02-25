package tamaized.voidscape.turmoil;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SEntityVelocityPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.turmoil.abilities.TurmoilAbility;
import tamaized.voidscape.turmoil.abilities.TurmoilAbilityInstance;
import tamaized.voidscape.turmoil.skills.TurmoilSkill;
import tamaized.voidscape.world.InstanceChunkGenerator;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TurmoilStats implements SubCapability.ISubCap.ISubCapData.All {

	public static final ResourceLocation ID = new ResourceLocation(Voidscape.MODID, "turmoilstats");

	private TurmoilAbilityInstance[] slots = new TurmoilAbilityInstance[9];

	private int voidicPower = 0;
	private int nullPower = 0;
	private int insanePower = 0;

	private TurmoilSkill.Stats stats = TurmoilSkill.Stats.empty();

	private boolean voidmancerStance = false;
	private float ramDamage = 0;
	private Vector3d ramTarget;
	private int ramTimeout;

	private boolean dirty = false;

	private void resetStats() {
		stats = TurmoilSkill.Stats.empty();
	}

	private void recalculateStatsAndApply(Entity parent) {
		resetStats();
		parent.getCapability(SubCapability.CAPABILITY).ifPresent(cap -> cap.get(Voidscape.subCapTurmoilData).
				ifPresent(data -> data.getSkills().forEach(skill -> stats = stats.add(skill.getStats()))));
		if (parent instanceof LivingEntity) {
			LivingEntity living = (LivingEntity) parent;
			ModifiableAttributeInstance attribute = living.getAttribute(Attributes.MAX_HEALTH);
			if (attribute != null) {
				attribute.removeModifier(TurmoilSkill.Stats.HEALTH);
				attribute.addTransientModifier(new AttributeModifier(TurmoilSkill.Stats.HEALTH, "Voidic Health Boost", stats.heart * 2, AttributeModifier.Operation.ADDITION));
			}
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
		if (!parent.level.isClientSide() && dirty && parent instanceof ServerPlayerEntity) {
			sendToClient((ServerPlayerEntity) parent);
			dirty = false;
		}
		if (voidicPower < 1000)
			voidicPower += 1 + stats.rechargeRate;
		if (nullPower > 0 && parent.tickCount % ((voidmancerStance ? 2 : 1) * (1 + stats.rechargeRate)) == 0)
			nullPower--;
		if (insanePower > 0 && parent.tickCount % 10 == 0)
			insanePower--;
		if (parent.level instanceof ServerWorld && ramTarget != null && ramTimeout-- > 0) {
			Vector3d dist = ramTarget.subtract(parent.position());
			if ((dist.x * dist.x + dist.y * dist.y + dist.z * dist.z) / 2D <= 1D) {
				final boolean flag = ((ServerWorld) parent.level).getChunkSource().getGenerator() instanceof InstanceChunkGenerator;
				parent.level.getEntities(parent, parent.getBoundingBox().inflate(1F, 1F, 1F), e -> !flag || !(e instanceof PlayerEntity)).forEach(e -> {
					e.hurt(parent instanceof PlayerEntity ? DamageSource.playerAttack((PlayerEntity) parent) : parent instanceof LivingEntity ? DamageSource.mobAttack((LivingEntity) parent) : DamageSource.GENERIC, ramDamage);
					for (int i = 0; i < 10; i++) {
						Vector3d pos = new Vector3d(0.25F + parent.level.getRandom().nextFloat() * 0.75F, 0, 0).
								yRot((float) Math.toRadians(parent.level.getRandom().nextInt(360))).
								xRot((float) Math.toRadians(parent.level.getRandom().nextInt(360))).
								add(e.getX(), e.getEyeY() - 0.5F + parent.level.getRandom().nextFloat(), e.getZ());
						((ServerWorld) parent.level).sendParticles(ParticleTypes.CRIT, pos.x, pos.y, pos.z, 0, 0, 0, 0, 1);
					}
				});
				parent.level.playSound(null, parent, SoundEvents.BLAZE_HURT, SoundCategory.PLAYERS, 1F, 0.75F + parent.level.getRandom().nextFloat() * 0.5F);
				ramTarget = null;
				ramDamage = 0;
				ramTimeout = 0;
			} else if (!parent.getDeltaMovement().equals(dist.normalize())) {
				parent.setDeltaMovement(dist.normalize());
				if (parent instanceof ServerPlayerEntity)
					((ServerPlayerEntity) parent).connection.send(new SEntityVelocityPacket(parent));
			}
		}
	}

	@Override
	public ResourceLocation id() {
		return ID;
	}

	@Override
	public CompoundNBT write(CompoundNBT nbt, @Nullable Direction side) {
		nbt.putIntArray("slots", Arrays.stream(slots).mapToInt(slot -> slot == null ? -1 : slot.ability().id()).toArray());
		return nbt;
	}

	@Override
	public void read(CompoundNBT nbt, @Nullable Direction side) {
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
	public void write(PacketBuffer buffer) {
		buffer.writeInt(voidicPower);
		buffer.writeInt(nullPower);
		buffer.writeInt(insanePower);
		stats.encode(buffer);
		for (TurmoilAbilityInstance slot : slots) {
			if (slot == null) {
				buffer.writeVarInt(-1);
				buffer.writeLong(0L);
			} else
				slot.encode(buffer);
		}
	}

	@Override
	public void read(PacketBuffer buffer) {
		voidicPower = buffer.readInt();
		nullPower = buffer.readInt();
		insanePower = buffer.readInt();
		stats = TurmoilSkill.Stats.decode(buffer);
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

	public void ramTowards(Vector3d target, float damage) {
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
}
