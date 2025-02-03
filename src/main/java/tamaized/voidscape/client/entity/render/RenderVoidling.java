package tamaized.voidscape.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import tamaized.beanification.Autowired;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.client.entity.ModModelLayerLocations;
import tamaized.voidscape.client.entity.model.ModelVoidling;
import tamaized.voidscape.entity.VoidlingEntity;

public class RenderVoidling<T extends VoidlingEntity, M extends ModelVoidling<T>> extends LivingEntityRenderer<T, M> {

	@Autowired
	private static ModModelLayerLocations modelLayerLocations;

	private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Voidscape.MODID, "textures/entity/voidling.png");

	public RenderVoidling(EntityRendererProvider.Context rendererManager, M model) {
		super(rendererManager, model, 0F);
	}

	public static <T extends VoidlingEntity> RenderVoidling<T, ModelVoidling<T>> factory(EntityRendererProvider.Context manager) {
		return new RenderVoidling<>(manager, new ModelVoidling<>(manager.bakeLayer(modelLayerLocations.VOIDLING)));
	}

	@Override
	protected boolean shouldShowName(T entityIn) {
		return super.shouldShowName(entityIn) && (entityIn.shouldShowName() || entityIn.hasCustomName() && entityIn == this.entityRenderDispatcher.crosshairPickEntity);
	}

	@Override
	public void render(T entity, float yaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
		super.render(entity, yaw, partialTicks, matrixStackIn, bufferIn, LightTexture.FULL_BRIGHT);
	}

	@Override
	public ResourceLocation getTextureLocation(T entityIn) {
		return TEXTURE;
	}

	@Nullable
	@Override
	protected RenderType getRenderType(T entity, boolean bodyVisible, boolean translucent, boolean glowing) {
		return RenderType.entityTranslucentCull(getTextureLocation(entity));
	}

	@Override
	protected void scale(T entity, PoseStack stack, float partialTick) {
		float scale = 0.7F;
		stack.scale(scale, scale, scale);
	}
}
