package tamaized.voidscape.network.client;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import tamaized.voidscape.Voidscape;

public record ClientPacketNoFlashOnSetHealth() implements CustomPacketPayload {

	public static final Type<ClientPacketNoFlashOnSetHealth> ID = new Type<>(ResourceLocation.fromNamespaceAndPath(Voidscape.MODID, "s2c_no_flash_set_health"));

	public static final StreamCodec<FriendlyByteBuf, ClientPacketNoFlashOnSetHealth> CODEC = StreamCodec.ofMember(ClientPacketNoFlashOnSetHealth::write, ClientPacketNoFlashOnSetHealth::new);

	private ClientPacketNoFlashOnSetHealth(FriendlyByteBuf packet) {
		this();
	}

	public void write(FriendlyByteBuf pBuffer) {

	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}

	public static void handle(ClientPacketNoFlashOnSetHealth packet, IPayloadContext context) {
		context.enqueueWork(() -> {
			if (context.player() instanceof LocalPlayer local)
				local.flashOnSetHealth = false;
		});
	}
}
