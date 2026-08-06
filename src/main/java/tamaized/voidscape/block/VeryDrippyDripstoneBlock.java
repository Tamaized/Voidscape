package tamaized.voidscape.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.Fallable;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import tamaized.beanification.Autowired;
import tamaized.beanification.Configurable;
import tamaized.voidscape.block.entity.VeryDrippyDripstoneBlockEntity;
import tamaized.voidscape.registry.blockentity.ModBlockEntities;

@Configurable
public class VeryDrippyDripstoneBlock extends Block implements EntityBlock, Fallable {

	private static final VoxelShape TIP_SHAPE_DOWN = Block.box(5.0, 5.0, 5.0, 11.0, 16.0, 11.0);

	@Autowired
	private ModBlockEntities blockEntities;

	public VeryDrippyDripstoneBlock(Properties pProperties) {
		super(pProperties);
	}

	@Override
	@Deprecated
	public boolean triggerEvent(BlockState pState, Level pLevel, BlockPos pPos, int pId, int pParam) {
		BlockEntity be = pLevel.getBlockEntity(pPos);
		return super.triggerEvent(pState, pLevel, pPos, pId, pParam) || (be != null && be.triggerEvent(pId, pParam));
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new VeryDrippyDripstoneBlockEntity(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> entity) {
		return blockEntities.VERY_DRIPPY_DRIPSTONE.get() == entity && !level.isClientSide() ? VeryDrippyDripstoneBlockEntity::tick : null;
	}

	@Override
	public void onBrokenAfterFall(Level pLevel, BlockPos pPos, FallingBlockEntity pFallingBlock) {
		if (!pFallingBlock.isSilent()) {
			pLevel.levelEvent(LevelEvent.SOUND_POINTED_DRIPSTONE_LAND, pPos, 0);
		}
	}

	@Override
	public DamageSource getFallDamageSource(Entity pEntity) {
		return pEntity.damageSources().fallingStalactite(pEntity);
	}

	@Override
	public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
		return level.getBlockState(pos.above()).isFaceSturdy(level, pos.above(), Direction.DOWN);
	}

	@Override
	protected VoxelShape getOcclusionShape(BlockState state) {
		return Shapes.empty();
	}

	@Override
	protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		Vec3 vec3 = state.getOffset(pos);
		return TIP_SHAPE_DOWN.move(vec3.x, 0.0, vec3.z);
	}

	@Override
	protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
		if (!this.canSurvive(state, level, pos)) {
			ticks.scheduleTick(pos, this, 1);
		}
		return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
	}

	@Override
	public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
		if (!this.canSurvive(pState, pLevel, pPos)) {
			FallingBlockEntity fallingblockentity = FallingBlockEntity.fall(pLevel, pPos, pState);
			fallingblockentity.setHurtsEntities(6, 40);
		}
	}

	@Override
	public boolean isCollisionShapeFullBlock(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
		return false;
	}

	@Override
	public float getMaxHorizontalOffset() {
		return 0.125F;
	}

	@Override
	protected boolean isPathfindable(BlockState state, PathComputationType pathComputationType) {
		return false;
	}

}
