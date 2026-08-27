package tamaized.voidscape.datagen.data.tag.block;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.level.block.Block;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.datagenutil.data.tag.ExposedKeyTagProvider;
import tamaized.voidscape.registry.ModBlockComponentDirectory;
import tamaized.voidscape.registry.tool.IncorrectBlocksForToolModTagKeys;

import java.util.List;

@Component
public class NeedsIchorToolBlockTagProviderFactory implements IBlockTagProviderFactory {

	@Autowired
	private IncorrectBlocksForToolModTagKeys incorrectBlocksForToolModTagKeys;

	@Autowired
	private ModBlockComponentDirectory blocks;

	@Override
	public void make(ExposedKeyTagProvider<Block> accessor, HolderLookup.Provider provider) {
		accessor.tag(incorrectBlocksForToolModTagKeys.NEEDS_ICHOR_TOOL).addAll(List.of(
			blocks.oreBlocks().STRANGE_ORE.getKey(),
			blocks.oreBlocks().CRACKED_ASTRALROCK.getKey()
		));
	}
}
