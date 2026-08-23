package tamaized.voidscape.datagen.data.tag.block;

import net.minecraft.core.HolderLookup;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.datagenutil.data.tag.ExposedKeyTagProvider;
import tamaized.voidscape.registry.ModBlockComponentDirectory;

import java.util.List;

@Component
public class LogsBlockTagProviderFactory implements IBlockTagProviderFactory {

	@Autowired
	private ModBlockComponentDirectory blocks;

	@Override
	public void make(ExposedKeyTagProvider<Block> accessor, HolderLookup.Provider provider) {
		accessor.tag(BlockTags.LOGS).addAll(List.of(
			blocks.thunderForestBiomeBlocks().THUNDER_STEM.getKey(),
			blocks.thunderForestBiomeBlocks().THUNDER_STEM_STRIPPED.getKey(),
			blocks.thunderForestBiomeBlocks().THUNDER_HYPHAE.getKey(),
			blocks.thunderForestBiomeBlocks().THUNDER_HYPHAE_STRIPPED.getKey()
		));
	}
}
