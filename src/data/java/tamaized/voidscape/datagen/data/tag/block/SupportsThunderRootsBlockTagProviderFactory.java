package tamaized.voidscape.datagen.data.tag.block;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.datagenutil.data.tag.ExposedKeyTagProvider;
import tamaized.voidscape.registry.ModBlockComponentDirectory;

import java.util.List;

@Component
public class SupportsThunderRootsBlockTagProviderFactory implements IBlockTagProviderFactory {

	@Autowired
	private ModBlockComponentDirectory blocks;

	@Override
	public void make(ExposedKeyTagProvider<Block> accessor, HolderLookup.Provider provider) {
		TagAppender<ResourceKey<Block>, Block> appender = accessor.tag(blocks.thunderForestBiomeBlocks().TAG_THUNDER_ROOTS_SUPPORTS);
		List.of(
			BlockTags.SUPPORTS_VEGETATION,
			BlockTags.NYLIUM
		).forEach(appender::addTag);
		appender.add(BuiltInRegistries.BLOCK.getResourceKey(Blocks.SOUL_SOIL).orElseThrow());
	}
}
