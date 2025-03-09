package tamaized.voidscape.datagen.data.tag.block;

import net.minecraft.core.HolderLookup;
import net.minecraft.tags.BlockTags;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.RegistryProvider;
import tamaized.voidscape.registry.ModBlockComponentDirectory;

@Component
public class WoodenSlabsEfficientTagProviderFactory implements IBlockTagProviderFactory {

	@Autowired
	private RegistryProvider registryProvider;

	@Autowired
	private ModBlockComponentDirectory blocks;

	@Override
	public void make(BlockTagProviderFactory.BlockTagsProviderAccessor accessor, HolderLookup.Provider provider) {
		accessor.tag(BlockTags.WOODEN_SLABS).add(
			blocks.thunderForestBiomeBlocks().THUNDER_SLAB.get()
		);
	}

}
