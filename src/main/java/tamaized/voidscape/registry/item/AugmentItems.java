package tamaized.voidscape.registry.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.regutil.RegUtil;
import tamaized.voidscape.registry.ModItemProperties;

@Component
public class AugmentItems {

	@Autowired
	private ModItemProperties itemProperties;

	public final DeferredHolder<Item, Item> VOIDIC_TEMPLATE = RegUtil.register(Registries.ITEM, "voidic_template", () -> new Item(itemProperties.LAVA_IMMUNE.get()));

	public final DeferredHolder<Item, Item> ETHEREAL_SPIDER_FANG = RegUtil.register(Registries.ITEM, "ethereal_spider_fang", () -> new Item(itemProperties.LAVA_IMMUNE.get()));

}
