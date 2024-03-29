package tamaized.voidscape.network.client;

import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.data.DonatorData;
import tamaized.voidscape.registry.ModDataAttachments;

import javax.annotation.Nullable;

public record ClientPacketDonatorSync(@Nullable DonatorData handler, int entity, @Nullable FriendlyByteBuf data) implements CustomPacketPayload {

	public static final ResourceLocation ID = new ResourceLocation(Voidscape.MODID, "s2c_donator_sync");

	public ClientPacketDonatorSync(DonatorData handler, Entity entity) {
		this(handler, entity.getId(), null);
	}

	public ClientPacketDonatorSync(DonatorData handler) {
		this(handler, -1, null);
	}

	public ClientPacketDonatorSync(FriendlyByteBuf packet) {
		this(null, packet.readInt(), packet);
	}

	@Override
	public void write(FriendlyByteBuf packet) {
		if (handler == null)
			throw new IllegalStateException("ClientPacketDonatorSync: Null Handler for entity id " + entity);
		packet.writeInt(entity);
		handler.write(packet);
	}

	@Override
	public ResourceLocation id() {
		return ID;
	}

	public static void handle(ClientPacketDonatorSync payload, PlayPayloadContext context) {
		context.player().ifPresent(player -> {
			Entity entity = payload.entity > 0 ? player.level().getEntity(payload.entity) : player;
			if (entity == null)
				return;
			FriendlyByteBuf data = payload.data;
			if (data == null && payload.handler != null) { // Assume Singleplayer
				data = new FriendlyByteBuf(Unpooled.buffer());
				payload.handler.write(data);
			}
			if (data != null)
				entity.getData(ModDataAttachments.DONATOR).read(data);
		});
	}

}
