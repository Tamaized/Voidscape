package tamaized.voidscape.datagen.data.tag.item;

import net.minecraft.core.HolderLookup;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.datagenutil.data.tag.ExposedKeyTagProvider;
import tamaized.voidscape.registry.ModArmorSetComponentDirectory;

@Component
public class LegArmorItemTagProviderFactory implements IItemTagProviderFactory {

	@Autowired
	private ModArmorSetComponentDirectory armor;

	@Override
	public void make(ExposedKeyTagProvider<Item> accessor, HolderLookup.Provider provider) {
		accessor.tag(ItemTags.LEG_ARMOR).add(
			armor.voidicCrystalArmorSet().VOIDIC_CRYSTAL_LEGS.getKey(),
			armor.corruptArmorSet().CORRUPT_LEGS.getKey(),
			armor.titaniteArmorSet().TITANITE_LEGS.getKey(),
			armor.ichorArmorSet().ICHOR_LEGS.getKey(),
			armor.astralArmorSet().ASTRAL_LEGS.getKey()
		);
	}
}
