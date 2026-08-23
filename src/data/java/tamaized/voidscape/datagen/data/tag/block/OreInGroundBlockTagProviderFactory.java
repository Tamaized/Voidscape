package tamaized.voidscape.datagen.data.tag.block;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.datagenutil.data.tag.ExposedKeyTagProvider;
import tamaized.voidscape.registry.ModBlockComponentDirectory;

import java.util.List;

@Component
public class OreInGroundBlockTagProviderFactory implements IBlockTagProviderFactory {

	@Autowired
	private ModBlockComponentDirectory blocks;

	@Override
	public void make(ExposedKeyTagProvider<Block> accessor, HolderLookup.Provider provider) {
		tag(accessor, Blocks.BEDROCK, List.of(blocks.oreBlocks().VOIDIC_CRYSTAL_ORE.getKey()));
		tag(accessor, Blocks.END_STONE, List.of(blocks.oreBlocks().STRANGE_ORE.getKey()));
		accessor.tag(Tags.Blocks.ORES_IN_GROUND_NETHERRACK).add(blocks.oreBlocks().FLESH_ORE.getKey());
		accessor.tag(Tags.Blocks.ORES_IN_GROUND_STONE).add(blocks.oreBlocks().TITANITE_ORE.getKey());
	}

	private void tag(ExposedKeyTagProvider<Block> accessor, Block ground, List<ResourceKey<Block>> ores) {
		accessor.tag(TagKey.create(
			Registries.BLOCK,
			Identifier.fromNamespaceAndPath(
				"c",
				"ores_in_ground/" + BuiltInRegistries.BLOCK.getResourceKey(ground).orElseThrow().identifier().getPath()
			)
		)).addAll(ores);
	}
}
