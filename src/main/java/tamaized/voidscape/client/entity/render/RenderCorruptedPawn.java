package tamaized.voidscape.client.entity.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.util.ResourceLocation;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.client.entity.model.ModelCorruptedPawn;
import tamaized.voidscape.entity.EntityCorruptedPawn;

public class RenderCorruptedPawn<T extends EntityCorruptedPawn, M extends ModelCorruptedPawn<T>> extends LivingRenderer<T, M> {

	private static final ResourceLocation TEXTURE = new ResourceLocation(Voidscape.MODID, "textures/entity/corruptedpawn.png");

	public RenderCorruptedPawn(EntityRendererManager rendererManager, M model) {
		super(rendererManager, model, 0F);
	}

	@Override
	protected boolean shouldShowName(T entityIn) {
		return super.shouldShowName(entityIn) && (entityIn.shouldShowName() || entityIn.hasCustomName() && entityIn == this.entityRenderDispatcher.crosshairPickEntity);
	}

	@Override
	public void render(T entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
		if (entityIn.shouldRender(Minecraft.getInstance().player))
			super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
	}

	@Override
	public ResourceLocation getTextureLocation(T entityIn) {
		return TEXTURE;
	}

	public static <T extends EntityCorruptedPawn> RenderCorruptedPawn<T, ModelCorruptedPawn<T>> factory(EntityRendererManager manager) {
		return new RenderCorruptedPawn<>(manager, new ModelCorruptedPawn<>());
	}
}
