package tamaized.voidscape.datagen.data.tag.item;

import net.minecraft.core.HolderLookup;
import net.minecraft.tags.ItemTags;
import net.neoforged.neoforge.common.Tags;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.util.TagProviderUtil;
import tamaized.voidscape.registry.ModToolSetComponentDirectory;

@Component
public class BowItemTagProviderFactory implements IItemTagProviderFactory {

	@Autowired
	private ModToolSetComponentDirectory tools;

	@Autowired
	private TagProviderUtil tagProviderUtil;

	@Override
	public void make(ItemTagProviderFactory.ItemTagsProviderAccessor accessor, HolderLookup.Provider provider) {
		tagProviderUtil.multiTagAll(
			accessor::tag,
			Tags.Items.TOOLS_BOW,
			Tags.Items.RANGED_WEAPON_TOOLS,
			ItemTags.BOW_ENCHANTABLE,
			ItemTags.DURABILITY_ENCHANTABLE,
			ItemTags.VANISHING_ENCHANTABLE
		).add(
			tools.voidicCrystalToolSet().VOIDIC_CRYSTAL_BOW.getKey(),
			tools.corruptToolSet().CORRUPT_BOW.getKey(),
			tools.titaniteToolSet().TITANITE_BOW.getKey(),
			tools.ichorToolSet().ICHOR_BOW.getKey(),
			tools.astralToolSet().ASTRAL_BOW.getKey()
		);
	}
}
