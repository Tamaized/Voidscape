package tamaized.voidscape.datagen.assets.bakedmodel.item.voidic;

import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.assets.bakedmodel.item.BreakableFullbrightItemModelHolder;
import tamaized.voidscape.registry.ModArmorSetComponentDirectory;

import java.util.Optional;

@Component
public class VoidicCrystalChestplateItemModelHolder extends BreakableFullbrightItemModelHolder {

	@Autowired
	private ModArmorSetComponentDirectory armor;

	@Override
	protected DeferredHolder<Item, ? extends Item> itemForName() {
		return armor.voidicCrystalArmorSet().VOIDIC_CRYSTAL_CHEST;
	}

	@Override
	protected String texturePath() {
		return "item/voidic/chest";
	}

	@Override
	public Optional<String> lang() {
		return Optional.of("Voidic Crystal Chestplate");
	}
}
