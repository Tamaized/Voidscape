package tamaized.voidscape.turmoil;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.entity.EntityCorruptedPawnPhantom;
import tamaized.voidscape.entity.IEthereal;
import tamaized.voidscape.registry.ModAttributes;

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

	private float paranoia;
	private float infusion;
	public boolean decrementEffects = true;

	private EntityCorruptedPawnPhantom hunt;

	private boolean dirty;

	@Override
	public void tick(Entity parent) {
		if (parent instanceof IEthereal ethereal && ethereal.insanityImmunity()) {
			paranoia = 0;
			infusion = 0;
			return;
		}
		if (Voidscape.checkForVoidDimension(parent.level) && !parent.isSpectator()) {
			paranoia += calcParanoiaRate(parent) / 20F;
			infusion += calcInfusionRate(parent) / 20F;
		} else if (parent.isSpectator() || decrementEffects) {
			paranoia = 0;
			infusion--;
		}
		decrementEffects = true;
		paranoia = Mth.clamp(paranoia, 0, 600);
		infusion = Mth.clamp(infusion, 0, 600);
		if (parent instanceof LivingEntity && !parent.level.isClientSide() && (parent.tickCount % 20 * 10 == 0 || dirty)) {
			refreshEquipmentAttributes((LivingEntity) parent);
			sendToClients(parent);
			dirty = false;
		}
		if (parent instanceof LivingEntity)
			calculateEffects((LivingEntity) parent);
	}

	private void calculateEffects(LivingEntity parent) {
		float perc = infusion / 600F;
		AttributeInstance attribute = parent.getAttribute(Attributes.MAX_HEALTH);
		if (attribute != null) {
			attribute.removeModifier(INFUSION_HEALTH_DECAY);
			attribute.removeModifier(INFUSION_ATTACK_DAMAGE);
			attribute.removeModifier(INFUSION_RESISTANCE);
			if (perc > 0F) {
				final float bound = 1F / parent.getMaxHealth();
				attribute.addTransientModifier(new AttributeModifier(INFUSION_HEALTH_DECAY, "Voidic Infusion Health Decay", Math.max((1F - perc) - 1F, bound - 1F), AttributeModifier.Operation.MULTIPLY_TOTAL));
				attribute.addTransientModifier(new AttributeModifier(INFUSION_ATTACK_DAMAGE, "Voidic Infusion Voidic Attack Damage", 10F * perc, AttributeModifier.Operation.ADDITION));
				attribute.addTransientModifier(new AttributeModifier(INFUSION_RESISTANCE, "Voidic Infusion Voidic Resistance", 10F * perc, AttributeModifier.Operation.ADDITION));
				final float maxHealth = parent.getMaxHealth();
				if (parent.getHealth() > maxHealth)
					parent.setHealth(parent.getMaxHealth());
			}
			if (perc >= 1F && Voidscape.checkForVoidDimension(parent.level))
				parent.hurt(DamageSource.OUT_OF_WORLD, 1024F);
		}
		if (parent instanceof Player) {
			float sanity = paranoia / 600F;
			if (parent.level.isClientSide() && sanity > 0.25F && parent.tickCount % (20 * 5) == 0 && parent.getRandom().nextFloat() <= 0.1F)
				parent.level.playSound((Player) parent,

						parent.blockPosition().offset(parent.getRandom().nextInt(30) - 30, parent.getRandom().nextInt(30) - 30, parent.getRandom().nextInt(30) - 30),

						PARANOIA_SOUNDS[parent.getRandom().nextInt(PARANOIA_SOUNDS.length)],

						SoundSource.MASTER,

						parent.getRandom().nextFloat() * 0.9F + 0.1F,

						parent.getRandom().nextFloat() * 0.5F + 0.5F);
			if (!parent.level.isClientSide() && sanity > 0.5F && parent.tickCount % (20 * 5) == 0 && parent.getRandom().nextFloat() <= 0.1F)
				parent.hurt(DamageSource.GENERIC, 1F);
			if (parent.level.isClientSide() && sanity > 0.75F && parent.tickCount % (82) == 0)
				parent.level.playSound((Player) parent,

						parent.blockPosition(),

						SoundEvents.CONDUIT_AMBIENT,

						SoundSource.MASTER,

						4F,

						1F);
			if (!parent.level.isClientSide()) {
				if (hunt == null && paranoia >= 600 && parent.getCapability(SubCapability.CAPABILITY).map(cap -> cap.get(Voidscape.subCapTurmoilData).map(data -> data.
						getProgression().ordinal() >= Progression.PostTutorial.ordinal() && data.getProgression().ordinal() < Progression.CorruptPawnPost.ordinal()).orElse(false)).orElse(false)) {
					hunt = new EntityCorruptedPawnPhantom(parent.level).target((Player) parent);
					Vec3 vec = new Vec3(0, 100, 0).xRot(parent.getRandom().nextFloat() * 2F - 1F).yRot(parent.getRandom().nextFloat() * 2F - 1F);
					hunt.setPos(parent.getX() + vec.x(), parent.getY() + vec.y(), parent.getZ() + vec.z());
					parent.level.addFreshEntity(hunt);
				}
				if (hunt != null) {
					if (!hunt.isAlive()) {
						hunt = null;
					} else if (!Voidscape.checkForVoidDimension(parent.level) || paranoia < 600) {
						hunt.remove(Entity.RemovalReason.DISCARDED);
						hunt = null;
					} else {
						if (parent.distanceTo(hunt) <= 1F) {
							parent.hurt(DamageSource.OUT_OF_WORLD, 1024);
							parent.getCapability(SubCapability.CAPABILITY).ifPresent(cap -> cap.get(Voidscape.subCapTurmoilData).ifPresent(data -> {
								if (data.getProgression().ordinal() < Progression.CorruptPhantom.ordinal()) {
									data.setProgression(Progression.CorruptPhantom);
									data.levelUp();
								}
							}));
						}
					}
				}
			}
		}
	}

	public EntityCorruptedPawnPhantom getHunter() {
		return hunt;
	}

	public float calcInfusionRate(Entity parent) {
		if (parent instanceof LivingEntity entity) {
			return 2F - (float) entity.getAttributeValue(ModAttributes.VOIDIC_INFUSION_RES.get());
		}
		return 1F;
	}


	private void refreshEquipmentAttributes(LivingEntity entity) {
		for (EquipmentSlot equipmentslottype : EquipmentSlot.values()) {
			ItemStack itemstack = entity.getItemBySlot(equipmentslottype);
			if (!itemstack.isEmpty()) {
				entity.getAttributes().removeAttributeModifiers(itemstack.getAttributeModifiers(equipmentslottype));
				entity.getAttributes().addTransientAttributeModifiers(itemstack.getAttributeModifiers(equipmentslottype));
			}

		}
	}

	public float calcParanoiaRate(Entity parent) {
		return 0.87F * parent.getCapability(SubCapability.CAPABILITY).map(cap -> cap.get(Voidscape.subCapTurmoilData).map(data -> data.
				getProgression().ordinal() >= Progression.CorruptPawnPost.ordinal() ? 0.1F : 1F).orElse(1F)).orElse(1F);
	}

	public float getInfusion() {
		return infusion;
	}

	public void setInfusion(float amount) {
		infusion = amount;
		dirty = true;
	}

	public float getParanoia() {
		return paranoia;
	}

	public void setParanoia(int amount) {
		paranoia = amount;
		dirty = true;
	}

	@Override
	public CompoundTag write(CompoundTag nbt, @Nullable Direction side) {
		nbt.putFloat("paranoia", paranoia);
		nbt.putFloat("infusion", infusion);
		return nbt;
	}

	@Override
	public void read(CompoundTag nbt, @Nullable Direction side) {
		paranoia = nbt.getFloat("paranoia");
		infusion = nbt.getFloat("infusion");
	}

	@Override
	public ResourceLocation id() {
		return ID;
	}

	@Override
	public void write(FriendlyByteBuf buffer) {
		buffer.writeFloat(paranoia);
		buffer.writeFloat(infusion);
	}

	@Override
	public void read(FriendlyByteBuf buffer) {
		paranoia = buffer.readFloat();
		infusion = buffer.readFloat();
	}

	@Override
	public void clone(SubCapability.ISubCap.ISubCapData old, boolean death) {
		if (old instanceof Insanity o) {
			if (!death) {
				infusion = o.infusion;
				paranoia = o.paranoia;
			}
		}
	}
}
