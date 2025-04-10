package tamaized.voidscape.datagen.data.loot.sub;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;

public interface IBasicLootTable {

	ResourceKey<LootTable> key();

	LootTable.Builder lootTable();

}
