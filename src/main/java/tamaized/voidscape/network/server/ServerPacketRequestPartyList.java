package tamaized.voidscape.network.server;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.PacketDistributor;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.network.NetworkMessages;
import tamaized.voidscape.network.client.ClientPacketSendPartyList;

import javax.annotation.Nullable;

public class ServerPacketRequestPartyList implements NetworkMessages.IMessage<ServerPacketRequestPartyList> {

	@Override
	public void handle(@Nullable PlayerEntity player) {
		if (player instanceof ServerPlayerEntity)
			Voidscape.NETWORK.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player), new ClientPacketSendPartyList());
	}

	@Override
	public void toBytes(PacketBuffer packet) {
	}

	@Override
	public ServerPacketRequestPartyList fromBytes(PacketBuffer packet) {
		return this;
	}

}
