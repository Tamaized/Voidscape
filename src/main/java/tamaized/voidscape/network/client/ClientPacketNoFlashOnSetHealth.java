package tamaized.voidscape.network.client;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.network.NetworkMessages;

import javax.annotation.Nullable;

public class ClientPacketNoFlashOnSetHealth implements NetworkMessages.IMessage<ClientPacketNoFlashOnSetHealth> {

	public ClientPacketNoFlashOnSetHealth() {

	}

	@Override
	public void handle(@Nullable Player player) {
		if (player instanceof LocalPlayer local)
			local.flashOnSetHealth = false;
	}

	@Override
	public void toBytes(FriendlyByteBuf packet) {

	}

	@Override
	public ClientPacketNoFlashOnSetHealth fromBytes(FriendlyByteBuf packet) {
		return this;
	}

}
