package tamaized.voidscape.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EnergySwirlLayer;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.client.entity.model.ModelVoidsWrath;
import tamaized.voidscape.entity.EntityVoidsWrathBoss;
import tamaized.voidscape.registry.ModEntities;

public class RenderVoidsWrath<T extends EntityVoidsWrathBoss> extends LivingEntityRenderer<T, ModelVoidsWrath<T>> {

	private static final ResourceLocation TEXTURE = new ResourceLocation(Voidscape.MODID, "textures/entity/voidswrath.png");

	public RenderVoidsWrath(EntityRendererProvider.Context rendererManager) {
		super(rendererManager, new ModelVoidsWrath<>(rendererManager.bakeLayer(ModEntities.ModelLayerLocations.VOIDS_WRATH)), 0F);
		this.addLayer(new ItemInHandLayer<>(this));
		this.addLayer(new OverlayLayer(this));
		this.addLayer(new PowerLayer(this, rendererManager.getModelSet()));
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
		return TEXTURE;
	}

	private class OverlayLayer extends EyesLayer<T, ModelVoidsWrath<T>> {

		private static final RenderType OVERLAY = RenderType.eyes(new ResourceLocation(Voidscape.MODID, "textures/entity/voidswrath_overlay.png"));

		public OverlayLayer(RenderLayerParent<T, ModelVoidsWrath<T>> p_117346_) {
			super(p_117346_);
		}

		@Override
		public RenderType renderType() {
			return OVERLAY;
		}
	}

	private class PowerLayer extends EnergySwirlLayer<T, ModelVoidsWrath<T>> {
		private static final ResourceLocation POWER_LOCATION = new ResourceLocation(Voidscape.MODID, "textures/entity/voidswrath_armor.png");
		private final ModelVoidsWrath<T> model;

		public PowerLayer(RenderLayerParent<T, ModelVoidsWrath<T>> p_174471_, EntityModelSet p_174472_) {
			super(p_174471_);
			this.model = new ModelVoidsWrath<>(p_174472_.bakeLayer(ModEntities.ModelLayerLocations.VOIDS_WRATH_CHARGED));
		}

		@Override
		public void render(PoseStack p_116970_, MultiBufferSource p_116971_, int p_116972_, T p_116973_, float p_116974_, float p_116975_, float p_116976_, float p_116977_, float p_116978_, float p_116979_) {
			super.render(p_116970_, p_116971_, 0xF000F0, p_116973_, p_116974_, p_116975_, p_116976_, p_116977_, p_116978_, p_116979_);
		}

		protected float xOffset(float p_116683_) {
			return p_116683_ * 0.01F;
		}

		protected ResourceLocation getTextureLocation() {
			return POWER_LOCATION;
		}

		protected EntityModel<T> model() {
			return this.model;
		}
	}

}
