package tamaized.voidscape.datagen.data.loot.sub.chest;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.registry.ModLootTables;
import tamaized.voidscape.registry.tool.ModItemComponentDirectory;

@Component
public class CharredOutpostChestLootTable implements IChestLootTable {

	@Autowired
	private ModLootTables lootTables;

	@Autowired
	private ModItemComponentDirectory items;

	@Override
	public ResourceKey<LootTable> key() {
		return lootTables.CHEST_STRUCTURE_CHARRED_OUTPOST;
	}

	@Override
	public LootTable.Builder lootTable() {
		return LootTable.lootTable()
			.withPool(
				LootPool.lootPool().setRolls(ConstantValue.exactly(4F))
					.add(LootItem.lootTableItem(items.materialItems().CHARRED_BONE.get())
						.apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2)))
					)
					.add(LootItem.lootTableItem(items.miscItems().ETHEREAL_ESSENCE.get())
						.apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 8)))
					)
			).withPool(
				LootPool.lootPool().setRolls(ConstantValue.exactly(1F))
					.add(LootItem.lootTableItem(Items.NETHERITE_SCRAP)
						.apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3)))
					)
					.add(LootItem.lootTableItem(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE))
			).withPool(
				LootPool.lootPool().setRolls(ConstantValue.exactly(6F))
					.add(LootItem.lootTableItem(Items.DIAMOND)
						.apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4)))
					)
					.add(LootItem.lootTableItem(Items.GOLD_INGOT)
						.apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4)))
					)
					.add(LootItem.lootTableItem(Items.IRON_INGOT)
						.apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4)))
					)
			);
	}

}
