package tamaized.voidscape.capability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.entity.CorruptedPawnEntity;
import tamaized.voidscape.entity.IEthereal;
import tamaized.voidscape.network.client.ClientPacketNoFlashOnSetHealth;
import tamaized.voidscape.registry.*;
import tamaized.voidscape.world.VoidPortalTeleporter;
import tamaized.voidscape.world.VoidTeleporter;

import javax.annotation.Nullable;
import java.util.UUID;

public class Insanity implements SubCapability.ISubCap.ISubCapData.All {

	private static final ResourceLocation ID = new ResourceLocation(Voidscape.MODID, "insanity");
	private static final UUID INFUSION_HEALTH_DECAY = UUID.fromString("56ace1bf-6e7f-4724-b4d6-4012519a5b5d");
	private static final UUID INFUSION_ATTACK_DAMAGE = UUID.fromString("08eecf1b-9bbb-46eb-be7e-76308d1241e7");
	private static final UUID INFUSION_RESISTANCE = UUID.fromString("4fe870c1-c74f-4856-b30d-7a4311d72639");

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

	private CorruptedPawnEntity hunt;

	private boolean dirty;

	public void setInPortal(boolean flag) {
		inPortal = flag;
	}

	private boolean canTeleport(Entity parent) {
		return parent.getY() <= parent.level().getMinBuildHeight() + 15 &&
				parent.level().getBlockState(parent.getOnPos()).is(Blocks.BEDROCK);
	}

	private boolean shouldTeleport(Entity parent) {
		return parent.tickCount % 20 == 0 &&
				parent.level().getRandom().nextInt(8) == 0 &&
				canTeleport(parent);
	}

