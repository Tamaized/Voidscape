package tamaized.voidscape.item.tool;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import tamaized.beanification.Autowired;
import tamaized.beanification.Configurable;
import tamaized.regutil.RegUtil;
import tamaized.regutil.item.BreakableShovel;
import tamaized.voidscape.util.tool.MultiBlockBreak;

import java.util.function.Consumer;

@Configurable
public class ThreeByThreeShovel extends BreakableShovel {

	@Autowired
	private MultiBlockBreak multiBlockBreak;

	public ThreeByThreeShovel(Tier tier, Properties properties, Consumer<RegUtil.ToolAndArmorHelper.TooltipContext> tooltipConsumer) {
		super(tier, properties, tooltipConsumer);
	}

	@Override
	public boolean canAttackBlock(BlockState state, Level level, BlockPos pos, Player player) {
		return multiBlockBreak.doBreak(multiBlockBreak.THREE_BY_THREE_RADIUS, player.getMainHandItem(), pos, player, () -> super.canAttackBlock(state, level, pos, player));
	}
}
