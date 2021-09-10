package tamaized.voidscape.network.server;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import tamaized.voidscape.network.NetworkMessages;
import tamaized.voidscape.party.PartyManager;

import javax.annotation.Nullable;

public class ServerPacketDisbandParty implements NetworkMessages.IMessage<ServerPacketDisbandParty> {

	@Override
	public void handle(@Nullable Player player) {
		PartyManager.findParty((ServerPlayer) player).ifPresent(party -> {
			if (party.host() == player)
				PartyManager.disbandParty(party);
			else
				party.removeMember((ServerPlayer) player);
		});
	}

	@Override
	public void toBytes(FriendlyByteBuf packet) {
	}

	@Override
	public ServerPacketDisbandParty fromBytes(FriendlyByteBuf packet) {
		return this;
	}

}
