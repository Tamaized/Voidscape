package tamaized.voidscape.data;

import net.minecraft.client.Minecraft;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;
import tamaized.beanification.Autowired;
import tamaized.beanification.Configurable;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.config.common.CommonConfig;
import tamaized.voidscape.dimension.DirectTeleporter;
import tamaized.voidscape.entity.CorruptedPawnEntity;
import tamaized.voidscape.entity.IEthereal;
import tamaized.voidscape.network.client.ClientPacketInsanitySync;
import tamaized.voidscape.network.client.ClientPacketNoFlashOnSetHealth;
import tamaized.voidscape.particle.ParticleTypeSpellCloud;
import tamaized.voidscape.registry.*;
import tamaized.voidscape.dimension.VoidPortalTeleporter;
import tamaized.voidscape.util.LevelUtil;

@Configurable
public class Insanity implements INetworkHandler, INBTSerializable<CompoundTag> { // TODO: split up this class into multiple components

	@Autowired
	private LevelUtil levelUtil;

	@Autowired
	private CommonConfig commonConfig;

	@Autowired
	private VoidPortalTeleporter voidPortalTeleporter;

	@Autowired
	private DirectTeleporter directTeleporter;

	@Autowired
	private ModEffects effects;

	@Autowired
	private ModDamageSource damageSource;

	@Autowired
	private ModAttributes attributes;

	@Autowired
	private ModAdvancementTriggers advancementTriggers;

	private static final ResourceLocation INFUSION_HEALTH_DECAY = ResourceLocation.fromNamespaceAndPath(Voidscape.MODID, "infusion_health_decay");
	private static final ResourceLocation INFUSION_ATTACK_DAMAGE = ResourceLocation.fromNamespaceAndPath(Voidscape.MODID, "infusion_attack_damage");
	private static final ResourceLocation INFUSION_RESISTANCE = ResourceLocation.fromNamespaceAndPath(Voidscape.MODID, "infusion_resistance");

	public static final float MAX_INFUSION = 600;
	public static final float MAX_PARANOIA = 600;

	private static final SoundEvent[] PARANOIA_SOUNDS = new SoundEvent[]{

			SoundEvents.CREEPER_PRIMED, SoundEvents.ENDERMAN_AMBIENT, SoundEvents.ENDERMAN_SCREAM, SoundEvents.ZOMBIFIED_PIGLIN_AMBIENT,

			SoundEvents.ZOMBIFIED_PIGLIN_ANGRY, SoundEvents.ZOMBIFIED_PIGLIN_HURT, SoundEvents.CAT_HISS};

	private boolean inPortal;
	private boolean pleaseLeavePortal;
	private boolean teleporting;
	private boolean nextTeleportStep;
	private int teleportTick;
	private float paranoia;
	private float infusion;
	public float decrementInfusion;
	private boolean aura;
	private int leapParticles;

	@Nullable
	private CorruptedPawnEntity hunt;

	private boolean dirty;

	public void setInPortal(boolean flag) {
		inPortal = flag;
	}

	public void enableLeapParticles() {
		leapParticles = 30;
		dirty = true;
	}

	private boolean canTeleport(Entity parent) {
		return parent.getY() <= parent.level().getMinBuildHeight() + 15 &&
			   parent.level().getBlockState(parent.getOnPos()).is(Blocks.BEDROCK) &&
			   commonConfig.bedrockTeleportationDimensionBlacklist.get().contains(parent.level().dimension().location().toString()) == commonConfig.bedrockTeleportationDimensionWhitelist.get();
	}

	private boolean shouldTeleport(Entity parent) {
		return parent.tickCount % 20 == 0 &&
				parent.level().getRandom().nextInt(8) == 0 &&
				canTeleport(parent);
	}

