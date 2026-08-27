package tamaized.voidscape.datagen.data.tag.item;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.Item;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.datagenutil.data.tag.ExposedKeyTagProvider;
import tamaized.voidscape.registry.ModItemTags;
import tamaized.voidscape.registry.item.MaterialItems;

@Component
public class RepairMaterialItemTagProviderFactory implements IItemTagProviderFactory {

	@Autowired
	private ModItemTags itemTags;

	@Autowired
	private MaterialItems materialItems;

	@Override
	public void make(ExposedKeyTagProvider<Item> accessor, HolderLookup.Provider provider) {
		accessor.tag(itemTags.REPAIR_MATERIAL_VOIDIC_CRYSTAL).add(
			materialItems.VOIDIC_CRYSTAL.getKey()
		);

		accessor.tag(itemTags.REPAIR_MATERIAL_CHARRED).add(
			materialItems.CHARRED_BONE.getKey()
		);

		accessor.tag(itemTags.REPAIR_MATERIAL_CORRUPT).add(
			materialItems.TENDRIL.getKey()
		);

		accessor.tag(itemTags.REPAIR_MATERIAL_TITANITE).add(
			materialItems.TITANITE_SHARD.getKey()
		);

		accessor.tag(itemTags.REPAIR_MATERIAL_ICHOR).add(
			materialItems.ICHOR_CRYSTAL.getKey()
		);

		accessor.tag(itemTags.REPAIR_MATERIAL_ASTRAL).add(
			materialItems.ASTRAL_CRYSTAL.getKey()
		);
	}
}
