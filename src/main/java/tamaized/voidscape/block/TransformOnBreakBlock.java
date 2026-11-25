package tamaized.voidscape.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.common.CommonHooks;
import org.jetbrains.annotations.Nullable;
import tamaized.beanification.Autowired;
import tamaized.beanification.Configurable;
import tamaized.voidscape.event.LastHitBlockFaceTracker;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@Configurable
public class TransformOnBreakBlock extends Block {

	@Autowired
	private LastHitBlockFaceTracker lastHitBlockFaceTracker;

	private final Supplier<BlockState> to;

	public TransformOnBreakBlock(Supplier<BlockState> to, Properties properties) {
		super(properties);
		this.to = to;
	}

	@Override
	public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, ItemStack toolStack, boolean willHarvest, FluidState fluid) {
		boolean flag = super.onDestroyedByPlayer(state, level, pos, player, toolStack, willHarvest, fluid);
		level.setBlock(pos, to.get(), level.isClientSide() ? 11 : 3);
		return flag;
	}

	@Override
	public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack destroyedWith) {
		Optional<Direction> face = lastHitBlockFaceTracker.get(player);
		if (face.isEmpty() || !(level instanceof ServerLevel serverLevel)) {
			super.playerDestroy(level, player, pos, state, blockEntity, destroyedWith);
			return;
		}
		// Copy from super
		player.awardStat(Stats.BLOCK_MINED.get(this));
		player.causeFoodExhaustion(0.005F);
		// End Copy
		List<ItemEntity> drops = new ArrayList<>();
		if (serverLevel.getGameRules().get(GameRules.BLOCK_DROPS) && !serverLevel.restoringBlockSnapshots) {
			for (ItemStack stack : getDrops(state, serverLevel, pos, blockEntity, player, destroyedWith)) {
				if (!stack.isEmpty())
					drops.add(createDropFromFace(serverLevel, pos, face.get(), stack));
			}
		}
		CommonHooks.handleBlockDrops(serverLevel, pos, state, blockEntity, drops, player, destroyedWith);
	}

	private ItemEntity createDropFromFace(ServerLevel level, BlockPos pos, Direction face, ItemStack stack) {
		int stepX = face.getStepX();
		int stepY = face.getStepY();
		int stepZ = face.getStepZ();
		double halfWidth = EntityType.ITEM.getWidth() / 2.0;
		double halfHeight = EntityType.ITEM.getHeight() / 2.0;
		RandomSource random = level.getRandom();
		double x = pos.getX() + 0.5 + (stepX == 0 ? Mth.nextDouble(random, -0.25, 0.25) : stepX * (0.5 + halfWidth));
		double y = pos.getY() + 0.5 + (stepY == 0 ? Mth.nextDouble(random, -0.25, 0.25) : stepY * (0.5 + halfHeight)) - halfHeight;
		double z = pos.getZ() + 0.5 + (stepZ == 0 ? Mth.nextDouble(random, -0.25, 0.25) : stepZ * (0.5 + halfWidth));
		double deltaX = stepX == 0 ? Mth.nextDouble(random, -0.1, 0.1) : stepX * 0.1;
		double deltaY = stepY == 0 ? Mth.nextDouble(random, 0.0, 0.1) : stepY * 0.1 + 0.1;
		double deltaZ = stepZ == 0 ? Mth.nextDouble(random, -0.1, 0.1) : stepZ * 0.1;
		ItemEntity entity = new ItemEntity(level, x, y, z, stack, deltaX, deltaY, deltaZ);
		entity.setDefaultPickUpDelay();
		return entity;
	}

}
