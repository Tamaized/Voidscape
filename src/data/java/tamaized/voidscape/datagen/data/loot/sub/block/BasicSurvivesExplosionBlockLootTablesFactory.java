package tamaized.voidscape.datagen.data.loot.sub.block;

import net.minecraft.core.HolderGetter;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.registry.ModBlockComponentDirectory;
import tamaized.voidscape.registry.ModItemComponentDirectory;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

@Component
public class BasicSurvivesExplosionBlockLootTablesFactory implements IBlockLootTable {

	@Autowired
	private ModBlockComponentDirectory blocks;

	@Autowired
	private ModItemComponentDirectory items;

	@Override
	public void add(BlockLootSubProvider provider, HolderGetter<Item> itemProvider, BiConsumer<Block, LootTable.Builder> add, Supplier<LootItemCondition.Builder> hasSilkTouch) {
		add(provider, add, blocks.oreBlocks().CRACKED_ASTRALROCK, items.materialItems().ASTRAL_ESSENCE);

		add(provider, add, blocks.materialBlocks().VOIDIC_CRYSTAL_BLOCK, blocks.materialBlocks().VOIDIC_CRYSTAL_BLOCK_ITEM);
		add(provider, add, blocks.materialBlocks().CHARRED_BRICK, blocks.materialBlocks().CHARRED_BRICK_ITEM);
		add(provider, add, blocks.materialBlocks().FLESH_BLOCK, blocks.materialBlocks().FLESH_BLOCK_ITEM);

		add(provider, add, blocks.machineBlocks().MACHINE_COLLECTOR, blocks.machineBlocks().MACHINE_COLLECTOR_ITEM);
		add(provider, add, blocks.machineBlocks().MACHINE_COOP, blocks.machineBlocks().MACHINE_COOP_ITEM);
		add(provider, add, blocks.machineBlocks().MACHINE_CORE, blocks.machineBlocks().MACHINE_CORE_ITEM);
		add(provider, add, blocks.machineBlocks().MACHINE_DEFUSER, blocks.machineBlocks().MACHINE_DEFUSER_ITEM);
		add(provider, add, blocks.machineBlocks().MACHINE_GERMINATOR, blocks.machineBlocks().MACHINE_GERMINATOR_ITEM);
		add(provider, add, blocks.machineBlocks().MACHINE_HATCHERY, blocks.machineBlocks().MACHINE_HATCHERY_ITEM);
		add(provider, add, blocks.machineBlocks().MACHINE_INFUSER, blocks.machineBlocks().MACHINE_INFUSER_ITEM);
		add(provider, add, blocks.machineBlocks().MACHINE_LIQUIFIER, blocks.machineBlocks().MACHINE_LIQUIFIER_ITEM);
		add(provider, add, blocks.machineBlocks().MACHINE_WELL, blocks.machineBlocks().MACHINE_WELL_ITEM);

		add(provider, add, blocks.thunderForestBiomeBlocks().THUNDER_FUNGUS, blocks.thunderForestBiomeBlocks().THUNDER_FUNGUS_ITEM);
		add(provider, add, blocks.thunderForestBiomeBlocks().THUNDER_HYPHAE, blocks.thunderForestBiomeBlocks().THUNDER_HYPHAE_ITEM);
		add(provider, add, blocks.thunderForestBiomeBlocks().THUNDER_HYPHAE_STRIPPED, blocks.thunderForestBiomeBlocks().THUNDER_HYPHAE_STRIPPED_ITEM);
		add(provider, add, blocks.thunderForestBiomeBlocks().THUNDER_PLANKS, blocks.thunderForestBiomeBlocks().THUNDER_PLANKS_ITEM);
		add(provider, add, blocks.thunderForestBiomeBlocks().THUNDER_ROOTS, blocks.thunderForestBiomeBlocks().THUNDER_ROOTS_ITEM);
		add(provider, add, blocks.thunderForestBiomeBlocks().THUNDER_STAIRS, blocks.thunderForestBiomeBlocks().THUNDER_STAIRS_ITEM);
		add(provider, add, blocks.thunderForestBiomeBlocks().THUNDER_STEM, blocks.thunderForestBiomeBlocks().THUNDER_STEM_ITEM);
		add(provider, add, blocks.thunderForestBiomeBlocks().THUNDER_STEM_STRIPPED, blocks.thunderForestBiomeBlocks().THUNDER_STEM_STRIPPED_ITEM);
		add(provider, add, blocks.thunderForestBiomeBlocks().THUNDER_WART, blocks.thunderForestBiomeBlocks().THUNDER_WART_ITEM);
	}

	private void add(BlockLootSubProvider provider, BiConsumer<Block, LootTable.Builder> add, Supplier<? extends Block> block, Supplier<? extends Item> item) {
		add.accept(block.get(), provider.createSingleItemTable(item.get()));
	}

}
