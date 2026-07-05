package tamaized.voidscape.registry.blockentity;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import tamaized.beanification.Component;

@Component
public class BlockEntityTypeFactory {

	public <T extends BlockEntity> BlockEntityType<T> create(BlockEntityType.BlockEntitySupplier<? extends T> factory, Block... validBlocks) {
		return new BlockEntityType<>(factory, validBlocks);
	}

}
