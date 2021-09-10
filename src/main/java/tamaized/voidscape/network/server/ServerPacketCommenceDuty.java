package tamaized.voidscape.network.server;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import tamaized.voidscape.network.NetworkMessages;
import tamaized.voidscape.party.PartyManager;

import javax.annotation.Nullable;

public class ServerPacketCommenceDuty implements NetworkMessages.IMessage<ServerPacketCommenceDuty> {

	@Override
	public void handle(@Nullable Player player) {
		if (player instanceof ServerPlayer && player.getServer() != null) {
			PartyManager.findParty((ServerPlayer) player).ifPresent(party -> {
				if (party.host() == player)
					party.commence();
			});
		}
	}

	@Override
	public void toBytes(FriendlyByteBuf packet) {
	}

	@Override
	public ServerPacketCommenceDuty fromBytes(FriendlyByteBuf packet) {
		return this;
	}

}
