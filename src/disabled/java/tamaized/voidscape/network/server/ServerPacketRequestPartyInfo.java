package tamaized.voidscape.network.server;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.PacketDistributor;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.network.NetworkMessages;
import tamaized.voidscape.network.client.ClientPacketUpdatePartyInfo;
import tamaized.voidscape.party.Party;
import tamaized.voidscape.party.PartyManager;

import javax.annotation.Nullable;
import java.util.Optional;

public class ServerPacketRequestPartyInfo implements NetworkMessages.IMessage<ServerPacketRequestPartyInfo> {

	@Override
	public void handle(@Nullable Player player) {
		if (player instanceof ServerPlayer) {
			Optional<Party> o = PartyManager.findParty((ServerPlayer) player);
			o.ifPresent(party -> Voidscape.NETWORK.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), new ClientPacketUpdatePartyInfo(party, party.host() == player)));
			if (!o.isPresent())
				PartyManager.resetClientInfo((ServerPlayer) player);
		}
	}

	@Override
	public void toBytes(FriendlyByteBuf packet) {
	}

	@Override
	public ServerPacketRequestPartyInfo fromBytes(FriendlyByteBuf packet) {
		return this;
	}

}
