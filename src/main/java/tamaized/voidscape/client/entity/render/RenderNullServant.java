package tamaized.voidscape.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.TheEndPortalRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import tamaized.voidscape.client.entity.model.ModelNullServant;
import tamaized.voidscape.entity.EntityNullServant;
import tamaized.voidscape.registry.ModEntities;

public class RenderNullServant<T extends EntityNullServant> extends LivingEntityRenderer<T, ModelNullServant<T>> {

	public RenderNullServant(EntityRendererProvider.Context rendererManager) {
		super(rendererManager, new ModelNullServant<>(rendererManager.bakeLayer(ModEntities.ModelLayerLocations.NULL_SERVANT)), 0F);
	}

	@Override
	protected boolean shouldShowName(T entityIn) {
		return super.shouldShowName(entityIn) && (entityIn.shouldShowName() || entityIn.hasCustomName() && entityIn == this.entityRenderDispatcher.crosshairPickEntity);
	}

	@Override
	public void render(T entity, float yaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
		super.render(entity, yaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
	}

	@Override
	public ResourceLocation getTextureLocation(T entityIn) {
		return TheEndPortalRenderer.END_PORTAL_LOCATION;
	}
}
