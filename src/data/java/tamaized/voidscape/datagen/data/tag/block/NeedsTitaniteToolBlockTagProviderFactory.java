package tamaized.voidscape.datagen.data.tag.block;

import net.minecraft.core.HolderLookup;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.block.RequiresVoidToolBlock;
import tamaized.voidscape.datagen.bootstrap.RegistryProvider;
import tamaized.voidscape.registry.ModBlockComponentDirectory;

@Component
public class NeedsTitaniteToolBlockTagProviderFactory implements IBlockTagProviderFactory {

	@Autowired
	private RegistryProvider registryProvider;

	@Autowired
	private ModBlockComponentDirectory blocks;

	@Override
	public void make(BlockTagProviderFactory.BlockTagsProviderAccessor accessor, HolderLookup.Provider provider) {
		accessor.tag(RequiresVoidToolBlock.NEEDS_TITANITE_TOOL).add(
			blocks.oreBlocks().FLESH_ORE.get()
		);
	}

}
