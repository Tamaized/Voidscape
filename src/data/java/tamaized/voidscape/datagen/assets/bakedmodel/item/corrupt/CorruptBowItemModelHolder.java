package tamaized.voidscape.datagen.assets.bakedmodel.item.corrupt;

import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.assets.bakedmodel.item.BreakableBowItemModelHolder;
import tamaized.voidscape.registry.ModToolSetComponentDirectory;

import java.util.Optional;

@Component
public class CorruptBowItemModelHolder extends BreakableBowItemModelHolder {

	@Autowired
	private ModToolSetComponentDirectory tools;

	@Override
	protected DeferredHolder<Item, ? extends Item> itemForName() {
		return tools.corruptToolSet().CORRUPT_BOW;
	}

	@Override
	protected String texturePath() {
		return "item/corrupt/bow/bow";
	}

	@Override
	public Optional<String> lang() {
		return Optional.of("Corrupt Bow");
	}
}
