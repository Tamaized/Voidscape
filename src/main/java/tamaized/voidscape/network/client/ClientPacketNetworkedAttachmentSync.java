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
import tamaized.voidscape.data.NetworkedDataAttachment;
import tamaized.voidscape.network.DetatchedFriendlyByteBuf;
import tamaized.voidscape.registry.ModDataAttachments;

import javax.annotation.Nullable;

public record ClientPacketNetworkedAttachmentSync(@Nullable NetworkedDataAttachment handler, int entity, @Nullable DetatchedFriendlyByteBuf data) implements CustomPacketPayload {

	public static final Type<ClientPacketNetworkedAttachmentSync> ID = new Type<>(Identifier.fromNamespaceAndPath(Voidscape.MODID, "s2c_networked_attachment_sync"));

	public static final StreamCodec<FriendlyByteBuf, ClientPacketNetworkedAttachmentSync> CODEC = StreamCodec.ofMember(
		ClientPacketNetworkedAttachmentSync::write,
		ClientPacketNetworkedAttachmentSync::new
	);

	@Autowired
	private static ModDataAttachments dataAttachments;

	public ClientPacketNetworkedAttachmentSync(NetworkedDataAttachment handler, Entity entity) {
		this(handler, entity.getId(), null);
	}

	public ClientPacketNetworkedAttachmentSync(NetworkedDataAttachment handler) {
		this(handler, -1, null);
	}

	private ClientPacketNetworkedAttachmentSync(FriendlyByteBuf packet) {
		this(null, packet.readInt(), new DetatchedFriendlyByteBuf(packet));
	}

	public void write(FriendlyByteBuf packet) {
		if (handler == null)
			throw new IllegalStateException("ClientPacketNetworkedAttachmentSync: Null handler for entity id " + entity);
		packet.writeInt(entity);
		packet.writeUtf(handler.id());
		handler.write(packet);
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}

	public static void handle(ClientPacketNetworkedAttachmentSync packet, IPayloadContext context) {
		context.enqueueWork(() -> {
			Entity entity = packet.entity > 0 ? context.player().level().getEntity(packet.entity) : context.player();
			if (entity == null)
				return;
			FriendlyByteBuf data = packet.data;
			if (data == null && packet.handler != null) { // Assume Singleplayer
				data = new FriendlyByteBuf(Unpooled.buffer());
				data.writeUtf(packet.handler.id());
				packet.handler.write(data);
			}
			if (data != null) {
				String id = data.readUtf();
				var type = dataAttachments.getNetworkedAttachment(id);
				if (type == null)
					throw new IllegalStateException("ClientPacketNetworkedAttachmentSync: Unknown type: " + id);
				entity.getData(type.get()).read(data);
			}
		});
	}

}
