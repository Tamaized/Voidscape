package tamaized.voidscape.turmoil;

public class Insanity { /*implements Turmoil.ITurmoilData.ITurmoil.ITickHandler, Turmoil.ITurmoilData.ITurmoil.IStorageHandler, Turmoil.ITurmoilData.ITurmoil.INetworkHandler {

	private static final ResourceLocation ID = new ResourceLocation(Voidscape.MODID, "insanity");

	private int paranoia;

	@Override
	public void tick(Entity parent) {
		*//*if (parent.world.dimension.getType().getId() == Voidscape.getDimensionTypeID()) {
			if (parent.world.rand.nextInt(50) == 0)
				paranoia++;
		} else
			paranoia--;
		paranoia = MathHelper.clamp(paranoia, 0, 100);*//*
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
	}*/
}
