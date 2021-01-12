package tamaized.voidscape.network.server;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import tamaized.voidscape.network.NetworkMessages;
import tamaized.voidscape.party.PartyManager;

import javax.annotation.Nullable;

public class ServerPacketDisbandParty implements NetworkMessages.IMessage<ServerPacketDisbandParty> {

	@Override
	public void handle(@Nullable PlayerEntity player) {
		PartyManager.findParty((ServerPlayerEntity) player).ifPresent(party -> {
			if (party.host() == player)
				PartyManager.disbandParty(party);
			else
				party.removeMember((ServerPlayerEntity) player);
		});
	}

	@Override
	public void toBytes(PacketBuffer packet) {
	}

	@Override
	public ServerPacketDisbandParty fromBytes(PacketBuffer packet) {
		return this;
	}

}
