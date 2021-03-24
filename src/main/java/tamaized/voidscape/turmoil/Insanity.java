package tamaized.voidscape.turmoil;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.entity.EntityCorruptedPawnPhantom;
import tamaized.voidscape.registry.ModAttributes;

import javax.annotation.Nullable;
import java.util.UUID;

public class Insanity implements SubCapability.ISubCap.ISubCapData.All {

	private static final ResourceLocation ID = new ResourceLocation(Voidscape.MODID, "insanity");
	private static final UUID INFUSION_HEALTH_DECAY = UUID.fromString("56ace1bf-6e7f-4724-b4d6-4012519a5b5d");

	private static final SoundEvent[] PARANOIA_SOUNDS = new SoundEvent[]{

			SoundEvents.CREEPER_PRIMED, SoundEvents.ENDERMAN_AMBIENT, SoundEvents.ENDERMAN_SCREAM, SoundEvents.ZOMBIFIED_PIGLIN_AMBIENT,

			SoundEvents.ZOMBIFIED_PIGLIN_ANGRY, SoundEvents.ZOMBIFIED_PIGLIN_HURT, SoundEvents.CAT_HISS};

	private float paranoia;
	private float infusion;

	private EntityCorruptedPawnPhantom hunt;

	private boolean dirty;

	@Override
	public void tick(Entity parent) {
		if (Voidscape.checkForVoidDimension(parent.level) && !parent.isSpectator()) {
			paranoia += calcParanoiaRate(parent) / 20F;
			infusion += calcInfusionRate(parent) / 20F;
		} else {
			paranoia = 0;
			infusion--;
		}
		paranoia = MathHelper.clamp(paranoia, 0, 600);
		infusion = MathHelper.clamp(infusion, 0, 600);
		if (parent instanceof ServerPlayerEntity && !parent.level.isClientSide() && (parent.tickCount % 20 * 10 == 0 || dirty)) {
			refreshEquipmentAttributes((LivingEntity) parent);
			sendToClient((ServerPlayerEntity) parent);
			dirty = false;
		}
		if (parent instanceof LivingEntity)
			calculateEffects((LivingEntity) parent);
	}

