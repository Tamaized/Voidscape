package tamaized.voidscape.datagen.data.tag.item;

import net.minecraft.core.HolderLookup;
import net.minecraft.tags.ItemTags;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.registry.ModToolSetComponentDirectory;

@Component
public class AxeItemTagProviderFactory implements IItemTagProviderFactory {

	@Autowired
	private ModToolSetComponentDirectory tools;

	@Override
	public void make(ItemTagProviderFactory.ItemTagsProviderAccessor accessor, HolderLookup.Provider provider) {
		accessor.tag(ItemTags.AXES).add(
			tools.voidicCrystalToolSet().VOIDIC_CRYSTAL_AXE.get(),
			tools.corruptToolSet().CORRUPT_AXE.get(),
			tools.titaniteToolSet().TITANITE_AXE.get(),
			tools.ichorToolSet().ICHOR_AXE.get(),
			tools.astralToolSet().ASTRAL_AXE.get()
		);
	}
}