	@Override
	public void tick(Entity parent) {
		if (!parent.level().isClientSide()) {
			if (inPortal) {
				inPortal = false;
				teleportTick++;
				if (teleportTick % 20 == 0) {
					dirty = true;
				}
				teleportTick = Mth.clamp(teleportTick, 0, 200);
				if (!pleaseLeavePortal && teleportTick >= 200) {
					if (Voidscape.checkForVoidDimension(parent.level())) {
						Voidscape.getLevel(parent.level(), Level.OVERWORLD).ifPresent(level -> parent.changeDimension(level, VoidPortalTeleporter.INSTANCE));
					} else {
						Voidscape.getLevel(parent.level(), Voidscape.WORLD_KEY_VOID).ifPresent(level -> parent.changeDimension(level, VoidPortalTeleporter.INSTANCE));
					}
				}
			} else {
				pleaseLeavePortal = false;
				boolean inVoid;
				if (inVoid = Voidscape.checkForVoidDimension(parent.level())) {
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
					Voidscape.getLevel(parent.level(), Voidscape.WORLD_KEY_VOID).ifPresent(level -> parent.changeDimension(level, VoidTeleporter.INSTANCE));
					return;
				}
			}
		}
		if (parent instanceof IEthereal ethereal && ethereal.insanityImmunity()) {
			paranoia = 0;
			infusion = 0;
		}
		if (Voidscape.checkForVoidDimension(parent.level()) && !parent.isSpectator()) {
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
		paranoia = Mth.clamp(paranoia, 0, 600);
		infusion = Mth.clamp(infusion, 0, 600);
		if (parent instanceof LivingEntity && !parent.level().isClientSide() && (parent.tickCount % 20 * 10 == 0 || dirty)) {
			refreshEquipmentAttributes((LivingEntity) parent);
			sendToClients(parent);
			dirty = false;
		}
		if (parent instanceof LivingEntity living) {
			calculateEffects(living);
			if (!living.level().isClientSide()) {
				if (living.hasEffect(ModEffects.AURA.get()))
					aura = true;
				else
					aura = false;
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
				entity.level().addParticle(new ModParticles.ParticleSpellCloudData(0x7700FF), pos.x(), pos.y(), pos.z(), 0, 0, 0);
			}
		} else if (entity.tickCount % 20 == 0) {
			entity.level().getEntities(entity, new AABB(entity.position().add(-0.5D, -0.5F, -0.5F), entity.position().add(0.5F, 0.5F, 0.5F)).inflate(2D))
					.forEach(e -> e.hurt(
							ModDamageSource.getEntityDamageSource(entity.level(), ModDamageSource.VOIDIC, entity),
							(float) (2D + entity.getAttributeValue(ModAttributes.VOIDIC_DMG.get()) / 2D)
					));
		}
	}

	public int getTeleportTick() {
		return teleportTick;
	}

	private void calculateEffects(LivingEntity parent) {
		float perc = infusion / 600F;
		if (parent.tickCount % 20 == 0) {
			AttributeInstance attributeMaxHealth = parent.getAttribute(Attributes.MAX_HEALTH);
			AttributeInstance attributeVoidicAttackDamage = parent.getAttribute(ModAttributes.VOIDIC_DMG.get());
			AttributeInstance attributeVoidicResistance = parent.getAttribute(ModAttributes.VOIDIC_RES.get());
			if (attributeMaxHealth != null && attributeVoidicAttackDamage != null && attributeVoidicResistance != null) {
				attributeMaxHealth.removeModifier(INFUSION_HEALTH_DECAY);
				attributeVoidicAttackDamage.removeModifier(INFUSION_ATTACK_DAMAGE);
				attributeVoidicResistance.removeModifier(INFUSION_RESISTANCE);
				if (perc > 0F) {
					final float bound = 1F / parent.getMaxHealth();
					attributeMaxHealth.addTransientModifier(new AttributeModifier(INFUSION_HEALTH_DECAY, "Voidic Infusion Health Decay", Math.max((1F - perc) - 1F, bound - 1F), AttributeModifier.Operation.MULTIPLY_TOTAL));
					attributeVoidicAttackDamage.addTransientModifier(new AttributeModifier(INFUSION_ATTACK_DAMAGE, "Voidic Infusion Voidic Attack Damage", 10F * perc, AttributeModifier.Operation.ADDITION));
					attributeVoidicResistance.addTransientModifier(new AttributeModifier(INFUSION_RESISTANCE, "Voidic Infusion Voidic Resistance", 10F * perc, AttributeModifier.Operation.ADDITION));
					if (parent.getHealth() > parent.getMaxHealth()) {
						if (parent instanceof ServerPlayer player)
							Voidscape.NETWORK.send(PacketDistributor.PLAYER.with(() -> player), new ClientPacketNoFlashOnSetHealth());
						parent.setHealth(parent.getMaxHealth());
					}
				}
				if (perc >= 1F && Voidscape.checkForVoidDimension(parent.level())) {
					if (parent instanceof ServerPlayer player)
						ModAdvancementTriggers.INFUSED_TRIGGER.trigger(player);
					parent.hurt(parent.damageSources().fellOutOfWorld(), 1024F);
				}
			}
		}
		if (parent instanceof Player) {
			float sanity = paranoia / 600F;
			if (parent.level().isClientSide() && sanity > 0.25F && parent.tickCount % (20 * 5) == 0 && parent.getRandom().nextFloat() <= 0.1F)
				parent.level().playSound((Player) parent,

						parent.blockPosition().offset(parent.getRandom().nextInt(30) - 30, parent.getRandom().nextInt(30) - 30, parent.getRandom().nextInt(30) - 30),

						PARANOIA_SOUNDS[parent.getRandom().nextInt(PARANOIA_SOUNDS.length)],

						SoundSource.MASTER,

						parent.getRandom().nextFloat() * 0.9F + 0.1F,

						parent.getRandom().nextFloat() * 0.5F + 0.5F);
			if (!parent.level().isClientSide() && sanity > 0.5F && parent.tickCount % (20 * 5) == 0 && parent.getRandom().nextFloat() <= 0.1F)
				parent.hurt(parent.damageSources().generic(), 1F);
			if (parent.level().isClientSide() && sanity > 0.75F && parent.tickCount % (82) == 0)
				parent.level().playSound((Player) parent,

						parent.blockPosition(),

						SoundEvents.CONDUIT_AMBIENT,

						SoundSource.MASTER,

						4F,

						1F);
			if (!parent.level().isClientSide()) {
				if (hunt == null && paranoia >= 600) {
					hunt = new CorruptedPawnEntity(parent.level()).target((Player) parent);
					Vec3 vec = new Vec3(0, 100, 0).xRot(parent.getRandom().nextFloat() * 2F - 1F).yRot(parent.getRandom().nextFloat());
					hunt.setPos(parent.getX() + vec.x(), parent.getY() + vec.y(), parent.getZ() + vec.z());
					parent.level().addFreshEntity(hunt);
				}
				if (hunt != null) {
					if (hunt.isRemoved()) {
						hunt = null;
					} else if (!hunt.isAlive()) {
						paranoia = 0;
					} else if (!Voidscape.checkForVoidDimension(parent.level()) || paranoia < 600) {
						hunt.remove(Entity.RemovalReason.DISCARDED);
						hunt = null;
					} else {
						if (parent.distanceTo(hunt) <= 0.25F) {
							parent.hurt(parent.damageSources().fellOutOfWorld(), 1024);
						}
					}
				}
			}
		}
	}

	public CorruptedPawnEntity getHunter() {
		return hunt;
	}

	public float calcInfusionRate(Entity parent) {
		if (parent instanceof LivingEntity entity) {
			return Mth.clamp(2F - (float) entity.getAttributeValue(ModAttributes.VOIDIC_INFUSION_RES.get()), 0F, 1F);
		}
		return 1F;
	}

	private void refreshEquipmentAttributes(LivingEntity entity) {
		for (EquipmentSlot equipmentSlotType : EquipmentSlot.values()) {
			ItemStack itemstack = entity.getItemBySlot(equipmentSlotType);
			if (!itemstack.isEmpty()) {
				entity.getAttributes().removeAttributeModifiers(itemstack.getAttributeModifiers(equipmentSlotType));
				entity.getAttributes().addTransientAttributeModifiers(itemstack.getAttributeModifiers(equipmentSlotType));
			}

		}
	}

	public float calcParanoiaRate(Entity parent) {
		if (parent instanceof LivingEntity entity) {
			return Mth.clamp(2F - (float) entity.getAttributeValue(ModAttributes.VOIDIC_PARANOIA_RES.get()), 0F, 1F) * 0.9F;
		}
		return 1F;
	}

	public float getInfusion() {
		return infusion;
	}

	public void decrementInfusion(float amount) {
		decrementInfusion = amount;
	}

	public void setInfusion(float amount) {
		float prev = infusion;
		infusion = Mth.clamp(amount, 0, 600);
		dirty = prev != infusion;
	}

	public float getParanoia() {
		return paranoia;
	}

	public void setParanoia(float amount) {
		float prev = paranoia;
		paranoia = Mth.clamp(amount, 0, 600);
		dirty = prev != paranoia;
	}

	@Override
	public CompoundTag write(CompoundTag nbt, @Nullable Direction side) {
		nbt.putFloat("paranoia", paranoia);
		nbt.putFloat("infusion", infusion);
		nbt.putBoolean("inPortal", inPortal);
		nbt.putBoolean("pleaseLeavePortal", pleaseLeavePortal);
		nbt.putInt("teleportTick", teleportTick);
		return nbt;
	}

	@Override
	public void read(CompoundTag nbt, @Nullable Direction side) {
		paranoia = nbt.getFloat("paranoia");
		infusion = nbt.getFloat("infusion");
		inPortal = nbt.getBoolean("inPortal");
		pleaseLeavePortal = nbt.getBoolean("pleaseLeavePortal");
		teleportTick = nbt.getInt("teleportTick");
	}

	@Override
	public ResourceLocation id() {
		return ID;
	}

	@Override
	public void write(FriendlyByteBuf buffer) {
		buffer.writeFloat(paranoia);
		buffer.writeFloat(infusion);
		buffer.writeInt(teleportTick);
		buffer.writeBoolean(aura);
	}

	@Override
	public void read(FriendlyByteBuf buffer) {
		paranoia = buffer.readFloat();
		infusion = buffer.readFloat();
		teleportTick = buffer.readInt();
		aura = buffer.readBoolean();
	}

	@Override
	public void clone(SubCapability.ISubCap.ISubCapData old, boolean death) {
		if (old instanceof Insanity o) {
			if (!death) {
				infusion = o.infusion;
				paranoia = o.paranoia;
				inPortal = o.inPortal;
				pleaseLeavePortal = o.pleaseLeavePortal;
				teleportTick = o.teleportTick;
			}
		}
	}

}
