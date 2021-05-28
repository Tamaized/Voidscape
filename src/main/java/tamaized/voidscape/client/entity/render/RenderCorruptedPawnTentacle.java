package tamaized.voidscape.client.entity.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.client.entity.model.ModelCorruptedPawnTentacle;
import tamaized.voidscape.entity.EntityCorruptedPawnTentacle;

public class RenderCorruptedPawnTentacle<T extends EntityCorruptedPawnTentacle> extends EntityRenderer<T> {

	private static final ResourceLocation TEXTURE = new ResourceLocation(Voidscape.MODID, "textures/entity/corruptedpawn.png");
	private final ModelCorruptedPawnTentacle<T> model = new ModelCorruptedPawnTentacle<>();

	public RenderCorruptedPawnTentacle(EntityRendererManager rendererManager) {
		super(rendererManager);
	}

	@Override
	protected boolean shouldShowName(T entityIn) {
		return super.shouldShowName(entityIn) && (entityIn.shouldShowName() || entityIn.hasCustomName() && entityIn == this.entityRenderDispatcher.crosshairPickEntity);
	}

	@Override
	public void render(T entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
		matrixStackIn.pushPose();
		matrixStackIn.translate(0, 0.01F, 0);
		final float scale = entityIn.binding() ? 4F : 10F;
		matrixStackIn.scale(scale, scale, scale);
		model.setupAnim(entityIn, 0, 0, entityIn.tickCount + partialTicks, 0, 0);
		float color = (float) (1F - MathHelper.clamp((entityIn.getExplosionTimer() + partialTicks) / entityIn.getExplosionDuration(), 0F, 1F));
		model.renderToBuffer(matrixStackIn, bufferIn.getBuffer(model.renderType(TEXTURE)), packedLightIn, OverlayTexture.NO_OVERLAY,

				1F,

				color,

				color,

				MathHelper.clamp(entityIn.getDeathTicks() <= 0 ? (entityIn.tickCount + partialTicks) / (20F * 5F) : 1F - (entityIn.getDeathTicks() + partialTicks) / (20F * 5F), 0F, 1F));
		matrixStackIn.popPose();
	}

	@Override
	public ResourceLocation getTextureLocation(T entityIn) {
		return TEXTURE;
	}
}
