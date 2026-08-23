package tamaized.voidscape.datagen.data.tag.block;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import tamaized.beanification.Component;
import tamaized.datagenutil.data.tag.ExposedKeyTagProvider;
import tamaized.voidscape.Voidscape;

import java.util.stream.Stream;

@Component
public class DebugOreBlockTagProviderFactory implements IBlockTagProviderFactory {

	@Override
	public void make(ExposedKeyTagProvider<Block> accessor, HolderLookup.Provider provider) {
		accessor.tag(TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(Voidscape.MODID, "debug_ore"))).addAll(Stream.of(
			Blocks.IRON_ORE,
			Blocks.GOLD_ORE,
			Blocks.COPPER_ORE,
			Blocks.DIAMOND_ORE,
			Blocks.EMERALD_ORE,
			Blocks.REDSTONE_ORE,
			Blocks.LAPIS_ORE,
			Blocks.COAL_ORE,
			Blocks.NETHER_GOLD_ORE,
			Blocks.STONE,
			Blocks.NETHERRACK
		).map(block -> BuiltInRegistries.BLOCK.getResourceKey(block).orElseThrow()));
	}
}
