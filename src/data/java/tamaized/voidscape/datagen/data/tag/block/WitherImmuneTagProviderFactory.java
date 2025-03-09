package tamaized.voidscape.datagen.data.tag.block;

import net.minecraft.core.HolderLookup;
import net.minecraft.tags.BlockTags;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.RegistryProvider;
import tamaized.voidscape.registry.ModBlockComponentDirectory;

@Component
public class WitherImmuneTagProviderFactory implements IBlockTagProviderFactory {

	@Autowired
	private RegistryProvider registryProvider;

	@Autowired
	private ModBlockComponentDirectory blocks;

	@Override
	public void make(BlockTagProviderFactory.BlockTagsProviderAccessor accessor, HolderLookup.Provider provider) {
		accessor.tag(BlockTags.WITHER_IMMUNE).add(
			blocks.oreBlocks().VOIDIC_CRYSTAL_ORE.get(),
			blocks.spireBlocks().THUNDERROCK.get(),
			blocks.spireBlocks().ANTIROCK.get(),
			blocks.nullBiomeBlocks().NULL_BLACK.get(),
			blocks.nullBiomeBlocks().NULL_WHITE.get(),
			blocks.spireBlocks().ASTRALROCK.get(),
			blocks.thunderForestBiomeBlocks().THUNDER_NYLIUM.get(),
			blocks.oreBlocks().CRACKED_ASTRALROCK.get()
		);
	}

}
