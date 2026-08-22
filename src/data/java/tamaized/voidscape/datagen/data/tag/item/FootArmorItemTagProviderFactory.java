package tamaized.voidscape.datagen.data.tag.item;

import net.minecraft.core.HolderLookup;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.datagenutil.data.tag.ExposedKeyTagProvider;
import tamaized.voidscape.registry.ModArmorSetComponentDirectory;

@Component
public class FootArmorItemTagProviderFactory implements IItemTagProviderFactory {

	@Autowired
	private ModArmorSetComponentDirectory armor;

	@Override
	public void make(ExposedKeyTagProvider<Item> accessor, HolderLookup.Provider provider) {
		accessor.tag(ItemTags.FOOT_ARMOR).add(
			armor.voidicCrystalArmorSet().VOIDIC_CRYSTAL_BOOTS.getKey(),
			armor.corruptArmorSet().CORRUPT_BOOTS.getKey(),
			armor.titaniteArmorSet().TITANITE_BOOTS.getKey(),
			armor.ichorArmorSet().ICHOR_BOOTS.getKey(),
			armor.astralArmorSet().ASTRAL_BOOTS.getKey()
		);
	}
}
