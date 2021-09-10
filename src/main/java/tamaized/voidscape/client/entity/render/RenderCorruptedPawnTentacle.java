package tamaized.voidscape.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.client.entity.model.ModelCorruptedPawnTentacle;
import tamaized.voidscape.entity.EntityCorruptedPawnTentacle;
import tamaized.voidscape.registry.ModEntities;

public class RenderCorruptedPawnTentacle<T extends EntityCorruptedPawnTentacle> extends EntityRenderer<T> {

	private static final ResourceLocation TEXTURE = new ResourceLocation(Voidscape.MODID, "textures/entity/corruptedpawn.png");
	private final ModelCorruptedPawnTentacle<T> model;

	public RenderCorruptedPawnTentacle(EntityRendererProvider.Context rendererManager) {
		super(rendererManager);
		model = new ModelCorruptedPawnTentacle<>(rendererManager.bakeLayer(ModEntities.ModelLayerLocations.CORRUPTED_PAWN_TENTACLE));
	}

	@Override
	protected boolean shouldShowName(T entityIn) {
		return super.shouldShowName(entityIn) && (entityIn.shouldShowName() || entityIn.hasCustomName() && entityIn == this.entityRenderDispatcher.crosshairPickEntity);
	}

	@Override
	public void render(T entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
		matrixStackIn.pushPose();
		matrixStackIn.translate(0, 0.01F, 0);
		final float scale = entityIn.binding() ? 4F : 10F;
		matrixStackIn.scale(scale, scale, scale);
		model.setupAnim(entityIn, 0, 0, entityIn.tickCount + partialTicks, 0, 0);
		float color = (float) (1F - Mth.clamp((entityIn.getExplosionTimer() + partialTicks) / entityIn.getExplosionDuration(), 0F, 1F));
		model.renderToBuffer(matrixStackIn, bufferIn.getBuffer(model.renderType(TEXTURE)), packedLightIn, OverlayTexture.NO_OVERLAY,

				1F,

				color,

				color,

				Mth.clamp(entityIn.getDeathTicks() <= 0 ? (entityIn.tickCount + partialTicks) / (20F * 5F) : 1F - (entityIn.getDeathTicks() + partialTicks) / (20F * 5F), 0F, 1F));
		matrixStackIn.popPose();
	}

	@Override
	public ResourceLocation getTextureLocation(T entityIn) {
		return TEXTURE;
	}
}
