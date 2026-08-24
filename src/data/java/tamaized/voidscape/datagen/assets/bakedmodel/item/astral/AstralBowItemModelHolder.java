package tamaized.voidscape.datagen.assets.bakedmodel.item.astral;

import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.assets.bakedmodel.item.BreakableBowItemModelHolder;
import tamaized.voidscape.registry.ModToolSetComponentDirectory;

import java.util.Optional;

@Component
public class AstralBowItemModelHolder extends BreakableBowItemModelHolder {

	@Autowired
	private ModToolSetComponentDirectory tools;

	@Override
	protected DeferredHolder<Item, ? extends Item> itemForName() {
		return tools.astralToolSet().ASTRAL_BOW;
	}

	@Override
	protected String texturePath() {
		return "item/astral/bow/bow";
	}

	@Override
	public Optional<String> lang() {
		return Optional.of("Astral Bow");
	}
}
