package tamaized.voidscape.datagen.data.tag.item;

import net.minecraft.core.HolderLookup;
import net.minecraft.tags.ItemTags;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.registry.ModArmorSetComponentDirectory;

@Component
public class LegArmorItemTagProviderFactory implements IItemTagProviderFactory {

	@Autowired
	private ModArmorSetComponentDirectory armor;

	@Override
	public void make(ItemTagProviderFactory.ItemTagsProviderAccessor accessor, HolderLookup.Provider provider) {
		accessor.tag(ItemTags.LEG_ARMOR).add(
			armor.voidicCrystalArmorSet().VOIDIC_CRYSTAL_LEGS.get(),
			armor.corruptArmorSet().CORRUPT_LEGS.get(),
			armor.titaniteArmorSet().TITANITE_LEGS.get(),
			armor.ichorArmorSet().ICHOR_LEGS.get(),
			armor.astralArmorSet().ASTRAL_LEGS.get()
		);
	}
}
