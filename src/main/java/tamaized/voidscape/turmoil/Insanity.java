package tamaized.voidscape.turmoil;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.registry.ModAttributes;

public class Insanity implements SubCapability.ISubCap.ISubCapData.All {

	private static final ResourceLocation ID = new ResourceLocation(Voidscape.MODID, "insanity");

	private float paranoia;
	private float infusion;

	@Override
	public void tick(Entity parent) {
		if (Voidscape.checkForVoidDimension(parent.level)) {
			paranoia += calcParanoiaRate(parent);
			infusion += calcInfusionRate(parent);
		} else {
			paranoia--;
		}
		paranoia = MathHelper.clamp(paranoia, 0, 100);
		infusion = MathHelper.clamp(infusion, 0, 100);
	}

	private float calcInfusionRate(Entity parent) {
		return 1F * (parent instanceof LivingEntity ? (float) ((LivingEntity) parent).getAttributeValue(ModAttributes.VOIDIC_INFUSION_RES.get()) : 1F);
	}

	private float calcParanoiaRate(Entity parent) {
		return 0.85F;
	}

	@Override
	public CompoundNBT write(CompoundNBT nbt, Direction side) {
		nbt.putFloat("paranoia", paranoia);
		nbt.putFloat("infusion", infusion);
		return nbt;
	}

	@Override
	public void read(CompoundNBT nbt, Direction side) {
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
}
