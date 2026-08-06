package tamaized.voidscape.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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
	public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, ItemStack toolStack, boolean willHarvest, FluidState fluid) {
		boolean flag = super.onDestroyedByPlayer(state, level, pos, player, toolStack, willHarvest, fluid);
		level.setBlock(pos, to.get(), level.isClientSide() ? 11 : 3);
		return flag;
	}

}
