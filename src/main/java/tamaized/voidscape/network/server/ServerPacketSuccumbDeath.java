package tamaized.voidscape.network.server;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.network.NetworkMessages;
import tamaized.voidscape.turmoil.SubCapability;
import tamaized.voidscape.world.VoidTeleporter;

import javax.annotation.Nullable;

public class ServerPacketSuccumbDeath implements NetworkMessages.IMessage<ServerPacketSuccumbDeath> {

	@Override
	public void handle(@Nullable PlayerEntity player) {
		if (player instanceof ServerPlayerEntity && player.getServer() != null)
			player.getCapability(SubCapability.CAPABILITY).ifPresent(cap -> cap.get(Voidscape.subCapTurmoilTracked).ifPresent(data -> {
				if (data.incapacitated) {
					player.setHealth(player.getMaxHealth() * 0.1F);
					player.changeDimension(Voidscape.getWorld(player.level, World.OVERWORLD), VoidTeleporter.INSTANCE);
					data.incapacitated = false;
				}
			}));
	}

	@Override
	public void toBytes(PacketBuffer packet) {
	}

	@Override
	public ServerPacketSuccumbDeath fromBytes(PacketBuffer packet) {
		return this;
	}

}
