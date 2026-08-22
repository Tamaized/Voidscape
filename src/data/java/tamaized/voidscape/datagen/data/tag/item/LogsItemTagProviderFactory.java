package tamaized.voidscape.datagen.data.tag.item;

import net.minecraft.core.HolderLookup;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.datagenutil.data.tag.ExposedKeyTagProvider;
import tamaized.voidscape.registry.ModBlockComponentDirectory;

import java.util.List;

@Component
public class LogsItemTagProviderFactory implements IItemTagProviderFactory {

	@Autowired
	private ModBlockComponentDirectory blocks;

	@Override
	public void make(ExposedKeyTagProvider<Item> accessor, HolderLookup.Provider provider) {
		accessor.tag(ItemTags.LOGS).addAll(List.of(
			blocks.thunderForestBiomeBlocks().THUNDER_STEM_ITEM.getKey(),
			blocks.thunderForestBiomeBlocks().THUNDER_STEM_STRIPPED_ITEM.getKey(),
			blocks.thunderForestBiomeBlocks().THUNDER_HYPHAE_ITEM.getKey(),
			blocks.thunderForestBiomeBlocks().THUNDER_HYPHAE_STRIPPED_ITEM.getKey()
		));
	}
}
