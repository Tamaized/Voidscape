package tamaized.voidscape.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;

public class ClientUtil {

	public static long tick;

	@Nullable
	public static Player getClientPlayerSafely() {
		return Minecraft.getInstance().player;
	}

	public static ResourceLocation getMissingTexture() {
		return TextureManager.INTENTIONAL_MISSING_TEXTURE;
	}

	public static void bindTexture(ResourceLocation texture) {
		RenderSystem.setShaderTexture(0, texture);
	}

}
