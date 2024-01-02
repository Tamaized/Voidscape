package tamaized.voidscape.network.client;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import tamaized.voidscape.Voidscape;

public record ClientPacketNoFlashOnSetHealth() implements CustomPacketPayload {

	public static final ResourceLocation ID = new ResourceLocation(Voidscape.MODID, "noflashsethealth");

	public ClientPacketNoFlashOnSetHealth(FriendlyByteBuf packet) {
		this();
	}

	@Override
	public void write(FriendlyByteBuf pBuffer) {

	}

	@Override
	public ResourceLocation id() {
		return ID;
	}

	public static void handle(ClientPacketNoFlashOnSetHealth payload, PlayPayloadContext context) {
		context.player().ifPresent(player -> {
			if (player instanceof LocalPlayer local)
				local.flashOnSetHealth = false;
		});
	}
}
