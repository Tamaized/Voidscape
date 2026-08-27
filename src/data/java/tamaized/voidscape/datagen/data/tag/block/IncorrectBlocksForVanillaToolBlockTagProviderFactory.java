package tamaized.voidscape.datagen.data.tag.block;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.datagenutil.data.tag.ExposedKeyTagProvider;
import tamaized.voidscape.registry.tool.VoidToolLadder;

import java.util.List;

@Component
public class IncorrectBlocksForVanillaToolBlockTagProviderFactory implements IBlockTagProviderFactory {

	@Autowired
	private VoidToolLadder ladder;

	private final List<TagKey<Block>> vanillaToolTags = List.of(
		BlockTags.INCORRECT_FOR_WOODEN_TOOL,
		BlockTags.INCORRECT_FOR_STONE_TOOL,
		BlockTags.INCORRECT_FOR_COPPER_TOOL,
		BlockTags.INCORRECT_FOR_IRON_TOOL,
		BlockTags.INCORRECT_FOR_GOLD_TOOL,
		BlockTags.INCORRECT_FOR_DIAMOND_TOOL,
		BlockTags.INCORRECT_FOR_NETHERITE_TOOL
	);

	@Override
	public void make(ExposedKeyTagProvider<Block> accessor, HolderLookup.Provider provider) {
		final List<TagKey<Block>> voidToolTags = ladder.needsToolTags();

		vanillaToolTags.forEach(vanillaToolTag -> {
			TagAppender<ResourceKey<Block>, Block> appender = accessor.tag(vanillaToolTag);
			voidToolTags.forEach(appender::addTag);
		});
	}
}
