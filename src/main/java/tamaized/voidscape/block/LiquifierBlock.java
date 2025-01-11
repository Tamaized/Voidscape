package tamaized.voidscape.block;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;
import tamaized.beanification.Autowired;
import tamaized.beanification.Configurable;
import tamaized.voidscape.block.entity.LiquifierBlockEntity;
import tamaized.voidscape.registry.blockentity.ModBlockEntities;
import tamaized.voidscape.registry.fluid.ModFluidBuckets;
import tamaized.voidscape.registry.fluid.ModFluids;

import java.util.Optional;

@SuppressWarnings("deprecation")
@Configurable
public class LiquifierBlock extends Block implements EntityBlock, BucketPickup {

	@Autowired
	private ModBlockEntities blockEntities;

	@Autowired
	private ModFluids fluids;

	@Autowired
	private ModFluidBuckets buckets;

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
		return blockEntities.LIQUIFIER.get() == entity && !level.isClientSide() ? LiquifierBlockEntity::tick : null;
	}

	@Override
	public ItemStack pickupBlock(@Nullable Player player, LevelAccessor level, BlockPos pos, BlockState state) {
		BlockEntity be = level.getBlockEntity(pos);
		if (be instanceof LiquifierBlockEntity entity && entity.fluids.getFluidAmount() >= 1000) {
			entity.fluids.drain(1000, IFluidHandler.FluidAction.EXECUTE);
			return new ItemStack(buckets.VOIDIC.get());
		}
		return ItemStack.EMPTY;
	}

	@Override
	public Optional<SoundEvent> getPickupSound() {
		return fluids.VOIDIC_SOURCE.get().getPickupSound();
	}

}
