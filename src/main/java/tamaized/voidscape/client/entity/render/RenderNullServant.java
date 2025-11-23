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
import net.minecraft.util.FastColor;
import net.neoforged.api.distmarker.Dist;
import tamaized.beanification.Autowired;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.client.entity.ModModelLayerLocations;
import tamaized.voidscape.client.entity.model.ModelNullServant;
import tamaized.voidscape.entity.NullServantEntity;

public class RenderNullServant<T extends NullServantEntity> extends LivingEntityRenderer<T, ModelNullServant<T>> {

	@Autowired(dist = Dist.CLIENT)
	private static ModModelLayerLocations modelLayerLocations;

	private static class ColorHack {
		private boolean eyes = false;
		private int color = 0xFFFFFF;

		void reset() {
			color = 0xFFFFFF;
		}
	}

	private static final ColorHack COLOR_STATE = new ColorHack();

	public RenderNullServant(EntityRendererProvider.Context rendererManager) {
		super(rendererManager, new ModelNullServant<>(rendererManager.bakeLayer(modelLayerLocations.NULL_SERVANT)) {
			@Override
			public void renderToBuffer(PoseStack stack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
				super.renderToBuffer(stack, buffer, packedLight, packedOverlay, COLOR_STATE.eyes ? color : FastColor.ARGB32.color(0xFF, COLOR_STATE.color));
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
			COLOR_STATE.color = 0x00FF00;
		} else if (entity.getAugment() == NullServantEntity.AUGMENT_ICHOR) {
			COLOR_STATE.color = 0xFF7F00;
		} else if (entity.getAugment() == NullServantEntity.AUGMENT_ASTRAL) {
			COLOR_STATE.color = 0xFFB2CC;
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

		private static final RenderType EYES = RenderType.eyes(ResourceLocation.fromNamespaceAndPath(Voidscape.MODID, "textures/entity/nullservant.png"));

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
