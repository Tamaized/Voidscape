package tamaized.voidscape.network.server;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.network.NetworkMessages;
import tamaized.voidscape.turmoil.SubCapability;

import javax.annotation.Nullable;

public class ServerPacketTurmoilAction implements NetworkMessages.IMessage<ServerPacketTurmoilAction> {

	@Override
	public void handle(@Nullable PlayerEntity player) {
		if (player != null)
			player.getCapability(SubCapability.CAPABILITY).ifPresent(cap -> cap.get(Voidscape.subCapTurmoilData).ifPresent(data -> data.serverAction(player)));
	}

	@Override
	public void toBytes(PacketBuffer packet) {

	}

	@Override
	public ServerPacketTurmoilAction fromBytes(PacketBuffer packet) {
		return this;
	}

}
