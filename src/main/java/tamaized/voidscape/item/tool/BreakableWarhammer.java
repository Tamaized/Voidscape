package tamaized.voidscape.item.tool;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import tamaized.beanification.Autowired;
import tamaized.beanification.Configurable;
import tamaized.regutil.ExtraTooltipContext;
import tamaized.regutil.item.BreakableHelper;
import tamaized.regutil.item.BreakableTool;
import tamaized.voidscape.util.tool.MultiBlockBreak;

import java.util.function.Consumer;

@Configurable
public class BreakableWarhammer extends BreakableTool {

	@Autowired
	private MultiBlockBreak multiBlockBreak;

	@Autowired
	private BreakableHelper breakableHelper;

	public BreakableWarhammer(ToolMaterial material, Item.Properties properties, Consumer<ExtraTooltipContext> tooltipConsumer) {
		super(properties.pickaxe(material, 7, -3.5F), tooltipConsumer);
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		return breakableHelper.useOn(context, () -> super.useOn(context));
	}

	@Override
	public boolean canDestroyBlock(ItemStack itemStack, BlockState state, Level level, BlockPos pos, LivingEntity user) {
		if (!(user instanceof Player player))
			return super.canDestroyBlock(itemStack, state, level, pos, user);
		return multiBlockBreak.doBreak(
			multiBlockBreak.THREE_BY_THREE_RADIUS,
			player.getMainHandItem(),
			pos,
			player,
			() -> super.canDestroyBlock(itemStack, state, level, pos, player)
		);
	}
}
