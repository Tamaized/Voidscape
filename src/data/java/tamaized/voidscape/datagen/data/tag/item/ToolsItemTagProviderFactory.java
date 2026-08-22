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
public class ToolsItemTagProviderFactory implements IItemTagProviderFactory {

	@Autowired
	private ModToolSetComponentDirectory tools;

	@Override
	public void make(ExposedKeyTagProvider<Item> accessor, HolderLookup.Provider provider) {
		accessor.tag(ItemTags.DURABILITY_ENCHANTABLE).addAll(List.of(
			tools.spellTomeSet().VOIDIC_TOME.getKey(),
			tools.spellTomeSet().CORRUPT_TOME.getKey(),
			tools.spellTomeSet().TITANITE_TOME.getKey(),
			tools.spellTomeSet().ICHOR_TOME.getKey()
		));
	}
}
