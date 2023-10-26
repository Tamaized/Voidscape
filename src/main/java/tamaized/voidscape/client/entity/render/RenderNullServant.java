package tamaized.voidscape.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
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
import tamaized.voidscape.entity.NullServantEntity;
import tamaized.voidscape.registry.ModEntities;

public class RenderNullServant<T extends NullServantEntity> extends LivingEntityRenderer<T, ModelNullServant<T>> {

	private static class ColorHack {
		private boolean eyes = false;
		private float red = 1F;
		private float green = 1F;
		private float blue = 1F;

		void reset() {
			red = 1F;
			green = 1F;
			blue = 1F;
		}
	}

	private static final ColorHack COLOR_STATE = new ColorHack();

	public RenderNullServant(EntityRendererProvider.Context rendererManager) {
		super(rendererManager, new ModelNullServant<>(rendererManager.bakeLayer(ModEntities.ModelLayerLocations.NULL_SERVANT)) {
			@Override
			public void renderToBuffer(PoseStack stack, VertexConsumer buffer, int p_103113_, int p_103114_, float r, float g, float b, float a) {
				super.renderToBuffer(stack, buffer, p_103113_, p_103114_, COLOR_STATE.eyes ? r : COLOR_STATE.red, COLOR_STATE.eyes ? g : COLOR_STATE.green, COLOR_STATE.eyes ? b : COLOR_STATE.blue, a);
			}
		}, 0F);
		this.addLayer(new ItemInHandLayer<>(this, rendererManager.getItemInHandRenderer()));
		this.addLayer(new EyeLayer(this));
	}

	@Override
	protected boolean shouldShowName(T entityIn) {
		return super.shouldShowName(entityIn) && (entityIn.shouldShowName() || entityIn.hasCustomName() && entityIn == this.entityRenderDispatcher.crosshairPickEntity);
	}

	@Override
	public void render(T entity, float yaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
		COLOR_STATE.eyes = false;
		if (entity.getAugment() == NullServantEntity.AUGMENT_TITANITE) {
			COLOR_STATE.red = 0F;
			COLOR_STATE.green = 1F;
			COLOR_STATE.blue = 0F;
		} else if (entity.getAugment() == NullServantEntity.AUGMENT_ICHOR) {
			COLOR_STATE.red = 1F;
			COLOR_STATE.green = 0.5F;
			COLOR_STATE.blue = 0F;
		} else if (entity.getAugment() == NullServantEntity.AUGMENT_ASTRAL) {
			COLOR_STATE.red = 1F;
			COLOR_STATE.green = 0.7F;
			COLOR_STATE.blue = 0.8F;
		} else {
			COLOR_STATE.reset();
		}
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
		public void render(PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight, T pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
			COLOR_STATE.eyes = true;
			super.render(pMatrixStack, pBuffer, pPackedLight, pLivingEntity, pLimbSwing, pLimbSwingAmount, pPartialTicks, pAgeInTicks, pNetHeadYaw, pHeadPitch);
		}

		@Override
		public RenderType renderType() {
			return EYES;
		}
	}

}
