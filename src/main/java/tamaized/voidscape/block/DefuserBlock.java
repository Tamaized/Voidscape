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
import tamaized.voidscape.block.entity.DefuserBlockEntity;
import tamaized.voidscape.registry.blockentity.ModBlockEntities;
import tamaized.voidscape.util.SimpleBlockEntityTickerFactory;

@Configurable
public class DefuserBlock extends Block implements EntityBlock {

	@Autowired
	private ModBlockEntities blockEntities;

	@Autowired
	private SimpleBlockEntityTickerFactory simpleBlockEntityTickerFactory;

    public DefuserBlock(Properties pProperties) {
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
        return new DefuserBlockEntity(pos, state);
    }

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return simpleBlockEntityTickerFactory.makeCasted(blockEntities.DEFUSER.get(), level, type);
	}

}
