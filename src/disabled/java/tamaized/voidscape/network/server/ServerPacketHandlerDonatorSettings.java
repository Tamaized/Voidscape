package tamaized.voidscape.network.server;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import tamaized.voidscape.network.DonatorHandler;
import tamaized.voidscape.network.NetworkMessages;

public class ServerPacketHandlerDonatorSettings implements NetworkMessages.IMessage<ServerPacketHandlerDonatorSettings> {

	private DonatorHandler.DonatorSettings settings;

	public ServerPacketHandlerDonatorSettings(DonatorHandler.DonatorSettings settings) {
		this.settings = settings;
	}

	@Override
	public void handle(Player player) {
		if (DonatorHandler.donators.contains(player.getUUID()))
			DonatorHandler.settings.put(player.getUUID(), settings);
	}

	@Override
	public void toBytes(FriendlyByteBuf packet) {
		packet.writeBoolean(settings.enabled);
		packet.writeInt(settings.color);
	}

	@Override
	public ServerPacketHandlerDonatorSettings fromBytes(FriendlyByteBuf packet) {
		settings = new DonatorHandler.DonatorSettings(packet.readBoolean(), packet.readInt());
		return this;
	}
}