	public void tick(Entity parent) {
		if (!parent.level().isClientSide()) {
			if (leapParticles > 0)
				leapParticles--;
			if (inPortal) {
				inPortal = false;
				teleportTick++;
				if (teleportTick % 20 == 0) {
					dirty = true;
				}
				teleportTick = Mth.clamp(teleportTick, 0, 200);
				if (!pleaseLeavePortal && teleportTick >= 200) {
					levelUtil.getDimensionForTeleport(parent.level()).flatMap(destLevel -> voidPortalTeleporter.make(parent, destLevel)).ifPresent(parent::changeDimension);
				}
			} else {
				pleaseLeavePortal = false;
				boolean inVoid = levelUtil.isInVoidDimension(parent.level());
				if (inVoid) {
					int prev = teleportTick;
					teleportTick--;
					if ((teleportTick > 0 && teleportTick % 20 == 0) || teleportTick <= 0 && prev > 0)
						dirty = true;
				} else {
					if (teleporting) {
						if (!canTeleport(parent)) {
							teleporting = false;
							nextTeleportStep = false;
						} else if (!nextTeleportStep && shouldTeleport(parent)) {
							nextTeleportStep = true;
						}
						if (nextTeleportStep) {
							teleportTick++;
							if (teleportTick % 20 == 0) {
								nextTeleportStep = false;
								dirty = true;
							}
						}
					} else {
						int prev = teleportTick;
						teleportTick--;
						if ((teleportTick > 0 && teleportTick % 20 == 0) || teleportTick <= 0 && prev > 0)
							dirty = true;
						if (shouldTeleport(parent)) {
							teleporting = true;
							nextTeleportStep = true;
						}
					}
				}
				teleportTick = Mth.clamp(teleportTick, 0, 200);
				if (!inVoid && teleportTick >= 200) {
					levelUtil.getVoidDimension(parent.level()).map(destLevel -> directTeleporter.make(parent, destLevel)).ifPresent(parent::changeDimension);
					return;
				}
			}
		} else if (leapParticles > 0) {
			for (int i = 0; i < 2; i++) {
				parent.level().addParticle(
						ParticleTypes.FIREWORK,
						parent.getX() - parent.getBbWidth() / 4F + (parent.level().getRandom().nextFloat() * (parent.getBbWidth() / 4F)),
						parent.getY() + parent.getBbHeight() / 4F + (parent.level().getRandom().nextFloat() * (parent.getBbHeight() / 4F)),
						parent.getZ() - parent.getBbWidth() / 4F + (parent.level().getRandom().nextFloat() * (parent.getBbWidth() / 4F)),
						0, 0, 0);
			}
			leapParticles--;
		}
		if (levelUtil.isInVoidDimension(parent.level()) && !parent.isSpectator()) {
			paranoia += calcParanoiaRate(parent) / 20F;
			if (decrementInfusion <= 0)
				infusion += calcInfusionRate(parent) / 20F;
			else
				infusion -= decrementInfusion;
		} else {
			paranoia = 0;
			infusion--;
		}
		decrementInfusion = 0;
		paranoia = Mth.clamp(paranoia, 0, MAX_PARANOIA);
		infusion = Mth.clamp(infusion, 0, MAX_INFUSION);
		boolean infusionImmune = parent instanceof ArmorStand || (parent instanceof IEthereal ethereal && ethereal.insanityImmunity());
		if (infusionImmune) {
			paranoia = 0;
			infusion = 0;
		}
		if (parent instanceof LivingEntity && !parent.level().isClientSide() && (parent.tickCount % 20 * 10 == 0 || dirty)) {
			if (!infusionImmune)
				refreshEquipmentAttributes((LivingEntity) parent);
			sendToClients(parent);
			dirty = false;
		}
		if (parent instanceof LivingEntity living) {
			if (!infusionImmune)
				calculateEffects(living);
			if (!living.level().isClientSide()) {
				aura = living.hasEffect(effects.AURA);
			}
			if (aura)
				handleAura(living);
		}
	}

	private void handleAura(LivingEntity entity) {
		if (entity.level().isClientSide()) {
			for (int i = 0; i < 5; i++) {
				Vec3 pos = new Vec3(2, 0, 0)
						.yRot((float) Math.toRadians(entity.getRandom().nextInt(360)))
						.scale(0.2F + entity.getRandom().nextFloat() * 0.8F)
						.add(entity.position().add(0, entity.getBbHeight() / 2F, 0));
				entity.level().addParticle(new ParticleTypeSpellCloud.Options(0x7700FF), pos.x(), pos.y(), pos.z(), 0, 0, 0);
			}
		} else if (entity.tickCount % 20 == 0) {
			entity.level().getEntities(entity, new AABB(entity.position().add(-0.5D, -0.5F, -0.5F), entity.position().add(0.5F, 0.5F, 0.5F)).inflate(2D), e -> e instanceof LivingEntity)
					.forEach(e -> e.hurt(
							damageSource.getEntityDamageSource(entity.level(), damageSource.VOIDIC, entity),
							(float) (2D + entity.getAttributeValue(attributes.VOIDIC_DMG) / 2D)
					));
		}
	}

	public int getTeleportTick() {
		return teleportTick;
	}

