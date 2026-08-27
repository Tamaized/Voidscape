package tamaized.voidscape.registry.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import tamaized.beanification.Component;

@Component
public class BlockEntityTypeFactory {

	public <T extends BlockEntity> BlockEntityType<T> create(ModBlockEntities blockEntities, ExtendedBlockEntitySupplier<? extends T> factory, Block... validBlocks) {
		return new BlockEntityType<>((worldPosition, blockState) -> factory.create(blockEntities, worldPosition, blockState), validBlocks);
	}

	@FunctionalInterface
	public interface ExtendedBlockEntitySupplier<T extends BlockEntity> {
		T create(ModBlockEntities blockEntities, BlockPos worldPosition, BlockState blockState);
	}

}
