package tamaized.voidscape.network.client;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.network.NetworkMessages;
import tamaized.voidscape.party.ClientPartyInfo;

import javax.annotation.Nullable;

public class ClientPacketResetPartyInfo implements NetworkMessages.IMessage<ClientPacketResetPartyInfo> {

	private boolean toast;

	public ClientPacketResetPartyInfo(boolean displayToast) {
		toast = displayToast;
	}

	@Override
	public void handle(@Nullable Player player) {
		if (player == null || player.level == null || !player.level.isClientSide()) {
			Voidscape.LOGGER.fatal("Warning, client attempted to send malicious packet! ({})", player == null ? "NULL PLAYER" : player.getDisplayName());
			return;
		}
		ClientPartyInfo.reset(toast);
	}

	@Override
	public void toBytes(FriendlyByteBuf packet) {
		packet.writeBoolean(toast);
	}

	@Override
	public ClientPacketResetPartyInfo fromBytes(FriendlyByteBuf packet) {
		toast = packet.readBoolean();
		return this;
	}

}
