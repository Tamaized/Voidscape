package tamaized.voidscape.network.server;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.network.NetworkMessages;
import tamaized.voidscape.turmoil.SubCapability;

import java.util.function.Supplier;

public class ServerPacketTurmoilAction implements NetworkMessages.IMessage<ServerPacketTurmoilAction> {

	@Override
	public void handle(Supplier<Supplier<PlayerEntity>> sup) {
		PlayerEntity player = sup.get().get();
		player.getCapability(SubCapability.CAPABILITY).ifPresent(cap -> cap.get(Voidscape.subCapTurmoilData).ifPresent(data -> data.doAction(player)));
	}

	@Override
	public void toBytes(PacketBuffer packet) {

	}

	@Override
	public ServerPacketTurmoilAction fromBytes(PacketBuffer packet) {
		return this;
	}

}
