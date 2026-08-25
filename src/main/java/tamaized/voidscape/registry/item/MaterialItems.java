package tamaized.voidscape.registry.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.regutil.RegUtil;
import tamaized.voidscape.item.BlockTransformerItem;
import tamaized.voidscape.item.StrangePearlThrowableItem;
import tamaized.voidscape.item.VoidPortalActivatorItem;
import tamaized.voidscape.registry.ModItemProperties;
import tamaized.voidscape.registry.block.SpireBlocks;

@Component
public class MaterialItems {

	@Autowired
	private SpireBlocks spireBlocks;

	@Autowired
	private ModItemProperties itemProperties;

	public final DeferredHolder<Item, Item> VOIDIC_CRYSTAL = RegUtil.register(Registries.ITEM, "voidic_crystal", (id) -> new Item(
		itemProperties.LAVA_IMMUNE.apply(id)
	));

	public final DeferredHolder<Item, Item> CHARRED_BONE = RegUtil.register(Registries.ITEM, "charred_bone", (id) -> new VoidPortalActivatorItem(
		itemProperties.LAVA_IMMUNE.apply(id)
	));

	public final DeferredHolder<Item, Item> TENDRIL = RegUtil.register(Registries.ITEM, "tendril", (id) -> new Item(
		itemProperties.LAVA_IMMUNE.apply(id)
	));

	public final DeferredHolder<Item, Item> TITANITE_CHUNK = RegUtil.register(Registries.ITEM, "titanite_chunk", (id) -> new Item(
		itemProperties.LAVA_IMMUNE.apply(id)
	));

	public final DeferredHolder<Item, Item> TITANITE_SHARD = RegUtil.register(Registries.ITEM, "titanite_shard", (id) -> new Item(
		itemProperties.LAVA_IMMUNE.apply(id)
	));

	public final DeferredHolder<Item, Item> FLESH_CHUNK = RegUtil.register(Registries.ITEM, "flesh_chunk", (id) -> new Item(
		itemProperties.LAVA_IMMUNE.apply(id)
	));

	public final DeferredHolder<Item, Item> ICHOR = RegUtil.register(Registries.ITEM, "ichor", (id) -> new Item(
		itemProperties.LAVA_IMMUNE.apply(id)
	));

	public final DeferredHolder<Item, Item> ICHOR_CRYSTAL = RegUtil.register(Registries.ITEM, "ichor_crystal", (id) -> new Item(
		itemProperties.LAVA_IMMUNE.apply(id)
	));

	public final DeferredHolder<Item, Item> STRANGE_PEARL = RegUtil.register(Registries.ITEM, "strange_pearl", (id) -> new StrangePearlThrowableItem(
		itemProperties.LAVA_IMMUNE.apply(id)
	));

	public final DeferredHolder<Item, Item> ASTRAL_SHARDS = RegUtil.register(Registries.ITEM, "astral_shards", (id) -> new BlockTransformerItem.Builder(
		state -> state.is(spireBlocks.ANTIROCK),
		() -> spireBlocks.ASTRALROCK.get().defaultBlockState()
	).build(itemProperties.LAVA_IMMUNE.apply(id)));

	public final DeferredHolder<Item, Item> ASTRAL_ESSENCE = RegUtil.register(Registries.ITEM, "astral_essence", (id) -> new Item(
		itemProperties.LAVA_IMMUNE.apply(id)
	));

	public final DeferredHolder<Item, Item> ASTRAL_CRYSTAL = RegUtil.register(Registries.ITEM, "astral_crystal", (id) -> new Item(
		itemProperties.LAVA_IMMUNE.apply(id)
	));

}
