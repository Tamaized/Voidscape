package tamaized.voidscape.datagen.data.tag.item;

import net.minecraft.core.HolderLookup;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.RegistryProvider;
import tamaized.voidscape.datagen.data.tag.block.BlockTagProviderFactory;
import tamaized.voidscape.datagen.data.tag.block.IBlockTagProviderFactory;
import tamaized.voidscape.registry.ModBlockComponentDirectory;
import tamaized.voidscape.registry.tool.ModItemComponentDirectory;

@Component
public class LogsItemTagProviderFactory implements IItemTagProviderFactory {

	@Autowired
	private RegistryProvider registryProvider;

	@Autowired
	private ModBlockComponentDirectory blocks;

	@Override
	public void make(ItemTagProviderFactory.ItemTagsProviderAccessor accessor, HolderLookup.Provider provider) {
		accessor.tag(ItemTags.LOGS).add(
			blocks.thunderForestBiomeBlocks().THUNDER_STEM_ITEM.get(),
			blocks.thunderForestBiomeBlocks().THUNDER_STEM_STRIPPED_ITEM.get(),
			blocks.thunderForestBiomeBlocks().THUNDER_HYPHAE_ITEM.get(),
			blocks.thunderForestBiomeBlocks().THUNDER_HYPHAE_STRIPPED_ITEM.get()
		);
	}
}
