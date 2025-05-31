package tamaized.voidscape.datagen.data.tag.item;

import net.minecraft.core.HolderLookup;
import net.minecraft.tags.ItemTags;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.bootstrap.RegistryProvider;
import tamaized.voidscape.registry.ModBlockComponentDirectory;

@Component
public class SlabsItemTagProviderFactory implements IItemTagProviderFactory {

	@Autowired
	private RegistryProvider registryProvider;

	@Autowired
	private ModBlockComponentDirectory blocks;

	@Override
	public void make(ItemTagProviderFactory.ItemTagsProviderAccessor accessor, HolderLookup.Provider provider) {
		accessor.tag(ItemTags.SLABS).add(
			blocks.thunderForestBiomeBlocks().THUNDER_SLAB_ITEM.get()
		);
	}
}
