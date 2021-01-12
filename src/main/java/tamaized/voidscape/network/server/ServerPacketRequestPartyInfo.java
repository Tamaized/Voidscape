package tamaized.voidscape.network.server;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.PacketDistributor;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.network.NetworkMessages;
import tamaized.voidscape.network.client.ClientPacketUpdatePartyInfo;
import tamaized.voidscape.party.Party;
import tamaized.voidscape.party.PartyManager;

import javax.annotation.Nullable;
import java.util.Optional;

public class ServerPacketRequestPartyInfo implements NetworkMessages.IMessage<ServerPacketRequestPartyInfo> {

	@Override
	public void handle(@Nullable PlayerEntity player) {
		if (player instanceof ServerPlayerEntity) {
			Optional<Party> o = PartyManager.findParty((ServerPlayerEntity) player);
			o.ifPresent(party -> Voidscape.NETWORK.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player), new ClientPacketUpdatePartyInfo(party, party.host() == player)));
			if (!o.isPresent())
				PartyManager.resetClientInfo((ServerPlayerEntity) player);
		}
	}

	@Override
	public void toBytes(PacketBuffer packet) {
	}

	@Override
	public ServerPacketRequestPartyInfo fromBytes(PacketBuffer packet) {
		return this;
	}

}
