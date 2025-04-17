package tamaized.voidscape.datagen.data.tag.item;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.datagen.RegistryProvider;
import tamaized.voidscape.registry.ModBlockComponentDirectory;
import tamaized.voidscape.registry.ModItemTags;

@Component
public class ThunderStemsItemTagProviderFactory implements IItemTagProviderFactory {

	@Autowired
	private RegistryProvider registryProvider;

	@Autowired
	private ModItemTags itemTags;

	@Autowired
	private ModBlockComponentDirectory blocks;

	@Override
	public void make(ItemTagProviderFactory.ItemTagsProviderAccessor accessor, HolderLookup.Provider provider) {
		accessor.tag(itemTags.THUNDER_STEMS).add(
			blocks.thunderForestBiomeBlocks().THUNDER_STEM_ITEM.get(),
			blocks.thunderForestBiomeBlocks().THUNDER_HYPHAE_ITEM.get(),
			blocks.thunderForestBiomeBlocks().THUNDER_STEM_STRIPPED_ITEM.get(),
			blocks.thunderForestBiomeBlocks().THUNDER_HYPHAE_STRIPPED_ITEM.get()
		);
	}
}
