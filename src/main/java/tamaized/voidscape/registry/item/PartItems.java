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
public class PartItems {

	@Autowired
	private ModItemProperties itemProperties;

	private final DeferredRegister<Item> REGISTRY = RegUtil.create(Registries.ITEM);

	public final DeferredHolder<Item, Item> CHARRED_WARHAMMER_HEAD = REGISTRY.register("charred_warhammer_head", () -> new Item(itemProperties.LAVA_IMMUNE.get()));

}
