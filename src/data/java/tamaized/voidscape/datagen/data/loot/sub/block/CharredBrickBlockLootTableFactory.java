package tamaized.voidscape.datagen.data.loot.sub.block;

import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.RegistryProvider;
import tamaized.voidscape.registry.ModBlockComponentDirectory;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

@Component
public class CharredBrickBlockLootTableFactory implements IBlockLootTable {

	@Autowired
	private RegistryProvider registries;

	@Autowired
	private ModBlockComponentDirectory blocks;

	public void add(BlockLootSubProvider provider, BiConsumer<Block, LootTable.Builder> add, Supplier<LootItemCondition.Builder> hasSilkTouch) {
		add.accept(
			blocks.materialBlocks().CHARRED_BRICK.get(),
			provider.createSingleItemTable(blocks.materialBlocks().CHARRED_BRICK_ITEM.get())
		);
	}

}
