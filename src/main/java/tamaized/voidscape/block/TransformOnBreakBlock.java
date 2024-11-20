package tamaized.voidscape.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;

import java.util.function.Supplier;

public class TransformOnBreakBlock extends Block {

	private final Supplier<BlockState> to;

	public TransformOnBreakBlock(Supplier<BlockState> to, Properties properties) {
		super(properties);
		this.to = to;
	}

	@Override
	public boolean onDestroyedByPlayer(BlockState state, Level world, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
		boolean flag = super.onDestroyedByPlayer(state, world, pos, player, willHarvest, fluid);
		world.setBlock(pos, to.get(), world.isClientSide() ? 11 : 3);
		return flag;
	}

}
