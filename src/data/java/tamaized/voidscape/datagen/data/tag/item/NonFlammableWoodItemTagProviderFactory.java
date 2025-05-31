package tamaized.voidscape.datagen.data.tag.item;

import net.minecraft.core.HolderLookup;
import net.minecraft.tags.ItemTags;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.bootstrap.RegistryProvider;
import tamaized.voidscape.registry.ModBlockComponentDirectory;

@Component
public class NonFlammableWoodItemTagProviderFactory implements IItemTagProviderFactory {

	@Autowired
	private RegistryProvider registryProvider;

	@Autowired
	private ModBlockComponentDirectory blocks;

	@Override
	public void make(ItemTagProviderFactory.ItemTagsProviderAccessor accessor, HolderLookup.Provider provider) {
		accessor.tag(ItemTags.NON_FLAMMABLE_WOOD).add(
			blocks.thunderForestBiomeBlocks().THUNDER_STEM_ITEM.get(),
			blocks.thunderForestBiomeBlocks().THUNDER_STEM_STRIPPED_ITEM.get(),
			blocks.thunderForestBiomeBlocks().THUNDER_HYPHAE_ITEM.get(),
			blocks.thunderForestBiomeBlocks().THUNDER_HYPHAE_STRIPPED_ITEM.get(),
			blocks.thunderForestBiomeBlocks().THUNDER_PLANKS_ITEM.get(),
			blocks.thunderForestBiomeBlocks().THUNDER_STAIRS_ITEM.get(),
			blocks.thunderForestBiomeBlocks().THUNDER_SLAB_ITEM.get()
		);
	}
}
