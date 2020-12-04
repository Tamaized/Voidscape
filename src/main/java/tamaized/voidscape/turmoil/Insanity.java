package tamaized.voidscape.turmoil;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import tamaized.voidscape.Voidscape;

public class Insanity implements SubCapability.ISubCap.ISubCapData.All {

	private static final ResourceLocation ID = new ResourceLocation(Voidscape.MODID, "insanity");

	private int paranoia;

	@Override
	public void tick(Entity parent) {
		if (Voidscape.checkForVoidDimension(parent.level)) {
			if (parent.level.random.nextInt(50) == 0)
				paranoia++;
		} else
			paranoia--;
		paranoia = MathHelper.clamp(paranoia, 0, 100);
	}

	@Override
	public CompoundNBT write(CompoundNBT nbt, Direction side) {
		nbt.putInt("paranoia", paranoia);
		return nbt;
	}

	@Override
	public void read(CompoundNBT nbt, Direction side) {
		paranoia = nbt.getInt("paranoia");
	}

	@Override
	public ResourceLocation id() {
		return ID;
	}

	@Override
	public void write(PacketBuffer buffer) {
		buffer.writeInt(paranoia);
	}

	@Override
	public void read(PacketBuffer buffer) {
		paranoia = buffer.readInt();
	}
}
