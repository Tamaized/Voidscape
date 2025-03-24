package tamaized.voidscape.datagen.util;

import net.minecraft.advancements.critereon.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.AlternativesEntry;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.RegistryProvider;

import java.util.List;
import java.util.function.Supplier;

@Component
public class BlockLootTableUtil {

	@Autowired
	private RegistryProvider registries;

	public LootTable.Builder etherealFruit(Supplier<Item> fruit) {
		return LootTable.lootTable().withPool(
			LootPool.lootPool().add(
				LootItem.lootTableItem(fruit.get())
					.apply(ApplyBonusCount.addBonusBinomialDistributionCount(
						registries.join().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.FORTUNE),
						0.5714286F,
						1
					))
			).when(ExplosionCondition.survivesExplosion())
		);
	}

	public LootTable.Builder silkTouch(Supplier<Item> item, Supplier<Item> itemFromSilkTouch, Supplier<LootItemCondition.Builder> hasSilkTouch) {
		return LootTable.lootTable().withPool(
			LootPool.lootPool().add(
				AlternativesEntry.alternatives(
					LootItem.lootTableItem(itemFromSilkTouch.get()).when(hasSilkTouch.get()),
					LootItem.lootTableItem(item.get())
				)
			).when(ExplosionCondition.survivesExplosion())
		);
	}

}
