package tamaized.voidscape.network.server;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.network.DonatorHandler;

public record ServerPacketHandlerDonatorSettings(DonatorHandler.DonatorSettings settings) implements CustomPacketPayload {

	public static final ResourceLocation ID = new ResourceLocation(Voidscape.MODID, "donatorsettings");

	public ServerPacketHandlerDonatorSettings(FriendlyByteBuf packet) {
		this(new DonatorHandler.DonatorSettings(packet.readBoolean(), packet.readInt()));
	}

	@Override
	public void write(FriendlyByteBuf packet) {
		packet.writeBoolean(settings.enabled);
		packet.writeInt(settings.color);
	}

	@Override
	public ResourceLocation id() {
		return ID;
	}

	public static void handle(ServerPacketHandlerDonatorSettings payload, PlayPayloadContext context) {
		context.player().ifPresent(player -> {
			if (DonatorHandler.donators.contains(player.getUUID()))
				DonatorHandler.settings.put(player.getUUID(), payload.settings);
		});
	}
}