package tamaized.voidscape.datagen.data.tag.block;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.level.block.Block;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.datagenutil.data.tag.ExposedKeyTagProvider;
import tamaized.voidscape.registry.ModBlockComponentDirectory;
import tamaized.voidscape.registry.tool.IncorrectBlocksForToolModTagKeys;

@Component
public class NeedsVoidicToolBlockTagProviderFactory implements IBlockTagProviderFactory {

	@Autowired
	private ModBlockComponentDirectory blocks;

	@Autowired
	private IncorrectBlocksForToolModTagKeys incorrectBlocksForToolModTagKeys;

	@Override
	public void make(ExposedKeyTagProvider<Block> accessor, HolderLookup.Provider provider) {
		accessor.tag(incorrectBlocksForToolModTagKeys.NEEDS_VOIDIC_TOOL).add(
			blocks.oreBlocks().TITANITE_ORE.getKey()
		);
	}
}
