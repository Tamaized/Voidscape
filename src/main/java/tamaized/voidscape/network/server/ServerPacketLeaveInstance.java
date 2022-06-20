package tamaized.voidscape.network.server;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.network.NetworkMessages;
import tamaized.voidscape.world.VoidTeleporter;

import javax.annotation.Nullable;

public class ServerPacketLeaveInstance implements NetworkMessages.IMessage<ServerPacketLeaveInstance> {

	@Override
	public void handle(@Nullable Player player) {
		if (player instanceof ServerPlayer serverPlayer && Voidscape.checkForDutyInstance(serverPlayer.getLevel())) {
			serverPlayer.setHealth(0.5F);
			serverPlayer.curePotionEffects(new ItemStack(Items.MILK_BUCKET));
			serverPlayer.changeDimension(Voidscape.getPlayersSpawnLevel(serverPlayer), VoidTeleporter.INSTANCE);
		}
	}

	@Override
	public void toBytes(FriendlyByteBuf packet) {
	}

	@Override
	public ServerPacketLeaveInstance fromBytes(FriendlyByteBuf packet) {
		return this;
	}

}
