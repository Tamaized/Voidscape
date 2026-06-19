package tamaized.voidscape.datagen.data.tag.item;

import net.minecraft.core.HolderLookup;
import net.minecraft.tags.ItemTags;
import net.neoforged.neoforge.common.Tags;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.util.TagProviderUtil;
import tamaized.voidscape.registry.ModToolSetComponentDirectory;

@Component
public class ToolsItemTagProviderFactory implements IItemTagProviderFactory {

	@Autowired
	private ModToolSetComponentDirectory tools;

	@Autowired
	private TagProviderUtil tagProviderUtil;

	@Override
	public void make(ItemTagProviderFactory.ItemTagsProviderAccessor accessor, HolderLookup.Provider provider) {
		tagProviderUtil.multiTagAll(
			accessor::tag,
			Tags.Items.TOOLS,
			ItemTags.DURABILITY_ENCHANTABLE
		).add(
			tools.spellTomeSet().VOIDIC_TOME.getKey(),
			tools.spellTomeSet().CORRUPT_TOME.getKey(),
			tools.spellTomeSet().TITANITE_TOME.getKey(),
			tools.spellTomeSet().ICHOR_TOME.getKey()
		);
	}
}
