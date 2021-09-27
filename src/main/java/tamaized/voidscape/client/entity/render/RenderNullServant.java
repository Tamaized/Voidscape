package tamaized.voidscape.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.TheEndPortalRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.client.entity.model.ModelNullServant;
import tamaized.voidscape.entity.EntityNullServant;
import tamaized.voidscape.registry.ModEntities;

public class RenderNullServant<T extends EntityNullServant> extends LivingEntityRenderer<T, ModelNullServant<T>> {

	public RenderNullServant(EntityRendererProvider.Context rendererManager) {
		super(rendererManager, new ModelNullServant<>(rendererManager.bakeLayer(ModEntities.ModelLayerLocations.NULL_SERVANT)), 0F);
		this.addLayer(new ItemInHandLayer<>(this));
		this.addLayer(new EyeLayer(this));
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

	private class EyeLayer extends EyesLayer<T, ModelNullServant<T>> {

		private static final RenderType EYES = RenderType.eyes(new ResourceLocation(Voidscape.MODID, "textures/entity/nullservant.png"));

		public EyeLayer(RenderLayerParent<T, ModelNullServant<T>> p_117346_) {
			super(p_117346_);
		}

		@Override
		public RenderType renderType() {
			return EYES;
		}
	}

}
