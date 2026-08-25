package tamaized.voidscape.registry.tool;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import net.neoforged.neoforge.registries.DeferredHolder;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.regutil.AttributeFactory;
import tamaized.regutil.ExtraTooltipContext;
import tamaized.regutil.ToolAndArmorHelper;
import tamaized.voidscape.item.tool.BonemealHoe;
import tamaized.voidscape.item.tool.LootingWarhammer;
import tamaized.voidscape.item.tool.ThreeByThreeShovel;

import java.util.function.Consumer;
import java.util.function.Supplier;

@Component
public class ExtraToolTypes {

	private final ToolAndArmorHelper toolAndArmorHelper;

	public ExtraToolTypes(@Autowired ToolAndArmorHelper toolAndArmorHelper) {
		this.toolAndArmorHelper = toolAndArmorHelper;
	}

	public DeferredHolder<Item, Item> hammer(
		String baseName,
		Supplier<ToolMaterial> material,
		Supplier<Item.Properties> properties,
		AttributeFactory factory,
		Consumer<ExtraTooltipContext> tooltipConsumer
	) {
		return toolAndArmorHelper.gear("warhammer", baseName, factory, () -> new LootingWarhammer(material.get(), properties.get(), tooltipConsumer));
	}

	public DeferredHolder<Item, Item> hoeWithBonemeal(
		String baseName,
		Supplier<ToolMaterial> material,
		Supplier<Item.Properties> properties,
		AttributeFactory factory,
		Consumer<ExtraTooltipContext> tooltipConsumer
	) {
		return toolAndArmorHelper.gear("hoe", baseName, factory, () -> new BonemealHoe(material.get(), properties.get(), tooltipConsumer));
	}

	public DeferredHolder<Item, Item> shovelThreeByThree(
		String baseName,
		Supplier<ToolMaterial> material,
		Supplier<Item.Properties> properties,
		AttributeFactory factory,
		Consumer<ExtraTooltipContext> tooltipConsumer
	) {
		return toolAndArmorHelper.gear("shovel", baseName, factory, () -> new ThreeByThreeShovel(material.get(), properties.get(), tooltipConsumer));
	}

}
