package tamaized.voidscape.datagen.data.tag.block;

import net.minecraft.core.HolderLookup;
import net.minecraft.tags.BlockTags;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.RegistryProvider;
import tamaized.voidscape.registry.ModBlockComponentDirectory;

@Component
public class NeedsIronToolBlockTagProviderFactory implements IBlockTagProviderFactory {

	@Autowired
	private RegistryProvider registryProvider;

	@Autowired
	private ModBlockComponentDirectory blocks;

	@Override
	public void make(BlockTagProviderFactory.BlockTagsProviderAccessor accessor, HolderLookup.Provider provider) {
		accessor.tag(BlockTags.NEEDS_IRON_TOOL).add(
			blocks.machineBlocks().MACHINE_LIQUIFIER.get(),
			blocks.machineBlocks().MACHINE_DEFUSER.get(),
			blocks.machineBlocks().MACHINE_GERMINATOR.get(),
			blocks.machineBlocks().MACHINE_WELL.get(),
			blocks.machineBlocks().MACHINE_COOP.get(),
			blocks.machineBlocks().MACHINE_INFUSER.get(),
			blocks.machineBlocks().MACHINE_COLLECTOR.get()
		);
	}

}
