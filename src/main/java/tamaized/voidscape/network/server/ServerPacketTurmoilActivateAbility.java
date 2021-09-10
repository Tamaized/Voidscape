package tamaized.voidscape.network.server;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
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
	public void handle(@Nullable Player player) {
		if (player != null)
			player.getCapability(SubCapability.CAPABILITY).ifPresent(cap -> cap.get(Voidscape.subCapTurmoilStats).ifPresent(stats -> stats.executeAbility(player, id)));
	}

	@Override
	public void toBytes(FriendlyByteBuf packet) {
		packet.writeVarInt(id);
	}

	@Override
	public ServerPacketTurmoilActivateAbility fromBytes(FriendlyByteBuf packet) {
		id = packet.readVarInt();
		return this;
	}

}
