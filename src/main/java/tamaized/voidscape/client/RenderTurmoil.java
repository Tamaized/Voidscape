package tamaized.voidscape.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.opengl.GL11;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.turmoil.Turmoil;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Voidscape.MODID)
public class RenderTurmoil {

	private static final ResourceLocation TEXTURE_MASK = new ResourceLocation(Voidscape.MODID, "textures/ui/mask.png");
	private static float fade = 0F;

	@SubscribeEvent
	public static void render(RenderGameOverlayEvent.Post event) {
		if (event.getType() != RenderGameOverlayEvent.ElementType.ALL)
			return;
		final float rate = 0.0025F;
		final float delta = 60F / Minecraft.debugFPS;
		if (!Minecraft.getInstance().isGamePaused()) {
			if (Turmoil.STATE == Turmoil.State.CLOSED) {
				if (fade > 0) {
					fade -= delta * rate * 2F;
					if (fade < 0.1F)
						fade = 0.1F;
				}
			} else {
				if (fade < 1F) {
					fade += delta * rate;
					if (fade > 1F)
						fade = 1F;
				}
			}
		}
		BufferBuilder buffer = Tessellator.getInstance().getBuffer();
		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR_TEXTURE);

		MainWindow window = Minecraft.getInstance().getWindow();

		float x = 0F;
		float y = 0F;
		float w = window.getScaledWidth();
		float h = window.getScaledHeight();
		float z = 0F;

		float r = 1F;
		float g = 1F;
		float b = 1F;
		float a = 1F;

		buffer.vertex(x, y, z).color(r, g, b, a).texture(0F, 0F).endVertex();
		buffer.vertex(x + w, y, z).color(r, g, b, a).texture(1F, 0F).endVertex();
		buffer.vertex(x + w, y + h, z).color(r, g, b, a).texture(1F, 1F).endVertex();
		buffer.vertex(x, y + h, z).color(r, g, b, a).texture(0F, 1F).endVertex();

		Minecraft.getInstance().getTextureManager().bindTexture(TEXTURE_MASK);

		RenderSystem.enableBlend();
		RenderSystem.alphaFunc(GL11.GL_LESS, fade);
		Tessellator.getInstance().draw();
		RenderSystem.defaultAlphaFunc();
	}

}
