package tamaized.voidscape.client.entity.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.entity.abilities.mage.EntitySpellBolt;

public class RenderSpellBolt<T extends EntitySpellBolt> extends EntityRenderer<T> {

	private static final ResourceLocation TEXTURE = new ResourceLocation(Voidscape.MODID, "textures/entity/spells/mage/bolt.png");

	public RenderSpellBolt(EntityRendererManager p_i46179_1_) {
		super(p_i46179_1_);
	}

	private void vertex(IVertexBuilder buffer, Matrix4f vertex, Matrix3f normals, float x, float y, float z, float red, float green, float blue, float alpha, float texU, float texV, int overlayUV, int lightmapUV, float normalX, float normalY, float normalZ) {
		buffer.vertex(vertex, x, y, z);
		buffer.color(red, green, blue, alpha);
		buffer.uv(texU, texV);
		//buffer.overlayCoords(overlayUV);
		buffer.uv2(lightmapUV);
		buffer.normal(normals, normalX, normalY, normalZ);
		buffer.endVertex();
	}

	@Override
	public void render(T entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
		IVertexBuilder buffer = bufferIn.getBuffer(RenderType.beaconBeam(getTextureLocation(entityIn), true));
		matrixStackIn.pushPose();
		MatrixStack.Entry stack = matrixStackIn.last();
		Matrix4f v = stack.pose();
		Matrix3f n = stack.normal();
		final float size = 0.5F;
		for (int i = 0; i < 8; i++) {
			int deg = (45 * i + entityIn.tickCount * 2) % 360;
			matrixStackIn.mulPose(Vector3f.XP.rotationDegrees(deg));
			matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(deg));
			vertex(buffer, v, n, -size, -size, 0, 0.25F, 0F, 0.6F, 0.75F, 0, 0, 0xF000F0, OverlayTexture.NO_OVERLAY, 0F, 1F, 0F);
			vertex(buffer, v, n, -size, size, 0, 0.25F, 0F, 0.6F, 0.75F, 0, 1, 0xF000F0, OverlayTexture.NO_OVERLAY, 0F, 1F, 0F);
			vertex(buffer, v, n, size, size, 0, 0.25F, 0F, 0.6F, 0.75F, 1, 1, 0xF000F0, OverlayTexture.NO_OVERLAY, 0F, 1F, 0F);
			vertex(buffer, v, n, size, -size, 0, 0.25F, 0F, 0.6F, 0.75F, 1, 0, 0xF000F0, OverlayTexture.NO_OVERLAY, 0F, 1F, 0F);
		}
		matrixStackIn.popPose();
/*		RenderSystem.pushMatrix();
		RenderSystem.disableCull();
		RenderSystem.disableLighting();
		RenderSystem.enableBlend();
		RenderSystem.translated(x - 0.5F, y - 0.5F, z - 0.0F);
		//		RenderSystem.rotatef(180F - getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder vertexbuffer = tessellator.getBuffer();

		for (int i = 0; i < 8; i++) {
			RenderSystem.rotatef((45 * i + entity.ticksExisted * 2) % 360, 1, 1, 0);
			//			RenderSystem.translated(-0.25F + ((0.125F * i) % 4), -0.25F + (0.125F * ((i + 2) % 4)), -0.25F + (0.125F * ((i - 2) % 4)));
			vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
			vertexbuffer.pos(1, 1, 0).tex(1, 1).endVertex();
			vertexbuffer.pos(1, 0, 0).tex(1, 0).endVertex();
			vertexbuffer.pos(0, 0, 0).tex(0, 0).endVertex();
			vertexbuffer.pos(0, 1, 0).tex(0, 1).endVertex();
			tessellator.draw();
		}
		RenderSystem.disableBlend();
		RenderSystem.enableLighting();
		RenderSystem.enableCull();
		RenderSystem.popMatrix();*/
	}

	@Override
	public ResourceLocation getTextureLocation(T entityIn) {
		return TEXTURE;
	}
}
