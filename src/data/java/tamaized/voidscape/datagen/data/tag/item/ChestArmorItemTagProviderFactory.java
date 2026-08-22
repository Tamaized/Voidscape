package tamaized.voidscape.datagen.data.tag.item;

import net.minecraft.core.HolderLookup;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.datagenutil.data.tag.ExposedKeyTagProvider;
import tamaized.voidscape.registry.ModArmorSetComponentDirectory;

import java.util.List;

@Component
public class ChestArmorItemTagProviderFactory implements IItemTagProviderFactory {

	@Autowired
	private ModArmorSetComponentDirectory armor;

	@Override
	public void make(ExposedKeyTagProvider<Item> accessor, HolderLookup.Provider provider) {
		accessor.tag(ItemTags.CHEST_ARMOR).addAll(List.of(
			armor.voidicCrystalArmorSet().VOIDIC_CRYSTAL_CHEST.getKey(),
			armor.corruptArmorSet().CORRUPT_CHEST.getKey(),
			armor.titaniteArmorSet().TITANITE_CHEST.getKey(),
			armor.ichorArmorSet().ICHOR_CHEST.getKey(),
			armor.astralArmorSet().ASTRAL_CHEST.getKey()
		));
	}
}
