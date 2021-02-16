package tamaized.voidscape.network.server;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import tamaized.voidscape.network.NetworkMessages;
import tamaized.voidscape.party.PartyManager;

import javax.annotation.Nullable;

public class ServerPacketSetPartyPassword implements NetworkMessages.IMessage<ServerPacketSetPartyPassword> {

	private String password;

	public ServerPacketSetPartyPassword(String password) {
		this.password = password;
	}

	@Override
	public void handle(@Nullable PlayerEntity player) {
		if (player instanceof ServerPlayerEntity)
			PartyManager.findParty((ServerPlayerEntity) player).ifPresent(party -> {
				if (party.host() == player)
					party.setPassword(password);
			});
	}

	@Override
	public void toBytes(PacketBuffer packet) {
		packet.writeUtf(password);
	}

	@Override
	public ServerPacketSetPartyPassword fromBytes(PacketBuffer packet) {
		password = packet.readUtf(Short.MAX_VALUE);
		return this;
	}

}
