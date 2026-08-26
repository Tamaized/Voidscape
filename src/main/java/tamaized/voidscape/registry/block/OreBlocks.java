package tamaized.voidscape.registry.block;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredHolder;
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

	public final DeferredHolder<Block, Block> VOIDIC_CRYSTAL_ORE = RegUtil.register(Registries.BLOCK, "voidic_crystal_ore",
		(id) -> new TransformOnBreakBlock(
			Blocks.BEDROCK::defaultBlockState,
			Block.Properties.of()
			.setId(ResourceKey.create(Registries.BLOCK, id))
				.sound(SoundType.STONE)
				.mapColor(MapColor.COLOR_BLACK)
				.strength(3F, 3600000.0F)
				.requiresCorrectToolForDrops()
		)
	);
	public final Supplier<Item> VOIDIC_CRYSTAL_ORE_ITEM = RegUtil.register(Registries.ITEM, VOIDIC_CRYSTAL_ORE.getId().getPath(),
		(id) -> new BlockItem(
			VOIDIC_CRYSTAL_ORE.get(),
			itemProperties.BLOCK_LAVA_IMMUNE.apply(id)
		)
	);

	public final DeferredHolder<Block, Block> TITANITE_ORE = RegUtil.register(Registries.BLOCK, "titanite_ore",
		(id) -> new RequiresVoidToolBlock(Block.Properties.of()
			.setId(ResourceKey.create(Registries.BLOCK, id))
			.sound(SoundType.NETHER_GOLD_ORE)
			.mapColor(MapColor.COLOR_LIGHT_GREEN)
			.strength(4F, 1200.0F)
			.requiresCorrectToolForDrops()
		)
	);
	public final Supplier<Item> TITANITE_ORE_ITEM = RegUtil.register(Registries.ITEM, TITANITE_ORE.getId().getPath(),
		(id) -> new BlockItem(
			TITANITE_ORE.get(),
			itemProperties.BLOCK_LAVA_IMMUNE.apply(id)
		)
	);

	public final DeferredHolder<Block, Block> FLESH_ORE = RegUtil.register(Registries.BLOCK, "flesh_ore",
		(id) -> new RequiresVoidToolBlock(Block.Properties.of()
			.setId(ResourceKey.create(Registries.BLOCK, id))
			.sound(SoundType.HONEY_BLOCK)
			.mapColor(MapColor.COLOR_ORANGE)
			.strength(4F, 1200.0F)
			.requiresCorrectToolForDrops()
		)
	);
	public final Supplier<Item> FLESH_ORE_ITEM = RegUtil.register(Registries.ITEM, FLESH_ORE.getId().getPath(),
		(id) -> new BlockItem(
			FLESH_ORE.get(),
			itemProperties.BLOCK_LAVA_IMMUNE.apply(id)
		)
	);

	public final DeferredHolder<Block, Block> STRANGE_ORE = RegUtil.register(Registries.BLOCK, "strange_ore",
		(id) -> new RequiresVoidToolBlock(Block.Properties.of()
			.setId(ResourceKey.create(Registries.BLOCK, id))
			.sound(SoundType.AMETHYST)
			.mapColor(MapColor.COLOR_PINK)
			.strength(4F, 1200.0F)
			.requiresCorrectToolForDrops()
		)
	);
	public final Supplier<Item> STRANGE_ORE_ITEM = RegUtil.register(Registries.ITEM, STRANGE_ORE.getId().getPath(),
		(id) -> new BlockItem(
			STRANGE_ORE.get(),
			itemProperties.BLOCK_LAVA_IMMUNE.apply(id)
		)
	);

	public final DeferredHolder<Block, Block> CRACKED_ASTRALROCK = RegUtil.register(Registries.BLOCK, "cracked_astralrock",
		(id) -> new RequiresVoidToolBlock(BlockBehaviour.Properties.of()
			.setId(ResourceKey.create(Registries.BLOCK, id))
			.sound(SoundType.AMETHYST)
			.mapColor(MapColor.COLOR_BLACK)
			.strength(4F, 1200.0F)
			.requiresCorrectToolForDrops()
		)
	);
	public final Supplier<Item> CRACKED_ASTRALROCK_ITEM = RegUtil.register(Registries.ITEM, CRACKED_ASTRALROCK.getId().getPath(),
		(id) -> new BlockItem(
			CRACKED_ASTRALROCK.get(),
			itemProperties.BLOCK_DEFAULT.apply(id)
		)
	);

}
