package tamaized.voidscape.datagen.data.loot.sub.block;

import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.RegistryProvider;
import tamaized.voidscape.datagen.util.BlockLootTableUtil;
import tamaized.voidscape.registry.ModBlockComponentDirectory;
import tamaized.voidscape.registry.ModItemComponentDirectory;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

@Component
public class EtherealFruitEndBlockLootTableFactory implements IBlockLootTable {

	@Autowired
	private RegistryProvider registries;

	@Autowired
	private BlockLootTableUtil blockLootTableUtil;

	@Autowired
	private ModBlockComponentDirectory blocks;

	@Autowired
	private ModItemComponentDirectory items;

	public void add(BlockLootSubProvider provider, BiConsumer<Block, LootTable.Builder> add, Supplier<LootItemCondition.Builder> hasSilkTouch) {
		add.accept(
			blocks.etherealFruitBlocks().VOID.get(),
			blockLootTableUtil.etherealFruit(items.etherealFruitItems().ETHEREAL_FRUIT_VOID)
		);
		add.accept(
			blocks.etherealFruitBlocks().NULL.get(),
			blockLootTableUtil.etherealFruit(items.etherealFruitItems().ETHEREAL_FRUIT_NULL)
		);
		add.accept(
			blocks.etherealFruitBlocks().OVERWORLD.get(),
			blockLootTableUtil.etherealFruit(items.etherealFruitItems().ETHEREAL_FRUIT_OVERWORLD)
		);
		add.accept(
			blocks.etherealFruitBlocks().NETHER.get(),
			blockLootTableUtil.etherealFruit(items.etherealFruitItems().ETHEREAL_FRUIT_NETHER)
		);
		add.accept(
			blocks.etherealFruitBlocks().END.get(),
			blockLootTableUtil.etherealFruit(items.etherealFruitItems().ETHEREAL_FRUIT_END)
		);
	}

}
