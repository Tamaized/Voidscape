package tamaized.voidscape.registry.fluid;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.registries.DeferredHolder;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.regutil.RegUtil;
import tamaized.voidscape.registry.ModItemProperties;

@Component
public class ModFluidBuckets {

	@Autowired
	private ModItemProperties itemProperties;

	@Autowired
	private ModFluids fluids;

	public final DeferredHolder<Item, Item> VOIDIC = RegUtil.register(Registries.ITEM, "voidic_bucket", (id) -> new BucketItem(
		fluids.VOIDIC_SOURCE.get(), itemProperties.DEFAULT.apply(id).stacksTo(1).craftRemainder(Items.BUCKET)
	));

}
