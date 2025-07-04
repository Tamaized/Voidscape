package tamaized.voidscape.datagen.data.tag.item;

import net.minecraft.core.HolderLookup;
import net.minecraft.tags.ItemTags;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.registry.ModArmorSetComponentDirectory;
import tamaized.voidscape.registry.ModToolSetComponentDirectory;

@Component
public class FootArmorItemTagProviderFactory implements IItemTagProviderFactory {

	@Autowired
	private ModArmorSetComponentDirectory armor;

	@Override
	public void make(ItemTagProviderFactory.ItemTagsProviderAccessor accessor, HolderLookup.Provider provider) {
		accessor.tag(ItemTags.FOOT_ARMOR).add(
			armor.voidicCrystalArmorSet().VOIDIC_CRYSTAL_BOOTS.get(),
			armor.corruptArmorSet().CORRUPT_BOOTS.get(),
			armor.titaniteArmorSet().TITANITE_BOOTS.get(),
			armor.ichorArmorSet().ICHOR_BOOTS.get(),
			armor.astralArmorSet().ASTRAL_BOOTS.get()
		);
	}
}
