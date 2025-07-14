package tamaized.voidscape.datagen.data.tag.item;

import net.minecraft.core.HolderLookup;
import net.minecraft.tags.ItemTags;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.registry.ModToolSetComponentDirectory;

@Component
public class PickaxeItemTagProviderFactory implements IItemTagProviderFactory {

	@Autowired
	private ModToolSetComponentDirectory tools;

	@Override
	public void make(ItemTagProviderFactory.ItemTagsProviderAccessor accessor, HolderLookup.Provider provider) {
		accessor.tag(ItemTags.PICKAXES).add(
			tools.voidicCrystalToolSet().VOIDIC_CRYSTAL_PICKAXE.get(),
			tools.charredToolSet().CHARRED_WARHAMMER.get(),
			tools.titaniteToolSet().TITANITE_PICKAXE.get(),
			tools.ichorToolSet().ICHOR_PICKAXE.get(),
			tools.astralToolSet().ASTRAL_PICKAXE.get()
		);
	}
}
