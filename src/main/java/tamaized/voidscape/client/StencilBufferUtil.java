package tamaized.voidscape.client;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

public final class StencilBufferUtil {

	private StencilBufferUtil() {

	}

	public static void setup(int index) {
		RenderSystem.enableBlend();
		invisibleBlend();
		enableStencil(index);
	}

	public static void finish() {
		disableStencil();
		RenderSystem.defaultBlendFunc();
	}

	public static void startRender(int index) {
		GL11.glEnable(GL11.GL_STENCIL_TEST);
		RenderSystem.stencilFunc(GL11.GL_EQUAL, index, 0xFF);
	}

	public static void endRenderAndFinish(int index) {
		endRender(index);
		finishRender();
	}

	public static void endRender(int index) {
		RenderSystem.stencilFunc(GL11.GL_ALWAYS, index, 0xFF);
	}

	public static void finishRender() {
		RenderSystem.stencilMask(0xFF);
		RenderSystem.clear(GL11.GL_STENCIL_BUFFER_BIT, Minecraft.IS_RUNNING_ON_MAC);
		RenderSystem.stencilMask(0x00);
		GL11.glDisable(GL11.GL_STENCIL_TEST);
	}

	public static void enableStencil(int index) {
		GL11.glEnable(GL11.GL_STENCIL_TEST);
		RenderSystem.stencilMask(0xFF);
		RenderSystem.stencilFunc(GL11.GL_ALWAYS, index, 0xFF);
		RenderSystem.stencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_REPLACE);
	}

	public static void disableStencil() {
		RenderSystem.stencilFunc(GL11.GL_ALWAYS, 0, 0xFF);
		RenderSystem.stencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_KEEP);
		RenderSystem.stencilMask(0x00);
		GL11.glDisable(GL11.GL_STENCIL_TEST);
	}

	public static void invisibleBlend() {
		RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.SRC_COLOR, GlStateManager.DestFactor.ZERO);
	}

}
