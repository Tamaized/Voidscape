package tamaized.voidscape.datagen.data.tag.block;

import net.minecraft.core.HolderLookup;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.block.RequiresVoidToolBlock;
import tamaized.voidscape.datagen.RegistryProvider;
import tamaized.voidscape.registry.ModBlockComponentDirectory;

@Component
public class NeedsIchorToolTagProviderFactory implements IBlockTagProviderFactory {

	@Autowired
	private RegistryProvider registryProvider;

	@Autowired
	private ModBlockComponentDirectory blocks;

	@Override
	public void make(BlockTagProviderFactory.BlockTagsProviderAccessor accessor, HolderLookup.Provider provider) {
		accessor.tag(RequiresVoidToolBlock.NEEDS_ICHOR_TOOL).add(
			blocks.oreBlocks().STRANGE_ORE.get(),
			blocks.oreBlocks().CRACKED_ASTRALROCK.get()
		);
	}

}
