package tamaized.voidscape.datagen.data.loot.sub;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootTable;
import tamaized.beanification.Configurable;
import tamaized.beanification.Directory;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.datagen.data.loot.sub.block.IBlockLootTable;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

@Configurable
public class BlockLootTableSubProvider extends BlockLootSubProvider {

	@Directory(IBlockLootTable.class)
	List<IBlockLootTable> lootTables;

	public BlockLootTableSubProvider(HolderLookup.Provider registries) {
		super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
	}

	@Override
	public void generate() {
		getKnownBlocksStream().forEach(e -> add(e, LootTable.lootTable()));
		lootTables.forEach(table -> table.add(this, this::add, this::hasSilkTouch));
	}

	@Override
	protected Iterable<Block> getKnownBlocks() {
		return getKnownBlocksStream().toList();
	}

	private Stream<Block> getKnownBlocksStream() {
		return registries.lookupOrThrow(Registries.BLOCK)
			.listElements()
			.filter(r -> Objects.requireNonNull(r.getKey()).location().getNamespace().equals(Voidscape.MODID))
			.map(Holder.Reference::value)
			.filter(b -> b.getLootTable() != BuiltInLootTables.EMPTY);
	}
}
