package tamaized.voidscape.datagen.data.loot.sub;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;
import tamaized.beanification.Configurable;
import tamaized.beanification.Directory;
import tamaized.voidscape.datagen.data.loot.sub.chest.IChestLootTable;

import java.util.List;
import java.util.function.BiConsumer;

@Configurable
public class ChestLootTableSubProvider implements LootTableSubProvider {

	@Directory(IChestLootTable.class)
	private List<IChestLootTable> lootTables;

	private final HolderLookup.Provider registries;

	public ChestLootTableSubProvider(HolderLookup.Provider registries) {
		this.registries = registries;
	}

	@Override
	public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {
		lootTables.forEach(table -> output.accept(table.key(), table.lootTable()));
	}

}
