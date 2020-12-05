package tamaized.voidscape.network.server;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.network.NetworkMessages;
import tamaized.voidscape.turmoil.SubCapability;
import tamaized.voidscape.turmoil.Turmoil;

import javax.annotation.Nullable;

public class ServerPacketTurmoilTeleport implements NetworkMessages.IMessage<ServerPacketTurmoilTeleport> {

	@Override
	public void handle(@Nullable PlayerEntity player) {
		if (player != null)
			player.getCapability(SubCapability.CAPABILITY).ifPresent(cap -> cap.get(Voidscape.subCapTurmoilData).ifPresent(data -> {
				if (data.hasStarted() && data.getState() == Turmoil.State.OPEN)
					data.setState(Turmoil.State.TELEPORTING);
			}));
	}

	@Override
	public void toBytes(PacketBuffer packet) {

	}

	@Override
	public ServerPacketTurmoilTeleport fromBytes(PacketBuffer packet) {
		return this;
	}

}
