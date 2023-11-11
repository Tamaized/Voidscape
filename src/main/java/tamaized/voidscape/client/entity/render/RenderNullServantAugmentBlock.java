package tamaized.voidscape.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import tamaized.voidscape.entity.NullServantAugmentBlockEntity;

public class RenderNullServantAugmentBlock<T extends NullServantAugmentBlockEntity> extends EntityRenderer<T> {
	private final BlockRenderDispatcher dispatcher;

	public RenderNullServantAugmentBlock(EntityRendererProvider.Context rendererManager) {
		super(rendererManager);
		this.dispatcher = rendererManager.getBlockRenderDispatcher();
	}

	@Override
	protected boolean shouldShowName(T entityIn) {
		return super.shouldShowName(entityIn) && (entityIn.shouldShowName() || entityIn.hasCustomName() && entityIn == this.entityRenderDispatcher.crosshairPickEntity);
	}

	@Override
	public void render(T entity, float yaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
		BlockState blockstate = entity.getMimic();
		if (blockstate.getRenderShape() == RenderShape.MODEL) {
			Level level = entity.level();
			if (blockstate != level.getBlockState(entity.blockPosition()) && blockstate.getRenderShape() != RenderShape.INVISIBLE) {
				matrixStackIn.pushPose();
				BlockPos blockpos = BlockPos.containing(entity.getX(), entity.getBoundingBox().maxY, entity.getZ());
				matrixStackIn.translate(0D, 0.5D, 0D);
				float rot = (entity.tickCount * 8) % 360;
				matrixStackIn.rotateAround(Axis.XP.rotationDegrees(rot + partialTicks), 1F, 0F, 0F);
				matrixStackIn.rotateAround(Axis.YP.rotationDegrees(rot + partialTicks), 0F, 1F, 0F);
				matrixStackIn.translate(-0.5D, -0.5D, -0.5D);
				var model = this.dispatcher.getBlockModel(blockstate);
				for (var renderType : model.getRenderTypes(blockstate, RandomSource.create(blockstate.getSeed(entity.blockPosition())), net.neoforged.neoforge.client.model.data.ModelData.EMPTY))
					this.dispatcher.getModelRenderer().tesselateBlock(
							level,
							model,
							blockstate,
							blockpos,
							matrixStackIn,
							bufferIn.getBuffer(renderType),
							false,
							RandomSource.create(),
							blockstate.getSeed(entity.blockPosition()),
							getOverlayCoords(entity, 0F),
							net.neoforged.neoforge.client.model.data.ModelData.EMPTY,
							renderType
					);
				matrixStackIn.popPose();
				super.render(entity, yaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
			}
		}
	}

	private int getOverlayCoords(T entity, float pU) {
		return OverlayTexture.pack(OverlayTexture.u(pU), OverlayTexture.v(entity.hurtTime > 0));
	}

	@Override
	public ResourceLocation getTextureLocation(T entityIn) {
		return InventoryMenu.BLOCK_ATLAS;
	}

}
