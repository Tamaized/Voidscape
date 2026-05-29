package tamaized.voidscape.network.client;

import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import tamaized.beanification.Autowired;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.data.DonatorData;
import tamaized.voidscape.network.DetatchedFriendlyByteBuf;
import tamaized.voidscape.registry.ModDataAttachments;

import javax.annotation.Nullable;

public record ClientPacketDonatorSync(@Nullable DonatorData handler, int entity, @Nullable DetatchedFriendlyByteBuf data) implements CustomPacketPayload {

	public static final Type<ClientPacketDonatorSync> ID = new Type<>(Identifier.fromNamespaceAndPath(Voidscape.MODID, "s2c_donator_sync"));

	public static final StreamCodec<FriendlyByteBuf, ClientPacketDonatorSync> CODEC = StreamCodec.ofMember(ClientPacketDonatorSync::write, ClientPacketDonatorSync::new);

	@Autowired
	private static ModDataAttachments dataAttachments;

	public ClientPacketDonatorSync(DonatorData handler, Entity entity) {
		this(handler, entity.getId(), null);
	}

	public ClientPacketDonatorSync(DonatorData handler) {
		this(handler, -1, null);
	}

	private ClientPacketDonatorSync(FriendlyByteBuf packet) {
		this(null, packet.readInt(), new DetatchedFriendlyByteBuf(packet));
	}

	public void write(FriendlyByteBuf packet) {
		if (handler == null)
			throw new IllegalStateException("ClientPacketDonatorSync: Null Handler for entity id " + entity);
		packet.writeInt(entity);
		handler.write(packet);
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}

	public static void handle(ClientPacketDonatorSync payload, IPayloadContext context) {
		context.enqueueWork(() -> {
			Entity entity = payload.entity > 0 ? context.player().level().getEntity(payload.entity) : context.player();
			if (entity == null)
				return;
			FriendlyByteBuf data = payload.data;
			if (data == null && payload.handler != null) { // Assume Singleplayer
				data = new FriendlyByteBuf(Unpooled.buffer());
				payload.handler.write(data);
			}
			if (data != null)
				entity.getData(dataAttachments.DONATOR).read(data);
		});
	}

}
