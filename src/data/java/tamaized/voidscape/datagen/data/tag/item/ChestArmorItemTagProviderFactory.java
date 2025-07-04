package tamaized.voidscape.datagen.data.tag.item;

import net.minecraft.core.HolderLookup;
import net.minecraft.tags.ItemTags;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.registry.ModArmorSetComponentDirectory;

@Component
public class ChestArmorItemTagProviderFactory implements IItemTagProviderFactory {

	@Autowired
	private ModArmorSetComponentDirectory armor;

	@Override
	public void make(ItemTagProviderFactory.ItemTagsProviderAccessor accessor, HolderLookup.Provider provider) {
		accessor.tag(ItemTags.CHEST_ARMOR).add(
			armor.voidicCrystalArmorSet().VOIDIC_CRYSTAL_CHEST.get(),
			armor.corruptArmorSet().CORRUPT_CHEST.get(),
			armor.titaniteArmorSet().TITANITE_CHEST.get(),
			armor.ichorArmorSet().ICHOR_CHEST.get(),
			armor.astralArmorSet().ASTRAL_CHEST.get()
		);
	}
}
