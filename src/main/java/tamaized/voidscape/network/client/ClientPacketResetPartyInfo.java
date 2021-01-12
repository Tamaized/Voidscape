package tamaized.voidscape.network.client;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
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
	public void handle(@Nullable PlayerEntity player) {
		if (player == null || player.level == null || !player.level.isClientSide()) {
			Voidscape.LOGGER.fatal("Warning, client attempted to send malicious packet! ({})", player == null ? "NULL PLAYER" : player.getDisplayName());
			return;
		}
		ClientPartyInfo.reset(toast);
	}

	@Override
	public void toBytes(PacketBuffer packet) {
		packet.writeBoolean(toast);
	}

	@Override
	public ClientPacketResetPartyInfo fromBytes(PacketBuffer packet) {
		toast = packet.readBoolean();
		return this;
	}

}
