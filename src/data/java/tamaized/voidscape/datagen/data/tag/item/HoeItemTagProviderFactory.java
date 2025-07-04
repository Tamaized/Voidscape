package tamaized.voidscape.datagen.data.tag.item;

import net.minecraft.core.HolderLookup;
import net.minecraft.tags.ItemTags;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.registry.ModToolSetComponentDirectory;

@Component
public class HoeItemTagProviderFactory implements IItemTagProviderFactory {

	@Autowired
	private ModToolSetComponentDirectory tools;

	@Override
	public void make(ItemTagProviderFactory.ItemTagsProviderAccessor accessor, HolderLookup.Provider provider) {
		accessor.tag(ItemTags.HOES).add(
			tools.titaniteToolSet().TITANITE_HOE.get()
		);
	}
}