	private void calculateEffects(LivingEntity parent) {
		float perc = infusion / 600F;
		ModifiableAttributeInstance attribute = parent.getAttribute(Attributes.MAX_HEALTH);
		if (attribute != null) {
			attribute.removeModifier(INFUSION_HEALTH_DECAY);
			if (perc > 0F)
				attribute.addTransientModifier(new AttributeModifier(INFUSION_HEALTH_DECAY, "Voidic Infusion Health Decay", (1F - perc) - 1F, AttributeModifier.Operation.MULTIPLY_TOTAL));
			if (perc >= 1F && Voidscape.checkForVoidDimension(parent.level))
				parent.hurt(DamageSource.OUT_OF_WORLD, 1024F);
		}
		if (parent instanceof PlayerEntity) {
			float sanity = paranoia / 600F;
			if (parent.level.isClientSide() && sanity > 0.25F && parent.tickCount % (20 * 5) == 0 && parent.getRandom().nextFloat() <= 0.1F)
				parent.level.playSound((PlayerEntity) parent,

						parent.blockPosition().offset(parent.getRandom().nextInt(30) - 30, parent.getRandom().nextInt(30) - 30, parent.getRandom().nextInt(30) - 30),

						PARANOIA_SOUNDS[parent.getRandom().nextInt(PARANOIA_SOUNDS.length)],

						SoundCategory.MASTER,

						parent.getRandom().nextFloat() * 0.9F + 0.1F,

						parent.getRandom().nextFloat() * 0.5F + 0.5F);
			if (!parent.level.isClientSide() && sanity > 0.5F && parent.tickCount % (20 * 5) == 0 && parent.getRandom().nextFloat() <= 0.1F)
				parent.hurt(DamageSource.GENERIC, 1F);
			if (parent.level.isClientSide() && sanity > 0.75F && parent.tickCount % (82) == 0)
				parent.level.playSound((PlayerEntity) parent,

						parent.blockPosition(),

						SoundEvents.CONDUIT_AMBIENT,

						SoundCategory.MASTER,

						4F,

						1F);
			if (!parent.level.isClientSide()) {
				if (hunt == null && paranoia >= 600 && parent.getCapability(SubCapability.CAPABILITY).map(cap -> cap.get(Voidscape.subCapTurmoilData).map(data -> data.
						getProgression().ordinal() >= Progression.PostTutorial.ordinal() && data.getProgression().ordinal() < Progression.CorruptPawnPost.ordinal()).orElse(false)).orElse(false)) {
					hunt = new EntityCorruptedPawnPhantom(parent.level).target((PlayerEntity) parent);
					Vector3d vec = new Vector3d(0, 100, 0).xRot(parent.getRandom().nextFloat() * 2F - 1F).yRot(parent.getRandom().nextFloat() * 2F - 1F);
					hunt.setPos(parent.getX() + vec.x(), parent.getY() + vec.y(), parent.getZ() + vec.z());
					parent.level.addFreshEntity(hunt);
				}
				if (hunt != null) {
					if (!hunt.isAlive()) {
						hunt = null;
					} else if (!Voidscape.checkForVoidDimension(parent.level) || paranoia < 600) {
						hunt.remove();
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

	private float calcInfusionRate(Entity parent) {
		if (parent instanceof LivingEntity) {
			LivingEntity entity = (LivingEntity) parent;
			ModifiableAttributeInstance attribute = entity.getAttribute(ModAttributes.VOIDIC_INFUSION_RES.get());
			if (attribute != null && attribute.getBaseValue() == 0)
				attribute.setBaseValue(1);
			return 1F * 2F - (float) entity.getAttributeValue(ModAttributes.VOIDIC_INFUSION_RES.get());
		}
		return 1F;
	}


	private void refreshEquipmentAttributes(LivingEntity entity) {
		for (EquipmentSlotType equipmentslottype : EquipmentSlotType.values()) {
			ItemStack itemstack = entity.getItemBySlot(equipmentslottype);
			if (!itemstack.isEmpty()) {
				entity.getAttributes().removeAttributeModifiers(itemstack.getAttributeModifiers(equipmentslottype));
				entity.getAttributes().addTransientAttributeModifiers(itemstack.getAttributeModifiers(equipmentslottype));
			}

		}
	}

	private float calcParanoiaRate(Entity parent) {
		return 0.87F * parent.getCapability(SubCapability.CAPABILITY).map(cap -> cap.get(Voidscape.subCapTurmoilData).map(data -> data.
				getProgression().ordinal() >= Progression.CorruptPawnPost.ordinal() ? 0.1F : 1F).orElse(1F)).orElse(1F);
	}

	public float getInfusion() {
		return infusion;
	}

	public void setInfusion(int amount) {
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
	public CompoundNBT write(CompoundNBT nbt, @Nullable Direction side) {
		nbt.putFloat("paranoia", paranoia);
		nbt.putFloat("infusion", infusion);
		return nbt;
	}

	@Override
	public void read(CompoundNBT nbt, @Nullable Direction side) {
		paranoia = nbt.getFloat("paranoia");
		infusion = nbt.getFloat("infusion");
	}

	@Override
	public ResourceLocation id() {
		return ID;
	}

	@Override
	public void write(PacketBuffer buffer) {
		buffer.writeFloat(paranoia);
		buffer.writeFloat(infusion);
	}

	@Override
	public void read(PacketBuffer buffer) {
		paranoia = buffer.readFloat();
		infusion = buffer.readFloat();
	}

	@Override
	public void clone(SubCapability.ISubCap.ISubCapData old, boolean death) {
		if (old instanceof Insanity) {
			Insanity o = (Insanity) old;
			if (!death) {
				infusion = o.infusion;
				paranoia = o.paranoia;
			}
		}
	}
}
