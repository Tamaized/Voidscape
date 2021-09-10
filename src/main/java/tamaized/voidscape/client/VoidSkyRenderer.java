package tamaized.voidscape.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.ISkyRenderHandler;

public class VoidSkyRenderer implements ISkyRenderHandler {

	/*private static final ResourceLocation END_SKY_TEXTURE = new ResourceLocation("textures/environment/end_sky.png");
	private static final ResourceLocation END_PORTAL_TEXTURE = new ResourceLocation("textures/entity/end_portal.png");
	private static final Random RANDOM = new Random(31100L);
	private static final FloatBuffer MODELVIEW = GLAllocation.createFloatBuffer(16);
	private static final FloatBuffer PROJECTION = GLAllocation.createFloatBuffer(16);
	private FloatBuffer buffer = GLAllocation.createFloatBuffer(16);*/

	@Override
	public void render(int ticks, float partialTicks, PoseStack matrixStack, ClientLevel world, Minecraft mc) { // FIXME: use a shader
		/*double scale = 200.0D;
		double offset = scale / 2D;

		double x = -offset;
		double y = -offset;
		double z = -offset;
		RenderSystem.disableLighting();
		RANDOM.setSeed(31100L);
		GlStateManager._getMatrix(2982, MODELVIEW);
		GlStateManager._getMatrix(2983, PROJECTION);

		float exactTick = (float) ticks + partialTicks;
		float dur = 3200F;
		float phase = Mth.cos((float) Math.toRadians(((exactTick % dur) / dur) * 360F)) * 0.5F + 0.5F;
		float maxPhases = 8F;
		int minPhases = 1;
		int phases = (int) Math.ceil((phase * maxPhases) / 1F);

		for (int j = 0; j < phases + minPhases; ++j) {
			RenderSystem.pushMatrix();
			float f1 = 2.0F / (float) (18 - j);

			if (j == 0) {
				this.bindTexture(END_SKY_TEXTURE);
				f1 = 0.15F;
				RenderSystem.enableBlend();
				RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
			}

			if (j >= 1) {
				this.bindTexture(END_PORTAL_TEXTURE);
			}

			if (j == 1) {
				RenderSystem.enableBlend();
				RenderSystem.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
			}

			GlStateManager._texGenMode(GlStateManager.TexGen.S, 9216);
			GlStateManager._texGenMode(GlStateManager.TexGen.T, 9216);
			GlStateManager._texGenMode(GlStateManager.TexGen.R, 9216);
			GlStateManager._texGenParam(GlStateManager.TexGen.S, 9474, this.getBuffer(1.0F, 0.0F, 0.0F));
			GlStateManager._texGenParam(GlStateManager.TexGen.T, 9474, this.getBuffer(0.0F, 1.0F, 0.0F));
			GlStateManager._texGenParam(GlStateManager.TexGen.R, 9474, this.getBuffer(0.0F, 0.0F, 1.0F));
			GlStateManager._enableTexGen(GlStateManager.TexGen.S);
			GlStateManager._enableTexGen(GlStateManager.TexGen.T);
			GlStateManager._enableTexGen(GlStateManager.TexGen.R);
			RenderSystem.popMatrix();
			RenderSystem.matrixMode(5890);
			RenderSystem.pushMatrix();
			RenderSystem.loadIdentity();
			RenderSystem.translatef(0.5F, 0.5F, 0.0F);
			RenderSystem.scalef(0.5F, 0.5F, 1.0F);
			float f2 = (float) (j + 1);
			RenderSystem.translatef(17.0F / f2, (2.0F + f2 / 1.5F) * exactTick * 0.0002F, 0.0F);
			RenderSystem.rotatef((f2 * f2 * 4321.0F + f2 * 9.0F) * 2.0F, 0.0F, 0.0F, 1.0F);
			RenderSystem.scalef(4.5F - f2 / 4.0F, 4.5F - f2 / 4.0F, 1.0F);
			GlStateManager._multMatrix(PROJECTION);
			GlStateManager._multMatrix(MODELVIEW);
			Tesselator tessellator = Tesselator.getInstance();
			BufferBuilder vertexbuffer = tessellator.getBuilder();
			vertexbuffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

			float fade = j == 0 ? 0.1F : j < (phases + (minPhases - 1)) ? 1.0F : (phase % (1F / maxPhases) * maxPhases);

			float f3 = (RANDOM.nextFloat() * 0.5F + 0.1F) * f1 * fade;
			float f4 = (RANDOM.nextFloat() * 0.5F + 0.4F) * f1 * fade * 0.7F;
			float f5 = (RANDOM.nextFloat() * 0.5F + 0.5F) * f1 * fade * 0.8F;

			float alpha = 1.0F;

			vertexbuffer.vertex(x, y, z).color(f3, f4, f5, alpha).endVertex();
			vertexbuffer.vertex(x + scale, y, z).color(f3, f4, f5, alpha).endVertex();
			vertexbuffer.vertex(x + scale, y + scale, z).color(f3, f4, f5, alpha).endVertex();
			vertexbuffer.vertex(x, y + scale, z).color(f3, f4, f5, alpha).endVertex();

			vertexbuffer.vertex(x, y + scale, z + scale).color(f3, f4, f5, alpha).endVertex();
			vertexbuffer.vertex(x + scale, y + scale, z + scale).color(f3, f4, f5, alpha).endVertex();
			vertexbuffer.vertex(x + scale, y, z + scale).color(f3, f4, f5, alpha).endVertex();
			vertexbuffer.vertex(x, y, z + scale).color(f3, f4, f5, alpha).endVertex();

			vertexbuffer.vertex(x, y + scale, z).color(f3, f4, f5, alpha).endVertex();
			vertexbuffer.vertex(x, y + scale, z + scale).color(f3, f4, f5, alpha).endVertex();
			vertexbuffer.vertex(x, y, z + scale).color(f3, f4, f5, alpha).endVertex();
			vertexbuffer.vertex(x, y, z).color(f3, f4, f5, alpha).endVertex();

			vertexbuffer.vertex(x + scale, y, z).color(f3, f4, f5, alpha).endVertex();
			vertexbuffer.vertex(x + scale, y, z + scale).color(f3, f4, f5, alpha).endVertex();
			vertexbuffer.vertex(x + scale, y + scale, z + scale).color(f3, f4, f5, alpha).endVertex();
			vertexbuffer.vertex(x + scale, y + scale, z).color(f3, f4, f5, alpha).endVertex();

			vertexbuffer.vertex(x, y + scale, z).color(f3, f4, f5, alpha).endVertex();
			vertexbuffer.vertex(x + scale, y + scale, z).color(f3, f4, f5, alpha).endVertex();
			vertexbuffer.vertex(x + scale, y + scale, z + scale).color(f3, f4, f5, alpha).endVertex();
			vertexbuffer.vertex(x, y + scale, z + scale).color(f3, f4, f5, alpha).endVertex();

			vertexbuffer.vertex(x, y, z + scale).color(f3, f4, f5, alpha).endVertex();
			vertexbuffer.vertex(x + scale, y, z + scale).color(f3, f4, f5, alpha).endVertex();
			vertexbuffer.vertex(x + scale, y, z).color(f3, f4, f5, alpha).endVertex();
			vertexbuffer.vertex(x, y, z).color(f3, f4, f5, alpha).endVertex();

			RenderSystem.disableFog();
			tessellator.end();
			RenderSystem.enableFog();
			RenderSystem.popMatrix();
			RenderSystem.matrixMode(5888);
			this.bindTexture(END_SKY_TEXTURE);
		}

		RenderSystem.disableBlend();
		GlStateManager._disableTexGen(GlStateManager.TexGen.S);
		GlStateManager._disableTexGen(GlStateManager.TexGen.T);
		GlStateManager._disableTexGen(GlStateManager.TexGen.R);
		RenderSystem.enableLighting();*/
	}

	/*private FloatBuffer getBuffer(float p_188193_1_, float p_188193_2_, float p_188193_3_) {
		this.buffer.clear();
		this.buffer.put(p_188193_1_).put(p_188193_2_).put(p_188193_3_).put(0.0F);
		this.buffer.flip();
		return this.buffer;
	}*/

	protected void bindTexture(ResourceLocation location) {
		ClientUtil.bindTexture(location);
	}
}
