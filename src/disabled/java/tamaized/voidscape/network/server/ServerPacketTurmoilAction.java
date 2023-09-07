package tamaized.voidscape.network.server;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.network.NetworkMessages;
import tamaized.voidscape.turmoil.SubCapability;

import javax.annotation.Nullable;

public class ServerPacketTurmoilAction implements NetworkMessages.IMessage<ServerPacketTurmoilAction> {

	@Override
	public void handle(@Nullable Player player) {
		if (player != null)
			player.getCapability(SubCapability.CAPABILITY).ifPresent(cap -> cap.get(Voidscape.subCapTurmoilData).ifPresent(data -> data.serverAction(player)));
	}

	@Override
	public void toBytes(FriendlyByteBuf packet) {

	}

	@Override
	public ServerPacketTurmoilAction fromBytes(FriendlyByteBuf packet) {
		return this;
	}

}
