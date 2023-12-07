package tamaized.voidscape.client.ui;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.client.event.RegisterGuiOverlaysEvent;
import net.neoforged.neoforge.event.TickEvent;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.data.Insanity;
import tamaized.voidscape.client.ClientUtil;
import tamaized.voidscape.client.Shaders;
import tamaized.voidscape.client.StencilBufferUtil;
import tamaized.voidscape.registry.ModDataAttachments;

public class RenderTurmoil {

	public static final int STENCIL_INDEX = 10;
	public static final ResourceLocation TEXTURE_MASK = new ResourceLocation(Voidscape.MODID, "textures/ui/mask.png");
	static final ResourceLocation TEXTURE_VOIDICINFUSION = new ResourceLocation(Voidscape.MODID, "textures/ui/voidicinfusion.png");
	static final ResourceLocation TEXTURE_WATCHINGYOU = new ResourceLocation(Voidscape.MODID, "textures/ui/watchingyou.png");
	private static float deltaTick;
	private static float lastDeltaTick;
	private static float lastTeleportTick;

	public static void tick(TickEvent.ClientTickEvent event) {
		if (event.phase == TickEvent.Phase.START || Minecraft.getInstance().isPaused() || Minecraft.getInstance().level == null)
			return;
		lastDeltaTick = deltaTick;
		ClientUtil.tick++;
		if (Minecraft.getInstance().player != null) {
			Insanity data = Minecraft.getInstance().player.getData(ModDataAttachments.INSANITY);
			if (data.getTeleportTick() >= (lastTeleportTick + 20) || (lastTeleportTick == 0 && data.getTeleportTick() > 0)) {
				Minecraft.getInstance().player.playSound(SoundEvents.CONDUIT_AMBIENT_SHORT, 4F, 1F);
				lastTeleportTick = data.getTeleportTick();
			}
			if (data.getTeleportTick() < lastTeleportTick)
				lastTeleportTick = data.getTeleportTick();
			if (data.getTeleportTick() > deltaTick)
				deltaTick++;
			else if (data.getTeleportTick() < deltaTick)
				deltaTick--;
		}
	}

	public static void render(RegisterGuiOverlaysEvent event) {
		event.registerAboveAll(new ResourceLocation(Voidscape.MODID, "turmoil"), (gui, poseStack, partialTick, width, height) -> {
			Level world = Minecraft.getInstance().level;
			if (world != null && Minecraft.getInstance().player != null) {
				renderInsanity(Minecraft.getInstance().player.getData(ModDataAttachments.INSANITY));
				float perc = Mth.clamp(Mth.lerp(partialTick, lastDeltaTick, deltaTick) / 200F, 0F, 1F);
				if (perc > 0) {
					RenderSystem.enableBlend();
					{

						Window window = Minecraft.getInstance().getWindow();

						float x = 0F;
						float y = 0F;
						float w = window.getGuiScaledWidth();
						float h = window.getGuiScaledHeight();
						float z = 401F; // Catch All

						ClientUtil.bindTexture(TEXTURE_MASK);
						Color24.INSTANCE.set(1F, 1F, 1F, 1F).apply(true, x, y, z, w, h);
						StencilBufferUtil.setup(STENCIL_INDEX, () -> Shaders.OPTIMAL_ALPHA_LESSTHAN_POS_TEX_COLOR.invokeThenEndTesselator(perc));

						Color24.INSTANCE.set(0F, 0F, 0F, 1F).apply(false, x, y, z, w, h);
						StencilBufferUtil.renderAndFlush(STENCIL_INDEX, () -> Shaders.WRAPPED_POS_COLOR.invokeThenEndTesselator());
					}
					RenderSystem.disableBlend();
				}
			}
		});
	}

	private static void renderInsanity(Insanity insanity) {
		renderInfusion(insanity);
		renderParanoia(insanity);
	}

