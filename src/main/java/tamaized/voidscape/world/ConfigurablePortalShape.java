package tamaized.voidscape.world;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Predicate;

public class ConfigurablePortalShape {

	private static final int MIN_WIDTH = 2;
	public static final int MAX_WIDTH = 21;
	private static final int MIN_HEIGHT = 3;
	public static final int MAX_HEIGHT = 21;

	private final LevelAccessor level;
	private final Direction.Axis axis;
	private final BlockBehaviour.StatePredicate frameTest;
	private final BlockBehaviour.StatePredicate portalTest;
	private final BlockBehaviour.StatePredicate igniterTest;
	private final Direction rightDir;
	private int numPortalBlocks;
	private BlockPos bottomLeft;
	private int height;
	private final int width;

	public static Optional<ConfigurablePortalShape> findEmptyPortalShape(LevelAccessor levelAccessor, BlockPos blockPos, Direction.Axis axis, BlockBehaviour.StatePredicate frame, BlockBehaviour.StatePredicate portal, BlockBehaviour.StatePredicate igniter) {
		return findPortalShape(levelAccessor, blockPos, p_77727_ -> p_77727_.isValid() && p_77727_.numPortalBlocks == 0, axis, frame, portal, igniter);
	}

	public static Optional<ConfigurablePortalShape> findPortalShape(LevelAccessor levelAccessor, BlockPos blockPos, Predicate<ConfigurablePortalShape> portalShapePredicate, Direction.Axis axis, BlockBehaviour.StatePredicate frame, BlockBehaviour.StatePredicate portal, BlockBehaviour.StatePredicate igniter) {
		Optional<ConfigurablePortalShape> optional = Optional.of(new ConfigurablePortalShape(levelAccessor, blockPos, axis, frame, portal, igniter)).filter(portalShapePredicate);
		if (optional.isPresent()) {
			return optional;
		} else {
			Direction.Axis direction$axis = axis == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X;
			return Optional.of(new ConfigurablePortalShape(levelAccessor, blockPos, direction$axis, frame, portal, igniter)).filter(portalShapePredicate);
		}
	}

	public ConfigurablePortalShape(LevelAccessor levelAccessor, BlockPos blockPos, Direction.Axis axis, BlockBehaviour.StatePredicate frame, BlockBehaviour.StatePredicate portal, BlockBehaviour.StatePredicate igniter) {
		this.level = levelAccessor;
		this.axis = axis;
		this.frameTest = frame;
		this.portalTest = portal;
		this.igniterTest = igniter;
		this.rightDir = axis == Direction.Axis.X ? Direction.WEST : Direction.SOUTH;
		this.bottomLeft = this.calculateBottomLeft(blockPos);
		if (this.bottomLeft == null) {
			this.bottomLeft = blockPos;
			this.width = 1;
			this.height = 1;
		} else {
			this.width = this.calculateWidth();
			if (this.width > 0) {
				this.height = this.calculateHeight();
			}
		}
	}

	@Nullable
	private BlockPos calculateBottomLeft(BlockPos blockPos) {
		int i = Math.max(this.level.getMinBuildHeight(), blockPos.getY() - MAX_HEIGHT);

		while (blockPos.getY() > i && isEmpty(this.level.getBlockState(blockPos.below()), level, blockPos.below(), igniterTest, portalTest)) {
			blockPos = blockPos.below();
		}

		Direction direction = this.rightDir.getOpposite();
		int j = this.getDistanceUntilEdgeAboveFrame(blockPos, direction) - 1;
		return j < 0 ? null : blockPos.relative(direction, j);
	}

	private int calculateWidth() {
		int i = this.getDistanceUntilEdgeAboveFrame(this.bottomLeft, this.rightDir);
		return i >= MIN_WIDTH && i <= MAX_WIDTH ? i : 0;
	}

	private int getDistanceUntilEdgeAboveFrame(BlockPos blockPos, Direction direction) {
		BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

		for (int i = 0; i <= MAX_WIDTH; ++i) {
			blockpos$mutableblockpos.set(blockPos).move(direction, i);
			BlockState blockstate = this.level.getBlockState(blockpos$mutableblockpos);
			if (!isEmpty(blockstate, level, blockpos$mutableblockpos, igniterTest, portalTest)) {
				if (frameTest.test(blockstate, this.level, blockpos$mutableblockpos)) {
					return i;
				}
				break;
			}

			BlockState blockstate1 = this.level.getBlockState(blockpos$mutableblockpos.move(Direction.DOWN));
			if (!frameTest.test(blockstate1, this.level, blockpos$mutableblockpos)) {
				break;
			}
		}

		return 0;
	}

	private int calculateHeight() {
		BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
		int i = this.getDistanceUntilTop(blockpos$mutableblockpos);
		return i >= MIN_HEIGHT && i <= MAX_HEIGHT && this.hasTopFrame(blockpos$mutableblockpos, i) ? i : 0;
	}

	private boolean hasTopFrame(BlockPos.MutableBlockPos mutableBlockPos, int up) {
		for (int i = 0; i < this.width; ++i) {
			BlockPos.MutableBlockPos blockpos$mutableblockpos = mutableBlockPos.set(this.bottomLeft).move(Direction.UP, up).move(this.rightDir, i);
			if (!frameTest.test(this.level.getBlockState(blockpos$mutableblockpos), this.level, blockpos$mutableblockpos)) {
				return false;
			}
		}

		return true;
	}

	private int getDistanceUntilTop(BlockPos.MutableBlockPos mutableBlockPos) {
		for (int i = 0; i < MAX_HEIGHT; ++i) {
			mutableBlockPos.set(this.bottomLeft).move(Direction.UP, i).move(this.rightDir, -1);
			if (!frameTest.test(this.level.getBlockState(mutableBlockPos), this.level, mutableBlockPos)) {
				return i;
			}

			mutableBlockPos.set(this.bottomLeft).move(Direction.UP, i).move(this.rightDir, this.width);
			if (!frameTest.test(this.level.getBlockState(mutableBlockPos), this.level, mutableBlockPos)) {
				return i;
			}

			for (int j = 0; j < this.width; ++j) {
				mutableBlockPos.set(this.bottomLeft).move(Direction.UP, i).move(this.rightDir, j);
				BlockState blockstate = this.level.getBlockState(mutableBlockPos);
				if (!isEmpty(blockstate, level, mutableBlockPos, igniterTest, portalTest)) {
					return i;
				}

				if (portalTest.test(blockstate, level, mutableBlockPos)) {
					++this.numPortalBlocks;
				}
			}
		}

		return MAX_HEIGHT;
	}

	private static boolean isEmpty(BlockState state, LevelAccessor level, BlockPos pos, BlockBehaviour.StatePredicate igniter, BlockBehaviour.StatePredicate portal) {
		return state.isAir() || igniter.test(state, level, pos) || portal.test(state, level, pos);
	}

	public boolean isValid() {
		return this.bottomLeft != null && this.width >= MIN_WIDTH && this.width <= MAX_WIDTH && this.height >= MIN_HEIGHT && this.height <= MAX_HEIGHT;
	}

	public void createPortalBlocks(BlockState portal, EnumProperty<Direction.Axis> axisProperty) {
		BlockState blockstate = portal.setValue(axisProperty, this.axis);
		BlockPos.betweenClosed(this.bottomLeft, this.bottomLeft.relative(Direction.UP, this.height - 1).relative(this.rightDir, this.width - 1))
				.forEach(pos -> this.level.setBlock(pos, blockstate, 18));
	}

	public boolean isComplete() {
		return this.isValid() && this.numPortalBlocks == this.width * this.height;
	}

}
