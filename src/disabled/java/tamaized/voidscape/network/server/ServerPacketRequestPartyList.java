package tamaized.voidscape.network.server;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.PacketDistributor;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.network.NetworkMessages;
import tamaized.voidscape.network.client.ClientPacketSendPartyList;

import javax.annotation.Nullable;

public class ServerPacketRequestPartyList implements NetworkMessages.IMessage<ServerPacketRequestPartyList> {

	@Override
	public void handle(@Nullable Player player) {
		if (player instanceof ServerPlayer)
			Voidscape.NETWORK.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), new ClientPacketSendPartyList());
	}

	@Override
	public void toBytes(FriendlyByteBuf packet) {
	}

	@Override
	public ServerPacketRequestPartyList fromBytes(FriendlyByteBuf packet) {
		return this;
	}

}
