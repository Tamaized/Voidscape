package tamaized.voidscape.datagen.data.tag.block;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.RegistryProvider;
import tamaized.voidscape.registry.ModBlockComponentDirectory;

@Component
public class OreInGroundBlockTagProviderFactory implements IBlockTagProviderFactory {

	@Autowired
	private RegistryProvider registryProvider;

	@Autowired
	private ModBlockComponentDirectory blocks;

	@Override
	public void make(BlockTagProviderFactory.BlockTagsProviderAccessor accessor, HolderLookup.Provider provider) {
		tag(accessor, provider, Blocks.BEDROCK, blocks.oreBlocks().VOIDIC_CRYSTAL_ORE.get());
		tag(accessor, provider, Blocks.END_STONE, blocks.oreBlocks().STRANGE_ORE.get());
		accessor.tag(Tags.Blocks.ORES_IN_GROUND_NETHERRACK).add(blocks.oreBlocks().FLESH_ORE.get());
		accessor.tag(Tags.Blocks.ORES_IN_GROUND_STONE).add(blocks.oreBlocks().TITANITE_ORE.get());
	}

	private void tag(BlockTagProviderFactory.BlockTagsProviderAccessor accessor, HolderLookup.Provider provider, Block ground, Block... blocks) {
		accessor.tag(TagKey.create(
			Registries.BLOCK,
			ResourceLocation.fromNamespaceAndPath(
				"c",
				"ores_in_ground/" + registryProvider.findKeyFrom(provider, Registries.BLOCK, ground).orElseThrow().location().getPath()
			)
		)).add(blocks);
	}

}
