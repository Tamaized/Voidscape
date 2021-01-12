package tamaized.voidscape.network.server;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import tamaized.voidscape.network.NetworkMessages;
import tamaized.voidscape.party.PartyManager;

import javax.annotation.Nullable;

public class ServerPacketCommenceDuty implements NetworkMessages.IMessage<ServerPacketCommenceDuty> {

	@Override
	public void handle(@Nullable PlayerEntity player) {
		if (player instanceof ServerPlayerEntity && player.getServer() != null) {
			PartyManager.findParty((ServerPlayerEntity) player).ifPresent(party -> {
				if (party.host() == player)
					party.commence();
			});
		}
	}

	@Override
	public void toBytes(PacketBuffer packet) {
	}

	@Override
	public ServerPacketCommenceDuty fromBytes(PacketBuffer packet) {
		return this;
	}

}
