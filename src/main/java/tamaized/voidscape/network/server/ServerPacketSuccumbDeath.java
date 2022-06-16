package tamaized.voidscape.network.server;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.network.NetworkMessages;
import tamaized.voidscape.turmoil.SubCapability;
import tamaized.voidscape.world.VoidTeleporter;

import javax.annotation.Nullable;

public class ServerPacketSuccumbDeath implements NetworkMessages.IMessage<ServerPacketSuccumbDeath> {

	@Override
	public void handle(@Nullable Player player) {
		if (player instanceof ServerPlayer serverPlayer && player.getServer() != null)
			player.getCapability(SubCapability.CAPABILITY).ifPresent(cap -> cap.get(Voidscape.subCapTurmoilTracked).ifPresent(data -> {
				if (data.incapacitated) {
					player.setHealth(player.getMaxHealth() * 0.1F);
					player.changeDimension(Voidscape.getPlayersSpawnWorld(serverPlayer), VoidTeleporter.INSTANCE);
					data.incapacitated = false;
				}
			}));
	}

	@Override
	public void toBytes(FriendlyByteBuf packet) {
	}

	@Override
	public ServerPacketSuccumbDeath fromBytes(FriendlyByteBuf packet) {
		return this;
	}

}
