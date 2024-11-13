package tamaized.voidscape.registry;

import net.minecraft.world.item.Item;
import tamaized.beanification.Component;

import java.util.function.Supplier;

@Component
public class ModItemProperties {

	public final Supplier<Item.Properties> DEFAULT = Item.Properties::new;

	public final Supplier<Item.Properties> LAVA_IMMUNE = () -> DEFAULT.get().fireResistant();

}
