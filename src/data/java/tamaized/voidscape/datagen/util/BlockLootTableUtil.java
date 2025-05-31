package tamaized.voidscape.datagen.util;

import net.minecraft.advancements.critereon.*;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.AlternativesEntry;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.ApplyExplosionDecay;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.*;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.neoforged.neoforge.common.Tags;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.bootstrap.RegistryProvider;

import java.util.function.Supplier;

@Component
public class BlockLootTableUtil {

	@Autowired
	private RegistryProvider registries;

	public Holder<Enchantment> enchantment(ResourceKey<Enchantment> key) {
		return registries.join().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(key);
	}

	public LootTable.Builder etherealFruit(Supplier<Item> fruit) {
		return LootTable.lootTable().withPool(
			LootPool.lootPool().add(
				LootItem.lootTableItem(fruit.get())
					.apply(ApplyBonusCount.addBonusBinomialDistributionCount(
						enchantment(Enchantments.FORTUNE),
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

	public LootTable.Builder slab(Supplier<Block> slabBlock, Supplier<Item> item) {
		return LootTable.lootTable().withPool(
			LootPool.lootPool().add(
				LootItem.lootTableItem(item.get())
					.apply(
						SetItemCountFunction.setCount(ConstantValue.exactly(2F))
							.when(
								LootItemBlockStatePropertyCondition.hasBlockStateProperties(slabBlock.get())
									.setProperties(
										StatePropertiesPredicate.Builder.properties()
											.hasProperty(SlabBlock.TYPE, SlabType.DOUBLE)
									)
							)
					).apply(ApplyExplosionDecay.explosionDecay())
			)
		);
	}

	public LootTable.Builder vine(Supplier<? extends Item> item, Supplier<LootItemCondition.Builder> hasSilkTouch) {
		return LootTable.lootTable().withPool(
			LootPool.lootPool().add(
				AlternativesEntry.alternatives(
					LootItem.lootTableItem(item.get()).when(AnyOfCondition.anyOf(
						MatchTool.toolMatches(ItemPredicate.Builder.item().of(Tags.Items.TOOLS_SHEAR)),
						hasSilkTouch.get()
					)),
					LootItem.lootTableItem(item.get()).when(
						BonusLevelTableCondition.bonusLevelFlatChance(
							enchantment(Enchantments.FORTUNE),
							0.33F,
							0.55F,
							0.77F,
							1.0F
						)
					)
				)
			)
		);
	}

}
