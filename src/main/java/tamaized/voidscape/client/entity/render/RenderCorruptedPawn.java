package tamaized.voidscape.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.resources.ResourceLocation;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.client.entity.model.ModelCorruptedPawn;
import tamaized.voidscape.entity.EntityCorruptedPawn;
import tamaized.voidscape.registry.ModEntities;

public class RenderCorruptedPawn<T extends EntityCorruptedPawn, M extends ModelCorruptedPawn<T>> extends LivingEntityRenderer<T, M> {

	private static final ResourceLocation TEXTURE = new ResourceLocation(Voidscape.MODID, "textures/entity/corruptedpawn.png");
	private static final RenderType OVERLAY = RenderType.eyes(new ResourceLocation(Voidscape.MODID, "textures/entity/corruptedpawn_overlay.png"));

	public RenderCorruptedPawn(EntityRendererProvider.Context rendererManager, M model) {
		super(rendererManager, model, 0F);
		addLayer(new EyesLayer<>(this) {
			@Override
			public RenderType renderType() {
				return OVERLAY;
			}
		});
	}

	public static <T extends EntityCorruptedPawn> RenderCorruptedPawn<T, ModelCorruptedPawn<T>> factory(EntityRendererProvider.Context manager) {
		return new RenderCorruptedPawn<>(manager, new ModelCorruptedPawn<>(manager.bakeLayer(ModEntities.ModelLayerLocations.CORRUPTED_PAWN)));
	}

	@Override
	protected boolean shouldShowName(T entityIn) {
		return super.shouldShowName(entityIn) && (entityIn.shouldShowName() || entityIn.hasCustomName() && entityIn == this.entityRenderDispatcher.crosshairPickEntity);
	}

	@Override
	public void render(T entity, float yaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
		if (entity.shouldRender(Minecraft.getInstance().player)) {
			super.render(entity, yaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
		}
	}

	@Override
	public ResourceLocation getTextureLocation(T entityIn) {
		return TEXTURE;
	}
}
