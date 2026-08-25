package tamaized.voidscape.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import tamaized.beanification.Component;

import java.util.function.Function;

@Component
public class ModItemProperties {

	public final Function<Identifier, Item.Properties> DEFAULT = (id) -> new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id));

	public final Function<Identifier, Item.Properties> LAVA_IMMUNE = (id) -> DEFAULT.apply(id).fireResistant();

	public final Function<Identifier, Item.Properties> ETHEREAL_FRUIT = (id) -> LAVA_IMMUNE.apply(id).food(new FoodProperties.Builder()
		.nutrition(4)
		.saturationModifier(0.3F)
		.alwaysEdible()
		.build());

}
