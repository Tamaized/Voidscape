package tamaized.voidscape.datagen.data.tag.item;

import net.minecraft.core.HolderLookup;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.datagenutil.data.tag.ExposedKeyTagProvider;
import tamaized.voidscape.registry.ModToolSetComponentDirectory;

@Component
public class PickaxeItemTagProviderFactory implements IItemTagProviderFactory {

	@Autowired
	private ModToolSetComponentDirectory tools;

	@Override
	public void make(ExposedKeyTagProvider<Item> accessor, HolderLookup.Provider provider) {
		accessor.tag(ItemTags.PICKAXES).add(
			tools.voidicCrystalToolSet().VOIDIC_CRYSTAL_PICKAXE.getKey(),
			tools.charredToolSet().CHARRED_WARHAMMER.getKey(),
			tools.titaniteToolSet().TITANITE_PICKAXE.getKey(),
			tools.ichorToolSet().ICHOR_PICKAXE.getKey(),
			tools.astralToolSet().ASTRAL_PICKAXE.getKey()
		);
	}
}
