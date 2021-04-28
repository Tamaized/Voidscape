package tamaized.voidscape.turmoil;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import tamaized.voidscape.Voidscape;

public class BindData implements SubCapability.ISubCap.ISubCapData.INetworkHandler, SubCapability.ISubCap.ISubCapData.ITickHandler {

	private static final ResourceLocation ID = new ResourceLocation(Voidscape.MODID, "bind");

	public boolean bound;

	@Override
	public void clone(SubCapability.ISubCap.ISubCapData old, boolean death) {
		if (!death && old instanceof BindData)
			bound = ((BindData) old).bound;
	}

	@Override
	public ResourceLocation id() {
		return ID;
	}

	@Override
	public void write(PacketBuffer buffer) {
		buffer.writeBoolean(bound);
	}

	@Override
	public void read(PacketBuffer buffer) {
		bound = buffer.readBoolean();
	}

	@Override
	public void tick(Entity parent) {
		parent.canUpdate(parent.isSpectator() || !bound);
		if (parent instanceof ServerPlayerEntity && parent.tickCount % 20 == 0)
			sendToClients(parent);
		if (!parent.level.isClientSide())
			bound = false;
	}
}
