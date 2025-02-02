package tamaized.voidscape.registry.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredRegister;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.regutil.RegUtil;
import tamaized.voidscape.item.BlockTransformerItem;
import tamaized.voidscape.item.StrangePearlThrowableItem;
import tamaized.voidscape.item.VoidPortalActivatorItem;
import tamaized.voidscape.registry.ModItemProperties;
import tamaized.voidscape.registry.block.SpireBlocks;

import java.util.function.Supplier;

@Component
public class MaterialItems {

	@Autowired
	private SpireBlocks spireBlocks;

	@Autowired
	private ModItemProperties itemProperties;

	private final DeferredRegister<Item> REGISTRY = RegUtil.create(Registries.ITEM);

	public final Supplier<Item> VOIDIC_CRYSTAL = REGISTRY.register("voidic_crystal", () -> new Item(itemProperties.LAVA_IMMUNE.get()));

	public final Supplier<Item> CHARRED_BONE = REGISTRY.register("charred_bone", () -> new VoidPortalActivatorItem(itemProperties.LAVA_IMMUNE.get()));

	public final Supplier<Item> TENDRIL = REGISTRY.register("tendril", () -> new Item(itemProperties.LAVA_IMMUNE.get()));

	public final Supplier<Item> TITANITE_CHUNK = REGISTRY.register("titanite_chunk", () -> new Item(itemProperties.LAVA_IMMUNE.get()));

	public final Supplier<Item> TITANITE_SHARD = REGISTRY.register("titanite_shard", () -> new Item(itemProperties.LAVA_IMMUNE.get()));

	public final Supplier<Item> FLESH_CHUNK = REGISTRY.register("flesh_chunk", () -> new Item(itemProperties.LAVA_IMMUNE.get()));

	public final Supplier<Item> ICHOR = REGISTRY.register("ichor", () -> new Item(itemProperties.LAVA_IMMUNE.get()));

	public final Supplier<Item> ICHOR_CRYSTAL = REGISTRY.register("ichor_crystal", () -> new Item(itemProperties.LAVA_IMMUNE.get()));

	public final Supplier<Item> STRANGE_PEARL = REGISTRY.register("strange_pearl", () -> new StrangePearlThrowableItem(itemProperties.LAVA_IMMUNE.get()));

	public final Supplier<Item> ASTRAL_SHARDS = REGISTRY.register("astral_shards", () -> new BlockTransformerItem.Builder(
		state -> state.is(spireBlocks.ANTIROCK),
		() -> spireBlocks.ASTRALROCK.get().defaultBlockState()
	).build(itemProperties.LAVA_IMMUNE.get()));

	public final Supplier<Item> ASTRAL_ESSENCE = REGISTRY.register("astral_essence", () -> new Item(itemProperties.LAVA_IMMUNE.get()));

	public final Supplier<Item> ASTRAL_CRYSTAL = REGISTRY.register("astral_crystal", () -> new Item(itemProperties.LAVA_IMMUNE.get()));

}
