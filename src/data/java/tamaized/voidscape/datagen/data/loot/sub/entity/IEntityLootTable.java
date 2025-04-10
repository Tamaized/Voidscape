package tamaized.voidscape.datagen.data.loot.sub.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.storage.loot.LootTable;
import tamaized.voidscape.datagen.data.loot.sub.EntityLootTableSubProvider;

import java.util.function.BiConsumer;

public interface IEntityLootTable {

	void add(EntityLootTableSubProvider provider, BiConsumer<EntityType<?>, LootTable.Builder> add);

}
