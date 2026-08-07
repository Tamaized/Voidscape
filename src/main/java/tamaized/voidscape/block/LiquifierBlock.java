package tamaized.voidscape.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.LivingEntity;
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
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jetbrains.annotations.Nullable;
import tamaized.beanification.Autowired;
import tamaized.beanification.Configurable;
import tamaized.voidscape.block.entity.LiquifierBlockEntity;
import tamaized.voidscape.registry.blockentity.ModBlockEntities;
import tamaized.voidscape.registry.fluid.ModFluidBuckets;
import tamaized.voidscape.registry.fluid.ModFluids;
import tamaized.voidscape.util.SimpleBlockEntityTickerFactory;
import tamaized.voidscape.util.SingleResourceCapabilityUtil;

import java.util.Optional;

@Configurable
public class LiquifierBlock extends Block implements EntityBlock, BucketPickup {

	@Autowired
	private ModBlockEntities blockEntities;

	@Autowired
	private ModFluids fluids;

	@Autowired
	private ModFluidBuckets buckets;

	@Autowired
	private SimpleBlockEntityTickerFactory simpleBlockEntityTickerFactory;

	@Autowired
	private SingleResourceCapabilityUtil singleResourceCapabilityUtil;

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
	protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston) {
		super.affectNeighborsAfterRemoval(state, level, pos, movedByPiston);
		Containers.updateNeighboursAfterDestroy(state, level, pos);
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new LiquifierBlockEntity(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return simpleBlockEntityTickerFactory.makeCasted(blockEntities.LIQUIFIER.get(), level, type);
	}

	@Override
	public ItemStack pickupBlock(@Nullable LivingEntity user, LevelAccessor level, BlockPos pos, BlockState state) {
		BlockEntity be = level.getBlockEntity(pos);
		if (be instanceof LiquifierBlockEntity entity && singleResourceCapabilityUtil.amount(entity.fluids) >= 1000) {
			try (Transaction transaction = Transaction.openRoot()) {
				singleResourceCapabilityUtil.extract(entity.fluids, 1000, transaction);
				transaction.commit();
			}
			return new ItemStack(buckets.VOIDIC.get());
		}
		return ItemStack.EMPTY;
	}

	@Override
	public Optional<SoundEvent> getPickupSound() {
		return fluids.VOIDIC_SOURCE.get().getPickupSound();
	}

}
