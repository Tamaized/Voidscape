package tamaized.voidscape.datagen.data.tag.item;

import net.minecraft.core.HolderLookup;
import net.minecraft.tags.ItemTags;
import net.neoforged.neoforge.common.Tags;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.util.TagProviderUtil;
import tamaized.voidscape.registry.ModToolSetComponentDirectory;

@Component
public class ShieldItemTagProviderFactory implements IItemTagProviderFactory {

	@Autowired
	private ModToolSetComponentDirectory tools;

	@Autowired
	private TagProviderUtil tagProviderUtil;

	@Override
	public void make(ItemTagProviderFactory.ItemTagsProviderAccessor accessor, HolderLookup.Provider provider) {
		tagProviderUtil.multiTagAll(
			accessor::tag,
			Tags.Items.TOOLS_SHIELD,
			ItemTags.DURABILITY_ENCHANTABLE
		).add(
			tools.voidicCrystalToolSet().VOIDIC_CRYSTAL_SHIELD.getKey()
		);
	}
}
