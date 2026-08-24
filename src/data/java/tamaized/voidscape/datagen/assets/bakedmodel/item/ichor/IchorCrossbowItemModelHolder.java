package tamaized.voidscape.datagen.assets.bakedmodel.item.ichor;

import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.assets.bakedmodel.item.BreakableCrossbowItemModelHolder;
import tamaized.voidscape.registry.ModToolSetComponentDirectory;

import java.util.Optional;

@Component
public class IchorCrossbowItemModelHolder extends BreakableCrossbowItemModelHolder {

	@Autowired
	private ModToolSetComponentDirectory tools;

	@Override
	protected DeferredHolder<Item, ? extends Item> itemForName() {
		return tools.ichorToolSet().ICHOR_XBOW;
	}

	@Override
	protected String texturePath() {
		return "item/ichor/xbow/crossbow";
	}

	@Override
	public Optional<String> lang() {
		return Optional.of("Ichor Crossbow");
	}
}
