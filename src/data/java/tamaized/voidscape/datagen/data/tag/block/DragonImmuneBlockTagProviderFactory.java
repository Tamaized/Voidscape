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
public class DragonImmuneBlockTagProviderFactory implements IBlockTagProviderFactory {

	@Autowired
	private ModBlockComponentDirectory blocks;

	@Override
	public void make(ExposedKeyTagProvider<Block> accessor, HolderLookup.Provider provider) {
		accessor.tag(BlockTags.DRAGON_IMMUNE).addAll(List.of(
			blocks.oreBlocks().VOIDIC_CRYSTAL_ORE.getKey(),
			blocks.spireBlocks().THUNDERROCK.getKey(),
			blocks.spireBlocks().ANTIROCK.getKey(),
			blocks.nullBiomeBlocks().NULL_BLACK.getKey(),
			blocks.nullBiomeBlocks().NULL_WHITE.getKey(),
			blocks.spireBlocks().ASTRALROCK.getKey(),
			blocks.thunderForestBiomeBlocks().THUNDER_NYLIUM.getKey(),
			blocks.oreBlocks().CRACKED_ASTRALROCK.getKey()
		));
	}
}
