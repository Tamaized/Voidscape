package tamaized.voidscape.datagen.assets.bakedmodel.item.titanite;

import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.assets.bakedmodel.item.BreakableFullbrightItemModelHolder;
import tamaized.voidscape.registry.ModArmorSetComponentDirectory;

import java.util.Optional;

@Component
public class TitaniteChestplateItemModelHolder extends BreakableFullbrightItemModelHolder {

	@Autowired
	private ModArmorSetComponentDirectory armor;

	@Override
	protected DeferredHolder<Item, ? extends Item> itemForName() {
		return armor.titaniteArmorSet().TITANITE_CHEST;
	}

	@Override
	protected String texturePath() {
		return "item/titanite/chest";
	}

	@Override
	public Optional<String> lang() {
		return Optional.of("Titanite Chestplate");
	}
}
