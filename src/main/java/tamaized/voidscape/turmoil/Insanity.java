package tamaized.voidscape.turmoil;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.registry.ModAttributes;

import javax.annotation.Nullable;
import java.util.UUID;

public class Insanity implements SubCapability.ISubCap.ISubCapData.All {

	private static final ResourceLocation ID = new ResourceLocation(Voidscape.MODID, "insanity");
	private static final UUID INFUSION_HEALTH_DECAY = UUID.fromString("56ace1bf-6e7f-4724-b4d6-4012519a5b5d");

	private float paranoia;
	private float infusion;

	@Override
	public void tick(Entity parent) {
		if (Voidscape.checkForVoidDimension(parent.level)) {
			paranoia += calcParanoiaRate(parent) / 20F;
			infusion += calcInfusionRate(parent) / 20F;
		} else {
			paranoia--;
			infusion--;
		}
		paranoia = MathHelper.clamp(paranoia, 0, 600);
		infusion = MathHelper.clamp(infusion, 0, 600);
		if (parent instanceof ServerPlayerEntity && !parent.level.isClientSide() && parent.tickCount % 20 * 10 == 0) {
			refreshEquipmentAttributes((LivingEntity) parent);
			sendToClient((ServerPlayerEntity) parent);
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
		return 0.85F;
	}

	public float getInfusion() {
		return infusion;
	}

	public float getParanoia() {
		return paranoia;
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
