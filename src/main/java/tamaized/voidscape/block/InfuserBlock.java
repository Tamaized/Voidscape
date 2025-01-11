package tamaized.voidscape.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import tamaized.beanification.Autowired;
import tamaized.beanification.Configurable;
import tamaized.voidscape.block.entity.InfuserBlockEntity;
import tamaized.voidscape.registry.blockentity.ModBlockEntities;

@SuppressWarnings("deprecation")
@Configurable
public class InfuserBlock extends Block implements EntityBlock {

	@Autowired
	private ModBlockEntities blockEntities;

    public InfuserBlock(Properties pProperties) {
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
        return new InfuserBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> entity) {
        return blockEntities.INFUSER.get() == entity && !level.isClientSide() ? InfuserBlockEntity::tick : null;
    }

}
