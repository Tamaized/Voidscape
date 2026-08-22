package tamaized.voidscape.datagen.data.tag.item;

import net.minecraft.core.HolderLookup;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.Tags;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.datagenutil.data.tag.ExposedKeyTagProvider;
import tamaized.datagenutil.data.tag.TagProviderUtil;
import tamaized.voidscape.registry.ModToolSetComponentDirectory;

import java.util.List;

@Component
public class CrossbowItemTagProviderFactory implements IItemTagProviderFactory {

	@Autowired
	private ModToolSetComponentDirectory tools;

	@Autowired
	private TagProviderUtil tagProviderUtil;

	@Override
	public void make(ExposedKeyTagProvider<Item> accessor, HolderLookup.Provider provider) {
		tagProviderUtil.tagMany(
			accessor::tag,
			appender -> appender.addAll(List.of(
				tools.voidicCrystalToolSet().VOIDIC_CRYSTAL_XBOW.getKey(),
				tools.corruptToolSet().CORRUPT_XBOW.getKey(),
				tools.titaniteToolSet().TITANITE_XBOW.getKey(),
				tools.ichorToolSet().ICHOR_XBOW.getKey(),
				tools.astralToolSet().ASTRAL_XBOW.getKey()
			)),
			Tags.Items.TOOLS_CROSSBOW,
			Tags.Items.RANGED_WEAPON_TOOLS,
			ItemTags.CROSSBOW_ENCHANTABLE,
			ItemTags.DURABILITY_ENCHANTABLE,
			ItemTags.VANISHING_ENCHANTABLE
		);
	}
}
