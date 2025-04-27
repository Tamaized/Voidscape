package tamaized.voidscape.datagen.data.loot.sub.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.EnchantedCountIncreaseFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.data.loot.sub.EntityLootTableSubProvider;
import tamaized.voidscape.registry.ModEntities;
import tamaized.voidscape.registry.ModItemComponentDirectory;

import java.util.function.BiConsumer;

@Component
public class VoidlingEntityLootTable implements IEntityLootTable {

	@Autowired
	private ModEntities entities;

	@Autowired
	private ModItemComponentDirectory items;

	@Override
	public void add(EntityLootTableSubProvider provider, BiConsumer<EntityType<?>, LootTable.Builder> add) {
		add.accept(
			entities.VOIDLING.get(),
			LootTable.lootTable().withPool(LootPool.lootPool()
				.add(LootItem.lootTableItem(items.miscItems().ETHEREAL_SPIDER_EGGS.get())
					.setWeight(75)
					.apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3)))
					.apply(EnchantedCountIncreaseFunction.lootingMultiplier(provider.registries(), UniformGenerator.between(1, 2)))
				)
				.add(LootItem.lootTableItem(items.augmentItems().ETHEREAL_SPIDER_FANG.get())
					.setWeight(10)
					.apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 1))))
			));
	}

}
