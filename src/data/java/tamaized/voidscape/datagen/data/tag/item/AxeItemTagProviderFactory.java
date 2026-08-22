package tamaized.voidscape.datagen.data.tag.item;

import net.minecraft.core.HolderLookup;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.datagenutil.data.tag.ExposedKeyTagProvider;
import tamaized.voidscape.registry.ModToolSetComponentDirectory;

import java.util.List;

@Component
public class AxeItemTagProviderFactory implements IItemTagProviderFactory {

	@Autowired
	private ModToolSetComponentDirectory tools;

	@Override
	public void make(ExposedKeyTagProvider<Item> accessor, HolderLookup.Provider provider) {
		accessor.tag(ItemTags.AXES).addAll(List.of(
			tools.voidicCrystalToolSet().VOIDIC_CRYSTAL_AXE.getKey(),
			tools.corruptToolSet().CORRUPT_AXE.getKey(),
			tools.titaniteToolSet().TITANITE_AXE.getKey(),
			tools.ichorToolSet().ICHOR_AXE.getKey(),
			tools.astralToolSet().ASTRAL_AXE.getKey()
		));
	}
}