	private void calculateEffects(LivingEntity parent) {
		float perc = infusion / MAX_INFUSION;
		if (parent.tickCount % 20 == 0) {
			AttributeInstance attributeMaxHealth = parent.getAttribute(Attributes.MAX_HEALTH);
			AttributeInstance attributeVoidicAttackDamage = parent.getAttribute(attributes.VOIDIC_DMG);
			AttributeInstance attributeVoidicResistance = parent.getAttribute(attributes.VOIDIC_RES);
			if (attributeMaxHealth != null && attributeVoidicAttackDamage != null && attributeVoidicResistance != null) {
				attributeMaxHealth.removeModifier(INFUSION_HEALTH_DECAY);
				attributeVoidicAttackDamage.removeModifier(INFUSION_ATTACK_DAMAGE);
				attributeVoidicResistance.removeModifier(INFUSION_RESISTANCE);
				if (perc > 0F) {
					final float bound = 1F / parent.getMaxHealth();
					attributeMaxHealth.addTransientModifier(new AttributeModifier(INFUSION_HEALTH_DECAY, Math.max((1F - perc) - 1F, bound - 1F), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
					attributeVoidicAttackDamage.addTransientModifier(new AttributeModifier(INFUSION_ATTACK_DAMAGE, 10F * perc, AttributeModifier.Operation.ADD_VALUE));
					attributeVoidicResistance.addTransientModifier(new AttributeModifier(INFUSION_RESISTANCE, 10F * perc, AttributeModifier.Operation.ADD_VALUE));
					if (parent.getHealth() > parent.getMaxHealth()) {
						if (parent instanceof ServerPlayer player)
							PacketDistributor.sendToPlayer(player, new ClientPacketNoFlashOnSetHealth());
						parent.setHealth(parent.getMaxHealth());
					}
				}
				if (perc >= 1F && levelUtil.isInVoidDimension(parent.level())) {
					if (parent instanceof ServerPlayer player)
						advancementTriggers.INFUSED_TRIGGER.get().trigger(player);
					parent.hurt(parent.damageSources().fellOutOfWorld(), 1024F);
				}
			}
		}
		if (parent instanceof Player) {
			float sanity = paranoia / MAX_PARANOIA;
			if (parent.level().isClientSide() && sanity > 0.25F && parent.tickCount % (20 * 5) == 0 && parent.getRandom().nextFloat() <= 0.1F)
				parent.level().playSound((Player) parent,

						parent.blockPosition().offset(parent.getRandom().nextInt(30) - 30, parent.getRandom().nextInt(30) - 30, parent.getRandom().nextInt(30) - 30),

						PARANOIA_SOUNDS[parent.getRandom().nextInt(PARANOIA_SOUNDS.length)],

						SoundSource.MASTER,

						parent.getRandom().nextFloat() * 0.9F + 0.1F,

						parent.getRandom().nextFloat() * 0.5F + 0.5F);
			if (!parent.level().isClientSide() && sanity > 0.5F && parent.tickCount % (20 * 5) == 0 && parent.getRandom().nextFloat() <= 0.1F)
				parent.hurt(parent.damageSources().generic(), 1F);
			if (parent.level().isClientSide() && sanity > 0.75F && parent.tickCount % (sanity == 1F ? 8 : sanity > 0.95F ? 10 : sanity > 0.80F ? 20 : 30) == 0)
				parent.level().playSound((Player) parent,

						parent.blockPosition(),

						SoundEvents.WARDEN_HEARTBEAT,

						SoundSource.MASTER,

						4F,

						1F);
			if (!parent.level().isClientSide()) {
				if (hunt == null && paranoia >= MAX_PARANOIA) {
					hunt = new CorruptedPawnEntity(parent.level()).target((Player) parent);
					Vec3 vec = new Vec3(0, 100, 0).xRot(parent.getRandom().nextFloat() * 2F - 1F).yRot(parent.getRandom().nextFloat());
					hunt.setPos(parent.getX() + vec.x(), parent.getY() + vec.y(), parent.getZ() + vec.z());
					parent.level().addFreshEntity(hunt);
				}
				if (hunt != null) {
					if (hunt.isRemoved()) {
						hunt = null;
						paranoia = 0;
						if (parent instanceof ServerPlayer serverPlayer)
							sendToClient(serverPlayer);
					} else if (!levelUtil.isInVoidDimension(parent.level()) || paranoia < 600) {
						hunt.remove(Entity.RemovalReason.DISCARDED);
						hunt = null;
					} else if (hunt.detectModConflict()) {
						hunt.tick();
					}
				}
			}
		}
	}

	@Nullable
	public CorruptedPawnEntity getHunter() {
		return hunt;
	}

	public float calcInfusionRate(Entity parent) {
		if (parent instanceof LivingEntity entity) {
			return Mth.clamp(2F - (float) entity.getAttributeValue(attributes.VOIDIC_INFUSION_RES), 0F, 1F);
		}
		return 1F;
	}

	private void refreshEquipmentAttributes(LivingEntity entity) {
		// TODO: investigate, is this even needed?
		/*for (EquipmentSlot equipmentSlotType : EquipmentSlot.values()) {
			ItemStack itemstack = entity.getItemBySlot(equipmentSlotType);
			if (!itemstack.isEmpty()) {
				entity.getAttributes().removeAttributeModifiers(itemstack.getAttributeModifiers(equipmentSlotType));
				entity.getAttributes().addTransientAttributeModifiers(itemstack.getAttributeModifiers(equipmentSlotType));
			}

		}*/
	}

	public float calcParanoiaRate(Entity parent) {
		if (parent instanceof LivingEntity entity) {
			return Mth.clamp(2F - (float) entity.getAttributeValue(attributes.VOIDIC_PARANOIA_RES), 0F, 1F) * 0.9F;
		}
		return 1F;
	}

	public float getInfusion() {
		return infusion;
	}

	public void decrementInfusion(float amount) {
		decrementInfusion = amount;
	}

	public void addInfusion(float amount) {
		setInfusion(infusion + amount);
	}

	public void addInfusion(float amount, LivingEntity parent) {
		addInfusion(amount * (2F - (float) parent.getAttributeValue(attributes.VOIDIC_INFUSION_RES)));
	}

	public void removeInfusion(float amount) {
		setInfusion(infusion - amount);
	}

	public void setInfusion(float amount) {
		float prev = infusion;
		infusion = Mth.clamp(amount, 0, MAX_INFUSION);
		dirty = prev != infusion;
	}

	public float getParanoia() {
		return paranoia;
	}

	public void setParanoia(float amount) {
		float prev = paranoia;
		paranoia = Mth.clamp(amount, 0, MAX_PARANOIA);
		dirty = prev != paranoia;
	}

	public void addParanoia(float amount) {
		setParanoia(paranoia + amount);
	}

	public void removeParanoia(float amount) {
		setParanoia(paranoia - amount);
	}

	@Override
	public @UnknownNullability CompoundTag serializeNBT(HolderLookup.Provider provider) {
		CompoundTag nbt = new CompoundTag();
		nbt.putFloat("paranoia", paranoia);
		nbt.putFloat("infusion", infusion);
		nbt.putBoolean("inPortal", inPortal);
		nbt.putBoolean("pleaseLeavePortal", pleaseLeavePortal);
		nbt.putInt("teleportTick", teleportTick);
		return nbt;
	}

	@Override
	public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
		paranoia = nbt.getFloat("paranoia");
		infusion = nbt.getFloat("infusion");
		inPortal = nbt.getBoolean("inPortal");
		pleaseLeavePortal = nbt.getBoolean("pleaseLeavePortal");
		teleportTick = nbt.getInt("teleportTick");
	}

	@Override
	public void write(FriendlyByteBuf buffer) {
		buffer.writeFloat(paranoia);
		buffer.writeFloat(infusion);
		buffer.writeInt(teleportTick);
		buffer.writeBoolean(aura);
		buffer.writeInt(leapParticles);
		buffer.writeInt(hunt == null ? -1 : hunt.getId());
	}

	@Override
	public void read(FriendlyByteBuf buffer) {
		paranoia = buffer.readFloat();
		infusion = buffer.readFloat();
		teleportTick = buffer.readInt();
		aura = buffer.readBoolean();
		leapParticles = buffer.readInt();
		int huntId = buffer.readInt();
		if (huntId >= 0 && Minecraft.getInstance().level != null && Minecraft.getInstance().level.getEntity(huntId) instanceof CorruptedPawnEntity pawn)
			hunt = pawn;
		else if (huntId < 0)
			hunt = null;
	}

	private void sendToClient(ServerPlayer parent) {
		PacketDistributor.sendToPlayer(parent, new ClientPacketInsanitySync(this));
	}

	private void sendToClients(Entity parent) {
		PacketDistributor.sendToPlayersTrackingEntityAndSelf(parent, new ClientPacketInsanitySync(this, parent));
	}

}
