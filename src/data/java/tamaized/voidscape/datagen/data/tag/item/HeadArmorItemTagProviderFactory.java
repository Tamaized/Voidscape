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
public class HeadArmorItemTagProviderFactory implements IItemTagProviderFactory {

	@Autowired
	private ModArmorSetComponentDirectory armor;

	@Override
	public void make(ExposedKeyTagProvider<Item> accessor, HolderLookup.Provider provider) {
		accessor.tag(ItemTags.HEAD_ARMOR).addAll(List.of(
			armor.voidicCrystalArmorSet().VOIDIC_CRYSTAL_HELMET.getKey(),
			armor.corruptArmorSet().CORRUPT_HELMET.getKey(),
			armor.titaniteArmorSet().TITANITE_HELMET.getKey(),
			armor.ichorArmorSet().ICHOR_HELMET.getKey(),
			armor.astralArmorSet().ASTRAL_HELMET.getKey()
		));
	}
}
