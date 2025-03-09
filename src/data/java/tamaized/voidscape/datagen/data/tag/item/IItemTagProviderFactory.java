package tamaized.voidscape.datagen.data.tag.item;

import net.minecraft.core.HolderLookup;
import tamaized.voidscape.datagen.data.tag.block.BlockTagProviderFactory;

public interface IItemTagProviderFactory {

	void make(ItemTagProviderFactory.ItemTagsProviderAccessor accessor, HolderLookup.Provider provider);

}
