package tamaized.voidscape.network.server;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import tamaized.voidscape.network.NetworkMessages;
import tamaized.voidscape.party.PartyManager;

import javax.annotation.Nullable;

public class ServerPacketRemovePartyMember implements NetworkMessages.IMessage<ServerPacketRemovePartyMember> {

	private int index;

	public ServerPacketRemovePartyMember(int index) {
		this.index = index;
	}

	@Override
	public void handle(@Nullable PlayerEntity player) {
		if (player instanceof ServerPlayerEntity)
			PartyManager.findParty((ServerPlayerEntity) player).ifPresent(party -> {
				if (party.host() == player && party.size() > index - 1)
					party.removeMember(party.members().get(index + 1));
			});
	}

	@Override
	public void toBytes(PacketBuffer packet) {
		packet.writeVarInt(index);
	}

	@Override
	public ServerPacketRemovePartyMember fromBytes(PacketBuffer packet) {
		index = packet.readVarInt();
		return this;
	}

}
