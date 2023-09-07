package tamaized.voidscape.network.client;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.client.ui.screen.PartyListScreen;
import tamaized.voidscape.network.NetworkMessages;
import tamaized.voidscape.party.ClientPartyInfo;

import javax.annotation.Nullable;

public class ClientPacketJoinPartyError implements NetworkMessages.IMessage<ClientPacketJoinPartyError> {

	private String error;

	public ClientPacketJoinPartyError(String error) {
		this.error = error;
	}

	@Override
	public void handle(@Nullable Player player) {
		if (player == null || player.level == null || !player.level.isClientSide()) {
			Voidscape.LOGGER.fatal("Warning, client attempted to send malicious packet! ({})", player == null ? "NULL PLAYER" : player.getDisplayName());
			return;
		}
		ClientPartyInfo.error = Component.translatable(error);
		if (Minecraft.getInstance().screen instanceof PartyListScreen)
			((PartyListScreen) Minecraft.getInstance().screen).joining = false;
	}

	@Override
	public void toBytes(FriendlyByteBuf packet) {
		packet.writeUtf(error);
	}

	@Override
	public ClientPacketJoinPartyError fromBytes(FriendlyByteBuf packet) {
		error = packet.readUtf(Short.MAX_VALUE);
		return this;
	}

}
