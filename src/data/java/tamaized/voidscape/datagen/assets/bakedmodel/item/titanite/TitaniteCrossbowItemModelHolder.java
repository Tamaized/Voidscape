package tamaized.voidscape.datagen.assets.bakedmodel.item.titanite;

import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.assets.bakedmodel.item.BreakableCrossbowItemModelHolder;
import tamaized.voidscape.registry.ModToolSetComponentDirectory;

import java.util.Optional;

@Component
public class TitaniteCrossbowItemModelHolder extends BreakableCrossbowItemModelHolder {

	@Autowired
	private ModToolSetComponentDirectory tools;

	@Override
	protected DeferredHolder<Item, ? extends Item> itemForName() {
		return tools.titaniteToolSet().TITANITE_XBOW;
	}

	@Override
	protected String texturePath() {
		return "item/titanite/xbow/crossbow";
	}

	@Override
	public Optional<String> lang() {
		return Optional.of("Titanite Crossbow");
	}
}
