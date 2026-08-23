package tamaized.voidscape.datagen.data.loot.sub.block;

import net.minecraft.core.HolderGetter;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.util.BlockLootTableUtil;
import tamaized.voidscape.registry.ModBlockComponentDirectory;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

@Component
public class BasicSlabBlockLootTablesFactory implements IBlockLootTable {

	@Autowired
	private BlockLootTableUtil blockLootTableUtil;

	@Autowired
	private ModBlockComponentDirectory blocks;

	@Override
	public void add(BlockLootSubProvider provider, HolderGetter<Item> itemProvider, BiConsumer<Block, LootTable.Builder> add, Supplier<LootItemCondition.Builder> hasSilkTouch) {
		add(add, blocks.thunderForestBiomeBlocks().THUNDER_SLAB, blocks.thunderForestBiomeBlocks().THUNDER_SLAB_ITEM);
	}

	private void add(BiConsumer<Block, LootTable.Builder> add, Supplier<Block> block, Supplier<Item> item) {
		add.accept(block.get(), blockLootTableUtil.slab(block, item));
	}

}
