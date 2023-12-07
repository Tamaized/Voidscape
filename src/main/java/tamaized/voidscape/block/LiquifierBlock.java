package tamaized.voidscape.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;
import tamaized.voidscape.block.entity.LiquifierBlockEntity;
import tamaized.voidscape.registry.ModBlockEntities;

@SuppressWarnings("deprecation")
public class LiquifierBlock extends Block implements EntityBlock {

	public LiquifierBlock(Properties pProperties) {
		super(pProperties);
	}

	@Override
	@Deprecated
	public boolean triggerEvent(BlockState pState, Level pLevel, BlockPos pPos, int pId, int pParam) {
		BlockEntity be = pLevel.getBlockEntity(pPos);
		return super.triggerEvent(pState, pLevel, pPos, pId, pParam) || (be != null && be.triggerEvent(pId, pParam));
	}

	@Override
	@Deprecated
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			BlockEntity be = level.getBlockEntity(pos);
			if (be instanceof LiquifierBlockEntity) {
				IItemHandler items = level.getCapability(Capabilities.ItemHandler.BLOCK, pos, null);
				if (items != null)
					Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), items.getStackInSlot(0));
			}
			super.onRemove(state, level, pos, newState, isMoving);
		}
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new LiquifierBlockEntity(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> entity) {
		return ModBlockEntities.LIQUIFIER.get() == entity && !level.isClientSide() ? LiquifierBlockEntity::tick : null;
	}

}
