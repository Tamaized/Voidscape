package tamaized.voidscape.datagen.data.tag.item;

import net.minecraft.core.HolderLookup;
import net.minecraft.tags.ItemTags;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.registry.ModToolSetComponentDirectory;

@Component
public class SwordItemTagProviderFactory implements IItemTagProviderFactory {

	@Autowired
	private ModToolSetComponentDirectory tools;

	@Override
	public void make(ItemTagProviderFactory.ItemTagsProviderAccessor accessor, HolderLookup.Provider provider) {
		accessor.tag(ItemTags.SWORDS).add(
			tools.voidicCrystalToolSet().VOIDIC_CRYSTAL_SWORD.get(),
			tools.corruptToolSet().CORRUPT_SWORD.get(),
			tools.titaniteToolSet().TITANITE_SWORD.get(),
			tools.ichorToolSet().ICHOR_SWORD.get(),
			tools.astralToolSet().ASTRAL_SWORD.get()
		);
	}
}
