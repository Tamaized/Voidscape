package tamaized.voidscape.datagen.data.tag.block;

import net.minecraft.core.HolderLookup;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.block.RequiresVoidToolBlock;
import tamaized.voidscape.registry.tool.IncorrectBlocksForToolModTagKeys;

@Component
public class IncorrectBlocksForCorruptToolBlockTagProviderFactory implements IBlockTagProviderFactory {

	@Autowired
	private IncorrectBlocksForToolModTagKeys incorrectBlocksForToolModTagKeys;

	@Override
	public void make(BlockTagProviderFactory.BlockTagsProviderAccessor accessor, HolderLookup.Provider provider) {
		accessor.tag(incorrectBlocksForToolModTagKeys.CORRUPT)
			.addTag(RequiresVoidToolBlock.NEEDS_TITANITE_TOOL)
			.addTag(RequiresVoidToolBlock.NEEDS_ICHOR_TOOL)
			.addTag(RequiresVoidToolBlock.NEEDS_ASTRAL_TOOL);
	}

}
