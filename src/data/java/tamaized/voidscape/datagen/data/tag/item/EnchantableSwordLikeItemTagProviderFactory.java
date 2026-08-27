package tamaized.voidscape.datagen.data.tag.item;

import net.minecraft.core.HolderLookup;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.datagenutil.data.tag.ExposedKeyTagProvider;
import tamaized.datagenutil.data.tag.TagProviderUtil;
import tamaized.voidscape.registry.ModToolSetComponentDirectory;

import java.util.List;

@Component
public class EnchantableSwordLikeItemTagProviderFactory implements IItemTagProviderFactory {

	@Autowired
	private TagProviderUtil tagProviderUtil;

	@Autowired
	private ModToolSetComponentDirectory tools;

	@Override
	public void make(ExposedKeyTagProvider<Item> accessor, HolderLookup.Provider provider) {
		tagProviderUtil.tagMany(accessor::tag, appender -> appender.addAll(List.of(
			tools.voidicCrystalToolSet().VOIDIC_CRYSTAL_AXE.getKey(),
			tools.charredToolSet().CHARRED_WARHAMMER.getKey(),
			tools.corruptToolSet().CORRUPT_AXE.getKey(),
			tools.titaniteToolSet().TITANITE_AXE.getKey(),
			tools.ichorToolSet().ICHOR_AXE.getKey(),
			tools.astralToolSet().ASTRAL_AXE.getKey()
		)), ItemTags.MELEE_WEAPON_ENCHANTABLE, ItemTags.SWEEPING_ENCHANTABLE);
	}

}
