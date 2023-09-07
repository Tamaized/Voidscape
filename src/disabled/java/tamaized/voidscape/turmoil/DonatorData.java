package tamaized.voidscape.turmoil;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.network.DonatorHandler;

public class DonatorData implements SubCapability.ISubCap.ISubCapData.INetworkHandler, SubCapability.ISubCap.ISubCapData.ITickHandler {

	private static final ResourceLocation ID = new ResourceLocation(Voidscape.MODID, "donator");

	public boolean enabled;
	public int color;

	@Override
	public void clone(SubCapability.ISubCap.ISubCapData old, boolean death) {
		if (!death && old instanceof DonatorData) {
			enabled = ((DonatorData) old).enabled;
			color = ((DonatorData) old).color;
		}
	}

	@Override
	public ResourceLocation id() {
		return ID;
	}

	@Override
	public void write(FriendlyByteBuf buffer) {
		buffer.writeBoolean(enabled);
		buffer.writeInt(color);
	}

	@Override
	public void read(FriendlyByteBuf buffer) {
		enabled = buffer.readBoolean();
		color = buffer.readInt();
	}

	@Override
	public void tick(Entity parent) {
		if (parent instanceof ServerPlayer && parent.tickCount % 20 == 0) {
			DonatorHandler.DonatorSettings settings = DonatorHandler.settings.get(parent.getUUID());
			if (settings != null) {
				enabled = settings.enabled;
				color = settings.color;
			} else
				enabled = false;
			sendToClients(parent);
		}
	}
}
