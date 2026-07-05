package tamaized.voidscape.registry.block;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredHolder;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.regutil.RegUtil;
import tamaized.voidscape.registry.ModItemProperties;

import java.util.function.Supplier;

@Component
public class MaterialBlocks {

	@Autowired
	private ModItemProperties itemProperties;

	public final DeferredHolder<Block, Block> VOIDIC_CRYSTAL_BLOCK = RegUtil.register(Registries.BLOCK, "voidic_crystal_block",
		() -> new Block(Block.Properties.of()
			.sound(SoundType.AMETHYST)
			.mapColor(MapColor.COLOR_PURPLE)
			.strength(3F, 1200.0F)
			.requiresCorrectToolForDrops()
		)
	);
	public final Supplier<Item> VOIDIC_CRYSTAL_BLOCK_ITEM = RegUtil.register(Registries.ITEM, VOIDIC_CRYSTAL_BLOCK.getId().getPath(),
		() -> new BlockItem(
			VOIDIC_CRYSTAL_BLOCK.get(),
			itemProperties.LAVA_IMMUNE.get()
		)
	);

	public final DeferredHolder<Block, Block> CHARRED_BRICK = RegUtil.register(Registries.BLOCK, "charred_brick",
		() -> new Block(Block.Properties.of()
			.sound(SoundType.NETHER_BRICKS)
			.mapColor(MapColor.COLOR_BLACK)
			.strength(3F, 1200.0F)
			.requiresCorrectToolForDrops()
		)
	);
	public final Supplier<Item> CHARRED_BRICK_ITEM = RegUtil.register(Registries.ITEM, CHARRED_BRICK.getId().getPath(),
		() -> new BlockItem(
			CHARRED_BRICK.get(),
			itemProperties.LAVA_IMMUNE.get()
		)
	);

	public final DeferredHolder<Block, Block> FLESH_BLOCK = RegUtil.register(Registries.BLOCK, "flesh_block",
		() -> new Block(Block.Properties.of()
			.sound(SoundType.HONEY_BLOCK)
			.mapColor(MapColor.COLOR_ORANGE)
			.strength(2F, 6F)
		)
	);
	public final Supplier<Item> FLESH_BLOCK_ITEM = RegUtil.register(Registries.ITEM, FLESH_BLOCK.getId().getPath(),
		() -> new BlockItem(
			FLESH_BLOCK.get(),
			itemProperties.LAVA_IMMUNE.get()
		)
	);

}
