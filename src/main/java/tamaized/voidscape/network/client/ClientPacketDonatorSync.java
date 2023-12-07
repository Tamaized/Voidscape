package tamaized.voidscape.network.client;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.data.DonatorData;
import tamaized.voidscape.data.Insanity;
import tamaized.voidscape.network.NetworkMessages;
import tamaized.voidscape.registry.ModDataAttachments;

import javax.annotation.Nullable;

public class ClientPacketDonatorSync implements NetworkMessages.IMessage<ClientPacketDonatorSync> {

	private DonatorData handler;
	private int entity;
	private FriendlyByteBuf data;

	public ClientPacketDonatorSync(DonatorData handler, Entity entity) {
		this(handler);
		this.entity = entity.getId();
	}

	public ClientPacketDonatorSync(DonatorData handler) {
		this.handler = handler;
	}

	public ClientPacketDonatorSync() {

	}

	@Override
	public void handle(@Nullable Player player) {
		if (player == null || !player.level().isClientSide()) {
			Voidscape.LOGGER.fatal("Warning, client attempted to send malicious packet! ({})", player == null ? "NULL PLAYER" : player.getDisplayName());
			return;
		}
		Entity entity = this.entity > 0 ? player.level().getEntity(this.entity) : player;
		if (entity == null)
			return;
		entity.getData(ModDataAttachments.DONATOR).read(data);
	}

	@Override
	public void toBytes(FriendlyByteBuf packet) {
		packet.writeInt(entity);
		handler.write(packet);
	}

	@Override
	public ClientPacketDonatorSync fromBytes(FriendlyByteBuf packet) {
		entity = packet.readInt();
		data = packet;
		return this;
	}

}
