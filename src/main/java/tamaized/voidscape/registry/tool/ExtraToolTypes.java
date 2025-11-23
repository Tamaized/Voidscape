package tamaized.voidscape.registry.tool;

import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import tamaized.beanification.Component;
import tamaized.regutil.AttributeFactory;
import tamaized.regutil.RegUtil;
import tamaized.regutil.ToolTier;
import tamaized.voidscape.item.tool.BonemealHoe;
import tamaized.voidscape.item.tool.LootingWarhammer;
import tamaized.voidscape.item.tool.ThreeByThreeShovel;

import java.util.function.Consumer;
import java.util.function.Supplier;

@Component
public class ExtraToolTypes {

	public DeferredHolder<Item, Item> hammer(String baseName, Supplier<ToolTier> tier, Supplier<Item.Properties> properties, AttributeFactory factory, Consumer<RegUtil.ToolAndArmorHelper.TooltipContext> tooltipConsumer) {
		return RegUtil.ToolAndArmorHelper.gear("warhammer", baseName, factory, () -> new LootingWarhammer(tier.get(), properties.get(), tooltipConsumer));
	}

	public DeferredHolder<Item, Item> hoeWithBonemeal(String baseName, Supplier<ToolTier> tier, Supplier<Item.Properties> properties, AttributeFactory factory, Consumer<RegUtil.ToolAndArmorHelper.TooltipContext> tooltipConsumer) {
		return RegUtil.ToolAndArmorHelper.gear("hoe", baseName, factory, () -> new BonemealHoe(tier.get(), properties.get(), tooltipConsumer));
	}

	public DeferredHolder<Item, Item> shovelThreeByThree(String baseName, Supplier<ToolTier> tier, Supplier<Item.Properties> properties, AttributeFactory factory, Consumer<RegUtil.ToolAndArmorHelper.TooltipContext> tooltipConsumer) {
		return RegUtil.ToolAndArmorHelper.gear("shovel", baseName, factory, () -> new ThreeByThreeShovel(tier.get(), properties.get(), tooltipConsumer));
	}

}
