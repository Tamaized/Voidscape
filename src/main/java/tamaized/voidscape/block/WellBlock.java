package tamaized.voidscape.block;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.Nullable;
import tamaized.beanification.Autowired;
import tamaized.beanification.Configurable;
import tamaized.voidscape.block.entity.WellBlockEntity;
import tamaized.voidscape.registry.blockentity.ModBlockEntities;
import tamaized.voidscape.util.SimpleBlockEntityTickerFactory;

import java.util.Optional;

@Configurable
public class WellBlock extends Block implements EntityBlock, BucketPickup {

	@Autowired
	private ModBlockEntities blockEntities;

	@Autowired
	private SimpleBlockEntityTickerFactory simpleBlockEntityTickerFactory;

    public WellBlock(Properties pProperties) {
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
        return new WellBlockEntity(pos, state);
    }

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return simpleBlockEntityTickerFactory.makeCasted(blockEntities.WELL.get(), level, type);
	}

	@Override
	public ItemStack pickupBlock(@Nullable LivingEntity user, LevelAccessor level, BlockPos pos, BlockState state) {
		return new ItemStack(Items.WATER_BUCKET);
	}

	@Override
	@Deprecated
	public Optional<SoundEvent> getPickupSound() {
		return Fluids.WATER.getPickupSound();
	}
}
