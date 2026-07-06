package tamaized.voidscape.item.tool;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.phys.BlockHitResult;
import tamaized.beanification.Autowired;
import tamaized.beanification.Configurable;
import tamaized.regutil.ExtraTooltipContext;
import tamaized.regutil.item.BreakableHelper;
import tamaized.regutil.item.BreakableHoe;
import tamaized.voidscape.registry.ModAdvancementTriggers;

import java.util.function.Consumer;

@Configurable
public class BonemealHoe extends BreakableHoe {

	@Autowired
	private ModAdvancementTriggers advancementTriggers;

	@Autowired
	private BreakableHelper breakableHelper;

	public BonemealHoe(ToolMaterial material, Item.Properties properties, Consumer<ExtraTooltipContext> tooltipConsumer) {
		super(material, properties, tooltipConsumer);
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		return breakableHelper.useOn(context, () -> {
			InteractionResult result = context.getPlayer() != null && context.getPlayer().isShiftKeyDown() ? InteractionResult.PASS : super.useOn(context);
			if (result == InteractionResult.PASS) {
				result = Items.BONE_MEAL.useOn(new UseOnContext(
					context.getLevel(),
					context.getPlayer(),
					context.getHand(),
					new ItemStack(Items.BONE_MEAL),
					new BlockHitResult(
						context.getClickLocation(),
						context.getHorizontalDirection(),
						context.getClickedPos(),
						context.isInside()
					)));
				if ((result == InteractionResult.SUCCESS || result == InteractionResult.CONSUME) && context.getPlayer() != null) {
					context.getItemInHand().hurtAndBreak(20, context.getPlayer(), EquipmentSlot.MAINHAND);
					if (context.getPlayer() instanceof ServerPlayer player)
						advancementTriggers.HOE_BONEMEAL_TRIGGER.get().trigger(player);
				}
				return result;
			}
			return result;
		});
	}
}
