package tamaized.voidscape.datagen.data.tag.item;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.Item;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.datagenutil.data.tag.ExposedKeyTagProvider;
import tamaized.voidscape.registry.ModItemTags;
import tamaized.voidscape.registry.ModToolSetComponentDirectory;

import java.util.List;

@Component
public class VoidToolLadderItemTagProviderFactory implements IItemTagProviderFactory {

	@Autowired
	private ModItemTags itemTags;

	@Autowired
	private ModToolSetComponentDirectory tools;

	@Override
	public void make(ExposedKeyTagProvider<Item> accessor, HolderLookup.Provider provider) {
		accessor.tag(itemTags.VOIDIC_CRYSTAL_TOOLS).addAll(List.of(
			tools.voidicCrystalToolSet().VOIDIC_CRYSTAL_SWORD.getKey(),
			tools.voidicCrystalToolSet().VOIDIC_CRYSTAL_BOW.getKey(),
			tools.voidicCrystalToolSet().VOIDIC_CRYSTAL_XBOW.getKey(),
			tools.voidicCrystalToolSet().VOIDIC_CRYSTAL_SHIELD.getKey(),
			tools.voidicCrystalToolSet().VOIDIC_CRYSTAL_AXE.getKey(),
			tools.voidicCrystalToolSet().VOIDIC_CRYSTAL_PICKAXE.getKey()
		));

		accessor.tag(itemTags.CHARRED_TOOLS).add(
			tools.charredToolSet().CHARRED_WARHAMMER.getKey()
		);

		accessor.tag(itemTags.CORRUPT_TOOLS).addAll(List.of(
			tools.corruptToolSet().CORRUPT_SWORD.getKey(),
			tools.corruptToolSet().CORRUPT_BOW.getKey(),
			tools.corruptToolSet().CORRUPT_XBOW.getKey(),
			tools.corruptToolSet().CORRUPT_AXE.getKey()
		));

		accessor.tag(itemTags.TITANITE_TOOLS).addAll(List.of(
			tools.titaniteToolSet().TITANITE_HOE.getKey(),
			tools.titaniteToolSet().TITANITE_SWORD.getKey(),
			tools.titaniteToolSet().TITANITE_BOW.getKey(),
			tools.titaniteToolSet().TITANITE_XBOW.getKey(),
			tools.titaniteToolSet().TITANITE_AXE.getKey(),
			tools.titaniteToolSet().TITANITE_PICKAXE.getKey()
		));

		accessor.tag(itemTags.ICHOR_TOOLS).addAll(List.of(
			tools.ichorToolSet().ICHOR_SWORD.getKey(),
			tools.ichorToolSet().ICHOR_BOW.getKey(),
			tools.ichorToolSet().ICHOR_XBOW.getKey(),
			tools.ichorToolSet().ICHOR_AXE.getKey(),
			tools.ichorToolSet().ICHOR_PICKAXE.getKey()
		));

		accessor.tag(itemTags.ASTRAL_TOOLS).addAll(List.of(
			tools.astralToolSet().ASTRAL_SWORD.getKey(),
			tools.astralToolSet().ASTRAL_AXE.getKey(),
			tools.astralToolSet().ASTRAL_PICKAXE.getKey(),
			tools.astralToolSet().ASTRAL_SHOVEL.getKey(),
			tools.astralToolSet().ASTRAL_BOW.getKey(),
			tools.astralToolSet().ASTRAL_XBOW.getKey()
		));
	}
}
