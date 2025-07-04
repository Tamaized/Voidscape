package tamaized.voidscape.datagen.data.tag.item;

import net.minecraft.core.HolderLookup;
import net.minecraft.tags.ItemTags;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.registry.ModToolSetComponentDirectory;

@Component
public class BowItemTagProviderFactory implements IItemTagProviderFactory {

	@Autowired
	private ModToolSetComponentDirectory tools;

	@Override
	public void make(ItemTagProviderFactory.ItemTagsProviderAccessor accessor, HolderLookup.Provider provider) {
		accessor.tag(ItemTags.BOW_ENCHANTABLE).add(
			tools.voidicCrystalToolSet().VOIDIC_CRYSTAL_BOW.get(),
			tools.corruptToolSet().CORRUPT_BOW.get(),
			tools.titaniteToolSet().TITANITE_BOW.get(),
			tools.ichorToolSet().ICHOR_BOW.get(),
			tools.astralToolSet().ASTRAL_BOW.get()
		);
	}
}
