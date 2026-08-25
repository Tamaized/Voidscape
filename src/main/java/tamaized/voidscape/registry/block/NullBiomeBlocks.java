package tamaized.voidscape.registry.block;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
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
public class NullBiomeBlocks {

	@Autowired
	private ModItemProperties itemProperties;

	public final DeferredHolder<Block, Block> NULL_BLACK = RegUtil.register(Registries.BLOCK, "null_black",
		(id) -> new Block(Block.Properties.of()
			.setId(ResourceKey.create(Registries.BLOCK, id))
			.sound(SoundType.AMETHYST)
			.mapColor(MapColor.COLOR_BLACK)
			.strength(-1.0F, 3600000.0F)
			.noLootTable()
			.isValidSpawn((_, _, _, _) -> true)
		)
	);
	public final Supplier<Item> NULL_BLACK_ITEM = RegUtil.register(Registries.ITEM, NULL_BLACK.getId().getPath(),
		() -> new BlockItem(
			NULL_BLACK.get(),
			itemProperties.DEFAULT.get()
		)
	);

	public final DeferredHolder<Block, Block> NULL_WHITE = RegUtil.register(Registries.BLOCK, "null_white",
		(id) -> new Block(Block.Properties.of()
			.setId(ResourceKey.create(Registries.BLOCK, id))
			.sound(SoundType.AMETHYST)
			.mapColor(MapColor.COLOR_BLACK)
			.strength(-1.0F, 3600000.0F)
			.noLootTable()
			.isValidSpawn((_, _, _, _) -> true)
		)
	);
	public final Supplier<Item> NULL_WHITE_ITEM = RegUtil.register(Registries.ITEM, NULL_WHITE.getId().getPath(),
		() -> new BlockItem(
			NULL_WHITE.get(),
			itemProperties.DEFAULT.get()
		)
	);

}
