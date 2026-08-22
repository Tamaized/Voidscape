package tamaized.voidscape.datagen.data.tag.item;

import net.minecraft.core.HolderLookup;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.datagenutil.data.tag.ExposedKeyTagProvider;
import tamaized.voidscape.registry.ModBlockComponentDirectory;

@Component
public class StairsItemTagProviderFactory implements IItemTagProviderFactory {

	@Autowired
	private ModBlockComponentDirectory blocks;

	@Override
	public void make(ExposedKeyTagProvider<Item> accessor, HolderLookup.Provider provider) {
		accessor.tag(ItemTags.STAIRS).add(
			blocks.thunderForestBiomeBlocks().THUNDER_STAIRS_ITEM.getKey()
		);
	}
}
