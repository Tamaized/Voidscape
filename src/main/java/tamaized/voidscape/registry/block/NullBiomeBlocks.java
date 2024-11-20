package tamaized.voidscape.registry.block;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.regutil.RegUtil;
import tamaized.voidscape.registry.ModItemProperties;

import java.util.function.Supplier;

@Component
public class NullBiomeBlocks {

	private final DeferredRegister<Block> REGISTRY = RegUtil.create(Registries.BLOCK);
	private final DeferredRegister<Item> REGISTRY_ITEM = RegUtil.create(Registries.ITEM);

	@Autowired
	private ModItemProperties itemProperties;

	public final DeferredHolder<Block, Block> NULL_BLACK = REGISTRY.register("null_black", () -> new Block(Block.Properties.of()
		.sound(SoundType.AMETHYST)
		.mapColor(MapColor.COLOR_BLACK)
		.strength(-1.0F, 3600000.0F)
		.noLootTable()
		.isValidSpawn((p_test_1_, p_test_2_, p_test_3_, p_test_4_) -> true)
	));
	public final Supplier<Item> NULL_BLACK_ITEM = REGISTRY_ITEM.register(NULL_BLACK.getId().getPath(), () -> new BlockItem(
		NULL_BLACK.get(),
		itemProperties.DEFAULT.get()
	));

	public final DeferredHolder<Block, Block> NULL_WHITE = REGISTRY.register("null_white", () -> new Block(Block.Properties.of()
		.sound(SoundType.AMETHYST)
		.mapColor(MapColor.COLOR_BLACK)
		.strength(-1.0F, 3600000.0F)
		.noLootTable()
		.isValidSpawn((p_test_1_, p_test_2_, p_test_3_, p_test_4_) -> true)
	));
	public final Supplier<Item> NULL_WHITE_ITEM = REGISTRY_ITEM.register(NULL_WHITE.getId().getPath(), () -> new BlockItem(
		NULL_WHITE.get(),
		itemProperties.DEFAULT.get()
	));

}
