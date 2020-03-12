package tamaized.voidscape.network.server;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.network.NetworkMessages;
import tamaized.voidscape.world.VoidTeleporter;

public class ServerPacketTurmoilTeleport implements NetworkMessages.IMessage<ServerPacketTurmoilTeleport> {

	@Override
	public void handle(PlayerEntity player) {
		if (player.dimension.getId() == Voidscape.getDimensionTypeID())
			return;
		player.changeDimension(Voidscape.getDimensionType(), VoidTeleporter.INSTANCE);
	}

	@Override
	public void toBytes(PacketBuffer packet) {

	}

	@Override
	public ServerPacketTurmoilTeleport fromBytes(PacketBuffer packet) {
		return this;
	}

}
