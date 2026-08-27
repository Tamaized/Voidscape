package tamaized.voidscape.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.event.EventHooks;

public class VerySlowIncorrectToolHarvestBlock extends Block {

	public VerySlowIncorrectToolHarvestBlock(Properties pProperties) {
		super(pProperties);
	}

	@Override
	protected float getDestroyProgress(BlockState pState, Player pPlayer, BlockGetter pLevel, BlockPos pPos) {
		float f = pState.getDestroySpeed(pLevel, pPos);
		if (f == -1.0F) {
			return 0.0F;
		} else {
			int i = EventHooks.doPlayerHarvestCheck(pPlayer, pState, pLevel, pPos) ? 30 : 1000;
			return pPlayer.getDestroySpeed(pState, pPos) / f / (float) i;
		}
	}
}
