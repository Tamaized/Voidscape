package tamaized.voidscape.datagen.data.loot.sub.block;

import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.registry.ModBlockComponentDirectory;
import tamaized.voidscape.registry.tool.ModItemComponentDirectory;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

@Component
public class BasicSurvivesExplosionBlockLootTablesFactory implements IBlockLootTable {

	@Autowired
	private ModBlockComponentDirectory blocks;

	@Autowired
	private ModItemComponentDirectory items;

	@Override
	public void add(BlockLootSubProvider provider, BiConsumer<Block, LootTable.Builder> add, Supplier<LootItemCondition.Builder> hasSilkTouch) {
		add(provider, add, blocks.oreBlocks().CRACKED_ASTRALROCK, items.materialItems().ASTRAL_ESSENCE);

		add(provider, add, blocks.materialBlocks().CHARRED_BRICK, blocks.materialBlocks().CHARRED_BRICK_ITEM);
		add(provider, add, blocks.materialBlocks().FLESH_BLOCK, blocks.materialBlocks().FLESH_BLOCK_ITEM);
	}

	private void add(BlockLootSubProvider provider, BiConsumer<Block, LootTable.Builder> add, Supplier<Block> block, Supplier<Item> item) {
		add.accept(block.get(), provider.createSingleItemTable(item.get()));
	}

}
