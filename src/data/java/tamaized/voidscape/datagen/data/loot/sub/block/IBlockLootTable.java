package tamaized.voidscape.datagen.data.loot.sub.block;

import net.minecraft.core.HolderGetter;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public interface IBlockLootTable {

	void add(BlockLootSubProvider provider, HolderGetter<Item> itemProvider, BiConsumer<Block, LootTable.Builder> add, Supplier<LootItemCondition.Builder> hasSilkTouch);

}
