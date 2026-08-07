package tamaized.voidscape.util;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jetbrains.annotations.Nullable;
import tamaized.beanification.Component;
import tamaized.voidscape.block.entity.TickableBlockEntity;

@Component
public class SimpleBlockEntityTickerFactory {

	@Nullable
	public <E extends TickableBlockEntity, A extends BlockEntity> BlockEntityTicker<? super E> make(BlockEntityType<E> expectedType, Level level, BlockEntityType<A> actualType) {
		if (level.isClientSide() || expectedType != actualType)
			return null;
		return (l, pos, state, entity) -> entity.tick(l, pos, state);
	}

	@Nullable
	@SuppressWarnings("unchecked")
	public <E extends TickableBlockEntity, A extends BlockEntity> BlockEntityTicker<A> makeCasted(BlockEntityType<E> expectedType, Level level, BlockEntityType<A> actualType) {
		return (BlockEntityTicker<A>) ((BlockEntityTicker<E>) make(expectedType, level, actualType));
	}

}
