package tamaized.voidscape.network.client;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import tamaized.voidscape.data.Insanity;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.network.NetworkMessages;
import tamaized.voidscape.registry.ModDataAttachments;

import javax.annotation.Nullable;

public class ClientPacketInsanitySync implements NetworkMessages.IMessage<ClientPacketInsanitySync> {

	private Insanity handler;
	private int entity;
	private FriendlyByteBuf data;

	public ClientPacketInsanitySync(Insanity handler, Entity entity) {
		this(handler);
		this.entity = entity.getId();
	}

	public ClientPacketInsanitySync(Insanity handler) {
		this.handler = handler;
	}

	public ClientPacketInsanitySync() {

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
		entity.getData(ModDataAttachments.INSANITY).read(data);
	}

	@Override
	public void toBytes(FriendlyByteBuf packet) {
		packet.writeInt(entity);
		handler.write(packet);
	}

	@Override
	public ClientPacketInsanitySync fromBytes(FriendlyByteBuf packet) {
		entity = packet.readInt();
		data = packet;
		return this;
	}

}
