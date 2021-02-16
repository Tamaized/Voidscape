package tamaized.voidscape.network.client;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.TranslationTextComponent;
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
	public void handle(@Nullable PlayerEntity player) {
		if (player == null || player.level == null || !player.level.isClientSide()) {
			Voidscape.LOGGER.fatal("Warning, client attempted to send malicious packet! ({})", player == null ? "NULL PLAYER" : player.getDisplayName());
			return;
		}
		ClientPartyInfo.error = new TranslationTextComponent(error);
		if (Minecraft.getInstance().screen instanceof PartyListScreen)
			((PartyListScreen) Minecraft.getInstance().screen).joining = false;
	}

	@Override
	public void toBytes(PacketBuffer packet) {
		packet.writeUtf(error);
	}

	@Override
	public ClientPacketJoinPartyError fromBytes(PacketBuffer packet) {
		error = packet.readUtf(Short.MAX_VALUE);
		return this;
	}

}
