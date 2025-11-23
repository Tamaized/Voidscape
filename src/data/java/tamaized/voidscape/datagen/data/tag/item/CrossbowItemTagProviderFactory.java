package tamaized.voidscape.datagen.data.tag.item;

import net.minecraft.core.HolderLookup;
import net.minecraft.tags.ItemTags;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.registry.ModToolSetComponentDirectory;

@Component
public class CrossbowItemTagProviderFactory implements IItemTagProviderFactory {

	@Autowired
	private ModToolSetComponentDirectory tools;

	@Override
	public void make(ItemTagProviderFactory.ItemTagsProviderAccessor accessor, HolderLookup.Provider provider) {
		accessor.tag(ItemTags.CROSSBOW_ENCHANTABLE).add(
			tools.voidicCrystalToolSet().VOIDIC_CRYSTAL_XBOW.get(),
			tools.corruptToolSet().CORRUPT_XBOW.get(),
			tools.titaniteToolSet().TITANITE_XBOW.get(),
			tools.ichorToolSet().ICHOR_XBOW.get(),
			tools.astralToolSet().ASTRAL_XBOW.get()
		);
	}
}
