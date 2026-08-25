package tamaized.voidscape.registry.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.regutil.RegUtil;
import tamaized.voidscape.registry.ModItemProperties;

@Component
public class AugmentItems {

	@Autowired
	private ModItemProperties itemProperties;

	public final DeferredHolder<Item, Item> VOIDIC_TEMPLATE = RegUtil.register(Registries.ITEM, "voidic_template", (id) -> new Item(
		itemProperties.LAVA_IMMUNE.apply(id)
	));

	public final DeferredHolder<Item, Item> ETHEREAL_SPIDER_FANG = RegUtil.register(Registries.ITEM, "ethereal_spider_fang", (id) -> new Item(
		itemProperties.LAVA_IMMUNE.apply(id)
	));

}
