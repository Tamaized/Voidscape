package tamaized.voidscape.datagen.data.tag.item;

import net.minecraft.core.HolderLookup;
import net.minecraft.tags.ItemTags;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.registry.ModArmorSetComponentDirectory;

@Component
public class HeadArmorItemTagProviderFactory implements IItemTagProviderFactory {

	@Autowired
	private ModArmorSetComponentDirectory armor;

	@Override
	public void make(ItemTagProviderFactory.ItemTagsProviderAccessor accessor, HolderLookup.Provider provider) {
		accessor.tag(ItemTags.HEAD_ARMOR).add(
			armor.voidicCrystalArmorSet().VOIDIC_CRYSTAL_HELMET.get(),
			armor.corruptArmorSet().CORRUPT_HELMET.get(),
			armor.titaniteArmorSet().TITANITE_HELMET.get(),
			armor.ichorArmorSet().ICHOR_HELMET.get(),
			armor.astralArmorSet().ASTRAL_HELMET.get()
		);
	}
}
