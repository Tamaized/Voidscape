package tamaized.voidscape.datagen.util;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.RegistryProvider;

import java.util.function.Supplier;

@Component
public class BlockLootTableUtil {

	@Autowired
	private RegistryProvider registries;

	public LootTable.Builder etherealFruit(Supplier<Item> fruit) {
		return LootTable.lootTable().withPool(
			LootPool.lootPool()
				.add(
					LootItem.lootTableItem(fruit.get())
						.apply(ApplyBonusCount.addBonusBinomialDistributionCount(
							registries.join().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.FORTUNE),
							0.5714286F,
							1
						))
				).when(ExplosionCondition.survivesExplosion())
		);
	}

}
