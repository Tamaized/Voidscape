package tamaized.voidscape.network.server;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.network.NetworkMessages;
import tamaized.voidscape.turmoil.SubCapability;

import javax.annotation.Nullable;

public class ServerPacketTurmoilActivateAbility implements NetworkMessages.IMessage<ServerPacketTurmoilActivateAbility> {

	private int id;

	public ServerPacketTurmoilActivateAbility(int id) {
		this.id = id;
	}

	@Override
	public void handle(@Nullable PlayerEntity player) {
		if (player != null)
			player.getCapability(SubCapability.CAPABILITY).ifPresent(cap -> cap.get(Voidscape.subCapTurmoilStats).ifPresent(stats -> stats.executeAbility(player, id)));
	}

	@Override
	public void toBytes(PacketBuffer packet) {
		packet.writeVarInt(id);
	}

	@Override
	public ServerPacketTurmoilActivateAbility fromBytes(PacketBuffer packet) {
		id = packet.readVarInt();
		return this;
	}

}
