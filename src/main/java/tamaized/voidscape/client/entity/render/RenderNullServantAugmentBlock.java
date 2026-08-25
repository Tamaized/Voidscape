package tamaized.voidscape.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import tamaized.voidscape.client.entity.render.state.NullServantAugmentBlockRenderState;
import tamaized.voidscape.entity.NullServantAugmentBlockEntity;

public class RenderNullServantAugmentBlock<T extends NullServantAugmentBlockEntity> extends EntityRenderer<T, NullServantAugmentBlockRenderState> {

	public RenderNullServantAugmentBlock(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public NullServantAugmentBlockRenderState createRenderState() {
		return new NullServantAugmentBlockRenderState();
	}

	@Override
	public void extractRenderState(T entity, NullServantAugmentBlockRenderState state, float partialTicks) {
		super.extractRenderState(entity, state, partialTicks);
		BlockState mimic = entity.getMimic();
		Level level = entity.level();
		BlockPos blockPos = entity.blockPosition();
		state.visible = mimic.getRenderShape() == RenderShape.MODEL && mimic != level.getBlockState(blockPos) && mimic.getRenderShape() != RenderShape.INVISIBLE;
		state.rotation = (entity.tickCount * 8) % 360 + partialTicks;
		state.mimic.randomSeedPos = blockPos;
		state.mimic.blockPos = BlockPos.containing(entity.getX(), entity.getBoundingBox().maxY, entity.getZ());
		state.mimic.blockState = mimic;
		if (level instanceof ClientLevel clientLevel) {
			state.mimic.biome = clientLevel.getBiome(state.mimic.blockPos);
			state.mimic.cardinalLighting = clientLevel.cardinalLighting();
			state.mimic.lightEngine = clientLevel.getLightEngine();
		}
	}

	@Override
	public void submit(NullServantAugmentBlockRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
		if (state.visible) {
			poseStack.pushPose();
			poseStack.translate(0D, 0.5D, 0D);
			poseStack.rotateAround(Axis.XP.rotationDegrees(state.rotation), 1F, 0F, 0F);
			poseStack.rotateAround(Axis.YP.rotationDegrees(state.rotation), 0F, 1F, 0F);
			poseStack.translate(-0.5D, -0.5D, -0.5D);
			submitNodeCollector.submitMovingBlock(poseStack, state.mimic);
			poseStack.popPose();
			super.submit(state, poseStack, submitNodeCollector, camera);
		}
	}

}
