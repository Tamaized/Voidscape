package tamaized.voidscape.datagen.assets.bakedmodel.item.titanite;

import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.assets.bakedmodel.item.BreakableBowItemModelHolder;
import tamaized.voidscape.registry.ModToolSetComponentDirectory;

import java.util.Optional;

@Component
public class TitaniteBowItemModelHolder extends BreakableBowItemModelHolder {

	@Autowired
	private ModToolSetComponentDirectory tools;

	@Override
	protected DeferredHolder<Item, ? extends Item> itemForName() {
		return tools.titaniteToolSet().TITANITE_BOW;
	}

	@Override
	protected String texturePath() {
		return "item/titanite/bow/bow";
	}

	@Override
	public Optional<String> lang() {
		return Optional.of("Titanite Bow");
	}
}
