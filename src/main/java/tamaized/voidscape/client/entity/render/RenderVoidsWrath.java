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
import tamaized.voidscape.client.entity.model.ModelVoidsWrath;
import tamaized.voidscape.entity.EntityNullServant;
import tamaized.voidscape.entity.EntityVoidsWrathBoss;
import tamaized.voidscape.registry.ModEntities;

public class RenderVoidsWrath<T extends EntityVoidsWrathBoss> extends LivingEntityRenderer<T, ModelVoidsWrath<T>> {

	private static final ResourceLocation TEXTURE = new ResourceLocation(Voidscape.MODID, "textures/entity/voidswrath.png");

	public RenderVoidsWrath(EntityRendererProvider.Context rendererManager) {
		super(rendererManager, new ModelVoidsWrath<>(rendererManager.bakeLayer(ModEntities.ModelLayerLocations.VOIDS_WRATH)), 0F);
		this.addLayer(new ItemInHandLayer<>(this));
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

}
