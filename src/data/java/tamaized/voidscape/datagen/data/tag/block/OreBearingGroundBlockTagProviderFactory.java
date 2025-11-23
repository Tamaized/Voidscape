package tamaized.voidscape.datagen.data.tag.block;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.bootstrap.RegistryProvider;

@Component
public class OreBearingGroundBlockTagProviderFactory implements IBlockTagProviderFactory {

	@Autowired
	private RegistryProvider registryProvider;

	@Override
	public void make(BlockTagProviderFactory.BlockTagsProviderAccessor accessor, HolderLookup.Provider provider) {
		tag(accessor, provider, Blocks.BEDROCK);
		tag(accessor, provider, Blocks.END_STONE);
	}

	private void tag(BlockTagProviderFactory.BlockTagsProviderAccessor accessor, HolderLookup.Provider provider, Block block) {
		accessor.tag(TagKey.create(
			Registries.BLOCK,
			ResourceLocation.fromNamespaceAndPath(
				"c",
				"ore_bearing_ground/" + registryProvider.findKeyFrom(provider, Registries.BLOCK, block).orElseThrow().location().getPath()
			)
		)).add(block);
	}
}
