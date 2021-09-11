package tamaized.voidscape.client;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.Tesselator;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

public final class StencilBufferUtil {

	private StencilBufferUtil() {

	}

	public static void setup(int index) {
		Minecraft.getInstance().getMainRenderTarget().enableStencil();
		RenderSystem.enableBlend();
		invisibleBlend();
		enableStencil(index);
	}

	public static void finish() {
		disableStencil();
		RenderSystem.defaultBlendFunc();
	}

	public static void setup(int index, Runnable run) {
		setup(index);
		run.run();
		finish();
	}

	public static void startRender(int index) {
		GL11.glEnable(GL11.GL_STENCIL_TEST);
		RenderSystem.stencilFunc(GL11.GL_EQUAL, index, 0xFF);
	}

	public static void endRenderAndFinish(int index) {
		endRender(index);
		finishRender();
	}

	public static void render(int index, Runnable run, boolean flush) {
		startRender(index);
		run.run();
		if (flush)
			endRenderAndFinish(index);
		else
			endRender(index);

	}

	public static void render(int index, Runnable run) {
		render(index, run, false);
	}

	public static void renderAndFlush(int index, Runnable run) {
		render(index, run, true);
	}

	public static void renderTesselator(int index) {
		render(index, () -> Tesselator.getInstance().end());
	}

	public static void renderTesselatorAndFlush(int index) {
		renderAndFlush(index, () -> Tesselator.getInstance().end());
	}

	public static void endRender(int index) {
		RenderSystem.stencilFunc(GL11.GL_ALWAYS, index, 0xFF);
	}

	public static void finishRender() {
		RenderSystem.stencilMask(0xFF);
		RenderSystem.clear(GL11.GL_STENCIL_BUFFER_BIT, Minecraft.ON_OSX);
		RenderSystem.stencilMask(0x00);
		GL11.glDisable(GL11.GL_STENCIL_TEST);
	}

	public static void enableStencil(int index) {
		Minecraft.getInstance().getMainRenderTarget().enableStencil();
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
