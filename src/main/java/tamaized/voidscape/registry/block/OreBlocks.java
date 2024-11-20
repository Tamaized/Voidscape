package tamaized.voidscape.registry.block;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.regutil.RegUtil;
import tamaized.voidscape.block.RequiresVoidToolBlock;
import tamaized.voidscape.block.TransformOnBreakBlock;
import tamaized.voidscape.registry.ModItemProperties;

import java.util.function.Supplier;

@Component
public class OreBlocks {

	@Autowired
	private ModItemProperties itemProperties;

	private final DeferredRegister<Block> REGISTRY = RegUtil.create(Registries.BLOCK);
	private final DeferredRegister<Item> REGISTRY_ITEM = RegUtil.create(Registries.ITEM);

	public final DeferredHolder<Block, Block> VOIDIC_CRYSTAL_ORE = REGISTRY.register("voidic_crystal_ore", () -> new TransformOnBreakBlock(
		Blocks.BEDROCK::defaultBlockState,
		Block.Properties.of()
			.sound(SoundType.STONE)
			.mapColor(MapColor.COLOR_BLACK)
			.strength(3F, 3600000.0F)
			.requiresCorrectToolForDrops()
	));
	public final Supplier<Item> VOIDIC_CRYSTAL_ORE_ITEM = REGISTRY_ITEM.register(VOIDIC_CRYSTAL_ORE.getId().getPath(), () -> new BlockItem(
		VOIDIC_CRYSTAL_ORE.get(),
		itemProperties.LAVA_IMMUNE.get()
	));

	public final DeferredHolder<Block, Block> TITANITE_ORE = REGISTRY.register("titanite_ore", () -> new RequiresVoidToolBlock(Block.Properties.of()
		.sound(SoundType.NETHER_GOLD_ORE)
		.mapColor(MapColor.COLOR_LIGHT_GREEN)
		.strength(4F, 1200.0F)
		.requiresCorrectToolForDrops()));
	public final Supplier<Item> TITANITE_ORE_ITEM = REGISTRY_ITEM.register(TITANITE_ORE.getId().getPath(), () -> new BlockItem(
		TITANITE_ORE.get(),
		itemProperties.LAVA_IMMUNE.get()
	));

	public final DeferredHolder<Block, Block> FLESH_ORE = REGISTRY.register("flesh_ore", () -> new RequiresVoidToolBlock(Block.Properties.of()
		.sound(SoundType.HONEY_BLOCK)
		.mapColor(MapColor.COLOR_ORANGE)
		.strength(4F, 1200.0F)
		.requiresCorrectToolForDrops()));
	public final Supplier<Item> FLESH_ORE_ITEM = REGISTRY_ITEM.register(FLESH_ORE.getId().getPath(), () -> new BlockItem(
		FLESH_ORE.get(),
		itemProperties.LAVA_IMMUNE.get()
	));

	public final DeferredHolder<Block, Block> STRANGE_ORE = REGISTRY.register("strange_ore", () -> new RequiresVoidToolBlock(Block.Properties.of()
		.sound(SoundType.AMETHYST)
		.mapColor(MapColor.COLOR_PINK)
		.strength(4F, 1200.0F)
		.requiresCorrectToolForDrops()));
	public final Supplier<Item> STRANGE_ORE_ITEM = REGISTRY_ITEM.register(STRANGE_ORE.getId().getPath(), () -> new BlockItem(
		STRANGE_ORE.get(),
		itemProperties.LAVA_IMMUNE.get()
	));

}
