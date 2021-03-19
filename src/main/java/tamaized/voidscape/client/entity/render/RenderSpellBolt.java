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
import tamaized.voidscape.entity.abilities.EntitySpellBolt;

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
		final int color = entityIn.color();
		final float red = ((color >> 16) & 0xFF) / 255F;
		final float green = ((color >> 8) & 0xFF) / 255F;
		final float blue = (color & 0xFF) / 255F;
		for (int i = 0; i < 8; i++) {
			int deg = (45 * i + entityIn.tickCount * 2) % 360;
			matrixStackIn.mulPose(Vector3f.XP.rotationDegrees(deg));
			matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(deg));
			vertex(buffer, v, n, -size, -size, 0, red, green, blue, 0.75F, 0, 0, 0xF000F0, OverlayTexture.NO_OVERLAY, 0F, 1F, 0F);
			vertex(buffer, v, n, -size, size, 0, red, green, blue, 0.75F, 0, 1, 0xF000F0, OverlayTexture.NO_OVERLAY, 0F, 1F, 0F);
			vertex(buffer, v, n, size, size, 0, red, green, blue, 0.75F, 1, 1, 0xF000F0, OverlayTexture.NO_OVERLAY, 0F, 1F, 0F);
			vertex(buffer, v, n, size, -size, 0, red, green, blue, 0.75F, 1, 0, 0xF000F0, OverlayTexture.NO_OVERLAY, 0F, 1F, 0F);
		}
		matrixStackIn.popPose();
	}

	@Override
	public ResourceLocation getTextureLocation(T entityIn) {
		return TEXTURE;
	}
}