	private static void renderParanoia(Insanity insanity) {
		if (insanity.getParanoia() < 500F)
			return;
		float perc = (insanity.getParanoia() - 500F) / 90F;
		perc = Mth.clamp(perc, 0, 1);
		perc *= 0.25F;
		float endPerc = (insanity.getParanoia() - 590F) / 10F;
		endPerc = Mth.clamp(endPerc, 0, 1);
		endPerc *= 0.15F;
		perc += endPerc;
		BufferBuilder buffer = Tesselator.getInstance().getBuilder();
		buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
		ClientUtil.bindTexture(TEXTURE_WATCHINGYOU);
		final float w = Minecraft.getInstance().getWindow().getGuiScaledWidth();
		final float h = Minecraft.getInstance().getWindow().getGuiScaledHeight();
		buffer.vertex(0, h, 0).uv(0, 1).color(1F, 1F, 1F, perc).endVertex();
		buffer.vertex(w, h, 0).uv(1, 1).color(1F, 1F, 1F, perc).endVertex();
		buffer.vertex(w, 0, 0).uv(1, 0).color(1F, 1F, 1F, perc).endVertex();
		buffer.vertex(0, 0, 0).uv(0, 0).color(1F, 1F, 1F, perc).endVertex();
		RenderSystem.enableBlend();
		Shaders.OPTIMAL_ALPHA_GREATERTHAN_POS_TEX_COLOR.invokeThenEndTesselator(0F);
		RenderSystem.disableBlend();
	}

	private static void renderInfusion(Insanity insanity) {
		if (insanity.getInfusion() <= 0)
			return;
		float perc = insanity.getInfusion() / 600F;
		perc = Mth.clamp(perc, 0, 1);
		BufferBuilder buffer = Tesselator.getInstance().getBuilder();
		buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
		ClientUtil.bindTexture(TEXTURE_VOIDICINFUSION);
		final float w = Minecraft.getInstance().getWindow().getGuiScaledWidth();
		final float h = Minecraft.getInstance().getWindow().getGuiScaledHeight();
		buffer.vertex(0, h, 0).uv(0, 1).color(0.4F, 0F, 1F, perc).endVertex();
		buffer.vertex(w, h, 0).uv(1, 1).color(0.4F, 0F, 1F, perc).endVertex();
		buffer.vertex(w, 0, 0).uv(1, 0).color(0.4F, 0F, 1F, perc).endVertex();
		buffer.vertex(0, 0, 0).uv(0, 0).color(0.4F, 0F, 1F, perc).endVertex();
		RenderSystem.enableBlend();
		Shaders.OPTIMAL_ALPHA_GREATERTHAN_POS_TEX_COLOR.invokeThenEndTesselator(0F);
		RenderSystem.disableBlend();
	}

	public static class Color24 {

		public static final Color24 INSTANCE = new Color24();

		public int bit24;
		public int bit16;
		public int bit8;
		public int bit0;

		private Color24() {}

		public static float asFloat(int value) {
			return value / 255F;
		}

		public static int asInt(float value) {
			return (int) (value * 255);
		}

		public int packed() {
			return (bit24 << 24) | (bit16 << 16) | (bit8 << 8) | bit0;
		}

		public Color24 unpack(int packed) {
			return set(

					(packed >> 24) & 0xFF,

					(packed >> 16) & 0xFF,

					(packed >> 8) & 0xFF,

					packed & 0xFF

			);
		}

		public Color24 apply(boolean tex, float x, float y, float z, float w, float h) {
			BufferBuilder buffer = Tesselator.getInstance().getBuilder();
			buffer.begin(VertexFormat.Mode.QUADS, tex ? DefaultVertexFormat.POSITION_TEX_COLOR : DefaultVertexFormat.POSITION_COLOR);
			final float r = asFloat(bit24);
			final float g = asFloat(bit16);
			final float b = asFloat(bit8);
			final float a = asFloat(bit0);
			buffer.vertex(x, y + h, z);
			if (tex)
				buffer.uv(0F, 1F);
			buffer.color(r, g, b, a).endVertex();
			buffer.vertex(x + w, y + h, z);
			if (tex)
				buffer.uv(1F, 1F);
			buffer.color(r, g, b, a).endVertex();
			buffer.vertex(x + w, y, z);
			if (tex)
				buffer.uv(1F, 0F);
			buffer.color(r, g, b, a).endVertex();
			buffer.vertex(x, y, z);
			if (tex)
				buffer.uv(0F, 0F);
			buffer.color(r, g, b, a).endVertex();
			return this;
		}

		public void endTesselator() {
			Tesselator.getInstance().end();
		}

		public Color24 set(int b24, int b16, int b8, int b0) {
			bit0 = b0;
			bit8 = b8;
			bit16 = b16;
			bit24 = b24;
			return this;
		}

		public Color24 set(float b24, float b16, float b8, float b0) {
			set(asInt(b24), asInt(b16), asInt(b8), asInt(b0));
			return this;
		}

	}

}
