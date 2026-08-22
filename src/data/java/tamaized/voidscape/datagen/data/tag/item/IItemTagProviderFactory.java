package tamaized.voidscape.datagen.data.tag.item;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.Item;
import tamaized.datagenutil.data.tag.ExposedKeyTagProvider;

public interface IItemTagProviderFactory {

	void make(ExposedKeyTagProvider<Item> accessor, HolderLookup.Provider provider);

}
