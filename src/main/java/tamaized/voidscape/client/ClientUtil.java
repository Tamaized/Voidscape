package tamaized.voidscape.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class ClientUtil {

	public static long tick;

	@Nullable
	public static PlayerEntity getClientPlayerSafely() {
		return Minecraft.getInstance().player;
	}

	public static ResourceLocation getMissingTexture() {
		return TextureManager.INTENTIONAL_MISSING_TEXTURE;
	}

}
