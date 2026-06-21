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
import tamaized.regutil.RegUtil;
import tamaized.regutil.item.BreakableHelper;
import tamaized.regutil.item.BreakableTool;
import tamaized.voidscape.util.tool.MultiBlockBreak;

import java.util.List;
import java.util.function.Consumer;

@Configurable
public class BreakableWarhammer extends BreakableTool {

	@Autowired
	private MultiBlockBreak multiBlockBreak;

	private final Consumer<RegUtil.ToolAndArmorHelper.TooltipContext> tooltipConsumer;

	public BreakableWarhammer(ToolMaterial material, Item.Properties properties, Consumer<ExtraTooltipContext> tooltipConsumer) {
		super(tier, properties.attributes(PickaxeItem.createAttributes(tier, 7, -3.5F)));
		this.tooltipConsumer = tooltipConsumer;
	}

	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
		BreakableHelper.appendHoverText(stack, context, tooltipComponents, tooltipFlag, tooltipConsumer);
		super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		return BreakableHelper.useOn(context, () -> super.useOn(context));
	}

	@Override
	public boolean canAttackBlock(BlockState state, Level level, BlockPos pos, Player player) {
		return multiBlockBreak.doBreak(multiBlockBreak.THREE_BY_THREE_RADIUS, player.getMainHandItem(), pos, player, () -> super.canAttackBlock(state, level, pos, player));
	}
}
