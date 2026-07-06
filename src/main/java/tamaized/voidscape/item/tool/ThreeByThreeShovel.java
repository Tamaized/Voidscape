package tamaized.voidscape.item.tool;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import tamaized.beanification.Autowired;
import tamaized.beanification.Configurable;
import tamaized.regutil.ExtraTooltipContext;
import tamaized.regutil.RegUtil;
import tamaized.regutil.item.BreakableShovel;
import tamaized.voidscape.util.tool.MultiBlockBreak;

import java.util.function.Consumer;

@Configurable
public class ThreeByThreeShovel extends BreakableShovel {

	@Autowired
	private MultiBlockBreak multiBlockBreak;

	public ThreeByThreeShovel(ToolMaterial material, Item.Properties properties, Consumer<ExtraTooltipContext> tooltipConsumer) {
		super(material, properties, tooltipConsumer);
	}

	@Override
	public boolean canDestroyBlock(ItemStack itemStack, BlockState state, Level level, BlockPos pos, LivingEntity user) {
		if (!(user instanceof Player player))
			return super.canDestroyBlock(itemStack, state, level, pos, user);
		return multiBlockBreak.doBreak(
			multiBlockBreak.THREE_BY_THREE_RADIUS,
			user.getMainHandItem(),
			pos,
			player,
			() -> super.canDestroyBlock(itemStack, state, level, pos, user)
		);
	}
}
