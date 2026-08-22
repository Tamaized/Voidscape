package tamaized.voidscape.datagen.data.tag.item;

import net.minecraft.core.HolderLookup;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.datagenutil.data.tag.ExposedKeyTagProvider;
import tamaized.voidscape.registry.ModToolSetComponentDirectory;

@Component
public class SwordItemTagProviderFactory implements IItemTagProviderFactory {

	@Autowired
	private ModToolSetComponentDirectory tools;

	@Override
	public void make(ExposedKeyTagProvider<Item> accessor, HolderLookup.Provider provider) {
		accessor.tag(ItemTags.SWORDS).add(
			tools.voidicCrystalToolSet().VOIDIC_CRYSTAL_SWORD.getKey(),
			tools.corruptToolSet().CORRUPT_SWORD.getKey(),
			tools.titaniteToolSet().TITANITE_SWORD.getKey(),
			tools.ichorToolSet().ICHOR_SWORD.getKey(),
			tools.astralToolSet().ASTRAL_SWORD.getKey()
		);
	}
}
