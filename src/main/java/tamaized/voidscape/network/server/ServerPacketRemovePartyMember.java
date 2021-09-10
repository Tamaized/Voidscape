package tamaized.voidscape.network.server;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import tamaized.voidscape.network.NetworkMessages;
import tamaized.voidscape.party.PartyManager;

import javax.annotation.Nullable;

public class ServerPacketRemovePartyMember implements NetworkMessages.IMessage<ServerPacketRemovePartyMember> {

	private int index;

	public ServerPacketRemovePartyMember(int index) {
		this.index = index;
	}

	@Override
	public void handle(@Nullable Player player) {
		if (player instanceof ServerPlayer)
			PartyManager.findParty((ServerPlayer) player).ifPresent(party -> {
				if (party.host() == player && party.size() > index - 1)
					party.removeMember(party.members().get(index + 1));
			});
	}

	@Override
	public void toBytes(FriendlyByteBuf packet) {
		packet.writeVarInt(index);
	}

	@Override
	public ServerPacketRemovePartyMember fromBytes(FriendlyByteBuf packet) {
		index = packet.readVarInt();
		return this;
	}

}
