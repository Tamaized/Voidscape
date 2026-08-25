package tamaized.voidscape.registry.tool;

import net.minecraft.resources.Identifier;
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
import java.util.function.Function;
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
		Function<Identifier, Item.Properties> properties,
		AttributeFactory factory,
		Consumer<ExtraTooltipContext> tooltipConsumer
	) {
		return toolAndArmorHelper.gear("warhammer", baseName, factory, (id) -> new LootingWarhammer(material.get(), properties.apply(id), tooltipConsumer));
	}

	public DeferredHolder<Item, Item> hoeWithBonemeal(
		String baseName,
		Supplier<ToolMaterial> material,
		Function<Identifier, Item.Properties> properties,
		AttributeFactory factory,
		Consumer<ExtraTooltipContext> tooltipConsumer
	) {
		return toolAndArmorHelper.gear("hoe", baseName, factory, (id) -> new BonemealHoe(material.get(), properties.apply(id), tooltipConsumer));
	}

	public DeferredHolder<Item, Item> shovelThreeByThree(
		String baseName,
		Supplier<ToolMaterial> material,
		Function<Identifier, Item.Properties> properties,
		AttributeFactory factory,
		Consumer<ExtraTooltipContext> tooltipConsumer
	) {
		return toolAndArmorHelper.gear("shovel", baseName, factory, (id) -> new ThreeByThreeShovel(material.get(), properties.apply(id), tooltipConsumer));
	}

}
