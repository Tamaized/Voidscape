package tamaized.voidscape.network.server;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import tamaized.beanification.Autowired;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.network.DonatorHandler;

public record ServerPacketDonatorSettings(DonatorHandler.Settings settings) implements CustomPacketPayload {

	public static final Type<ServerPacketDonatorSettings> ID = new Type<>(Identifier.fromNamespaceAndPath(Voidscape.MODID, "c2s_donator_settings"));

	public static final StreamCodec<FriendlyByteBuf, ServerPacketDonatorSettings> CODEC = StreamCodec.ofMember(ServerPacketDonatorSettings::write, ServerPacketDonatorSettings::new);

	@Autowired
	private static DonatorHandler donatorHandler;

	private ServerPacketDonatorSettings(FriendlyByteBuf packet) {
		this(new DonatorHandler.Settings(packet.readBoolean(), packet.readInt()));
	}

	public void write(FriendlyByteBuf packet) {
		packet.writeBoolean(settings().enabled());
		packet.writeInt(settings().color());
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}

	public static void handle(ServerPacketDonatorSettings packet, IPayloadContext context) {
		context.enqueueWork(() -> donatorHandler.updateSettings(context.player().getUUID(), packet.settings()));
	}
}