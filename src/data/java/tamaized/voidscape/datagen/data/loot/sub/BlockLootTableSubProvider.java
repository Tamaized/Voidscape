package tamaized.voidscape.datagen.data.loot.sub;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import tamaized.beanification.Autowired;
import tamaized.beanification.Configurable;
import tamaized.beanification.Directory;
import tamaized.voidscape.datagen.bootstrap.RegistryProvider;
import tamaized.voidscape.datagen.data.loot.sub.block.IBlockLootTable;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

@Configurable
public class BlockLootTableSubProvider extends BlockLootSubProvider {

	@Directory(IBlockLootTable.class)
	List<IBlockLootTable> lootTables;

	@Autowired
	private RegistryProvider registryProvider;

	public BlockLootTableSubProvider(HolderLookup.Provider registries) {
		super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
	}

	@Override
	public void generate() {
		HolderGetter<Item> itemProvider = registries.lookupOrThrow(Registries.ITEM);
		getKnownBlocksStream().forEach(e -> add(e, LootTable.lootTable()));
		lootTables.forEach(table -> table.add(this, itemProvider, this::add, this::hasSilkTouch));
	}

	@Override
	protected Iterable<Block> getKnownBlocks() {
		return getKnownBlocksStream().toList();
	}

	private Stream<Block> getKnownBlocksStream() {
		return registryProvider.filterStreamForModFrom(registries, Registries.BLOCK)
			.filter(b -> b.getLootTable().isPresent());
	}
}
