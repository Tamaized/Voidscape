package tamaized.voidscape.network.client;

import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import tamaized.beanification.Autowired;
import tamaized.voidscape.data.Insanity;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.network.DetatchedFriendlyByteBuf;
import tamaized.voidscape.registry.ModDataAttachments;

import javax.annotation.Nullable;

public record ClientPacketInsanitySync(@Nullable Insanity handler, int entity, @Nullable DetatchedFriendlyByteBuf data) implements CustomPacketPayload {

	public static final Type<ClientPacketInsanitySync> ID = new Type<>(ResourceLocation.fromNamespaceAndPath(Voidscape.MODID, "s2c_insanity_sync"));

	public static final StreamCodec<FriendlyByteBuf, ClientPacketInsanitySync> CODEC = StreamCodec.ofMember(ClientPacketInsanitySync::write, ClientPacketInsanitySync::new);

	@Autowired
	private static ModDataAttachments dataAttachments;

	public ClientPacketInsanitySync(Insanity handler, Entity entity) {
		this(handler, entity.getId(), null);
	}

	public ClientPacketInsanitySync(Insanity handler) {
		this(handler, -1, null);
	}

	private ClientPacketInsanitySync(FriendlyByteBuf packet) {
		this(null, packet.readInt(), new DetatchedFriendlyByteBuf(packet));
	}

	public void write(FriendlyByteBuf packet) {
		if (handler == null)
			throw new IllegalStateException("ClientPacketInsanitySync: Null Handler for entity id " + entity);
		packet.writeInt(entity);
		handler.write(packet);
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}

	public static void handle(ClientPacketInsanitySync packet, IPayloadContext context) {
		context.enqueueWork(() -> {
			Entity entity = packet.entity > 0 ? context.player().level().getEntity(packet.entity) : context.player();
			if (entity == null)
				return;
			FriendlyByteBuf data = packet.data;
			if (data == null && packet.handler != null) { // Assume Singleplayer
				data = new FriendlyByteBuf(Unpooled.buffer());
				packet.handler.write(data);
			}
			if (data != null)
				entity.getData(dataAttachments.INSANITY).read(data);
		});
	}

}
