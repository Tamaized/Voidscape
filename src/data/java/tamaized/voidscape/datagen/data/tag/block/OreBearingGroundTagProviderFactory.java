package tamaized.voidscape.datagen.data.tag.block;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.datagen.RegistryProvider;
import tamaized.voidscape.datagen.data.tag.ITagProviderFactory;
import tamaized.voidscape.datagen.util.TagProviderUtil;

@Component
public class OreBearingGroundTagProviderFactory implements ITagProviderFactory<Block> {

	@Autowired
	private RegistryProvider registryProvider;

	@Autowired
	private TagProviderUtil tagProviderUtil;

	@Override
	public BlockTagsProvider make(GatherDataEvent event) {
		return new BlockTagsProvider(
			event.getGenerator().getPackOutput(),
			registryProvider.retrieve(event),
			Voidscape.MODID,
			event.getExistingFileHelper()
		) {
			@Override
			protected void addTags(HolderLookup.Provider provider) {
				tag(provider, Blocks.BEDROCK);
				tag(provider, Blocks.END_STONE);
			}

			private void tag(HolderLookup.Provider provider, Block block) {
				tag(TagKey.create(
					Registries.BLOCK,
					ResourceLocation.fromNamespaceAndPath(
						"c",
						"ore_bearing_ground/" + registryProvider.findKeyFrom(provider, Registries.BLOCK, block).orElseThrow().location().getPath()
					)
				)).add(block);
			}
		};
	}

}
