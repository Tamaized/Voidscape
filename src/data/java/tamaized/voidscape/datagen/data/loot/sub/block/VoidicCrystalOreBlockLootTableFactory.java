package tamaized.voidscape.datagen.data.loot.sub.block;

import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyExplosionDecay;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.registry.ModBlockComponentDirectory;
import tamaized.voidscape.registry.tool.ModItemComponentDirectory;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

@Component
public class VoidicCrystalOreBlockLootTableFactory implements IBlockLootTable {

	@Autowired
	private ModBlockComponentDirectory blocks;

	@Autowired
	private ModItemComponentDirectory items;

	@Override
	public void add(BlockLootSubProvider provider, BiConsumer<Block, LootTable.Builder> add, Supplier<LootItemCondition.Builder> hasSilkTouch) {
		add.accept(
			blocks.oreBlocks().VOIDIC_CRYSTAL_ORE.get(),
			LootTable.lootTable().withPool(
				LootPool.lootPool().add(
					LootItem.lootTableItem(items.materialItems().VOIDIC_CRYSTAL.get())
						.apply(SetItemCountFunction.setCount(UniformGenerator.between(1F, 3F)))
						.apply(ApplyExplosionDecay.explosionDecay())
				)
			)
		);
	}

}
