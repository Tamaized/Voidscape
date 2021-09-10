package tamaized.voidscape.network.server;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import tamaized.voidscape.network.NetworkMessages;
import tamaized.voidscape.party.PartyManager;

import javax.annotation.Nullable;

public class ServerPacketSetPartyPassword implements NetworkMessages.IMessage<ServerPacketSetPartyPassword> {

	private String password;

	public ServerPacketSetPartyPassword(String password) {
		this.password = password;
	}

	@Override
	public void handle(@Nullable Player player) {
		if (player instanceof ServerPlayer)
			PartyManager.findParty((ServerPlayer) player).ifPresent(party -> {
				if (party.host() == player)
					party.setPassword(password);
			});
	}

	@Override
	public void toBytes(FriendlyByteBuf packet) {
		packet.writeUtf(password);
	}

	@Override
	public ServerPacketSetPartyPassword fromBytes(FriendlyByteBuf packet) {
		password = packet.readUtf(Short.MAX_VALUE);
		return this;
	}

}
