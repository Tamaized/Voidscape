package tamaized.voidscape.datagen.data.tag.block;

import net.minecraft.core.HolderLookup;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.block.RequiresVoidToolBlock;
import tamaized.voidscape.datagen.bootstrap.RegistryProvider;
import tamaized.voidscape.registry.ModBlockComponentDirectory;
import tamaized.voidscape.registry.tool.IncorrectBlocksForToolModTagKeys;

@Component
public class IncorrectBlocksForVoidicToolBlockTagProviderFactory implements IBlockTagProviderFactory {

	@Autowired
	private IncorrectBlocksForToolModTagKeys incorrectBlocksForToolModTagKeys;

	@Override
	public void make(BlockTagProviderFactory.BlockTagsProviderAccessor accessor, HolderLookup.Provider provider) {
		accessor.tag(incorrectBlocksForToolModTagKeys.VOIDIC_CRYSTAL)
			.addTag(RequiresVoidToolBlock.NEEDS_CORRUPT_TOOL)
			.addTag(RequiresVoidToolBlock.NEEDS_TITANITE_TOOL)
			.addTag(RequiresVoidToolBlock.NEEDS_ICHOR_TOOL)
			.addTag(RequiresVoidToolBlock.NEEDS_ASTRAL_TOOL);
	}

}
