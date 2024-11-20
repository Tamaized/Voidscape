package tamaized.voidscape.registry.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredRegister;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.regutil.RegUtil;
import tamaized.voidscape.registry.ModItemProperties;

import java.util.function.Supplier;

@Component
public class AugmentItems {

	@Autowired
	private ModItemProperties itemProperties;

	private final DeferredRegister<Item> REGISTRY = RegUtil.create(Registries.ITEM);

	public final Supplier<Item> VOIDIC_TEMPLATE = REGISTRY.register("voidic_template", () -> new Item(itemProperties.LAVA_IMMUNE.get()));

	public final Supplier<Item> ETHEREAL_SPIDER_FANG = REGISTRY.register("ethereal_spider_fang", () -> new Item(itemProperties.LAVA_IMMUNE.get()));

}
