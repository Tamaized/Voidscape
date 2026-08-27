package tamaized.voidscape.datagen.data.tag.block;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.datagenutil.data.tag.ExposedKeyTagProvider;
import tamaized.voidscape.registry.tool.VoidToolLadder;

@Component
public class IncorrectBlocksForVoidToolBlockTagProviderFactory implements IBlockTagProviderFactory {

	@Autowired
	private VoidToolLadder ladder;

	@Override
	public void make(ExposedKeyTagProvider<Block> accessor, HolderLookup.Provider provider) {
		ladder.rungs().forEach(rung -> rung.incorrectBlocks().ifPresent(incorrectBlocks -> {
			TagAppender<ResourceKey<Block>, Block> appender = accessor.tag(incorrectBlocks);
			ladder.blocksAbove(rung).forEach(appender::addTag);
		}));
	}
}
