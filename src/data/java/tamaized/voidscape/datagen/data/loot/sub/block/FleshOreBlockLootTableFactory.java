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
import tamaized.voidscape.registry.tool.ModItemComponentDirectory;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

@Component
public class FleshOreBlockLootTableFactory implements IBlockLootTable {

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
			blocks.oreBlocks().FLESH_ORE.get(),
			blockLootTableUtil.ore(items.materialItems().FLESH_CHUNK, blocks.oreBlocks().FLESH_ORE_ITEM)
		);
	}

}
