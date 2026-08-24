package tamaized.voidscape.datagen.assets.bakedmodel.item.titanite;

import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.assets.bakedmodel.item.BreakableFullbrightItemModelHolder;
import tamaized.voidscape.registry.ModToolSetComponentDirectory;

import java.util.Optional;

@Component
public class TitanitePickaxeItemModelHolder extends BreakableFullbrightItemModelHolder {

	@Autowired
	private ModToolSetComponentDirectory tools;

	@Override
	protected DeferredHolder<Item, ? extends Item> itemForName() {
		return tools.titaniteToolSet().TITANITE_PICKAXE;
	}

	@Override
	protected Identifier modelParent() {
		return Identifier.withDefaultNamespace("item/handheld");
	}

	@Override
	protected String texturePath() {
		return "item/titanite/pickaxe";
	}

	@Override
	public Optional<String> lang() {
		return Optional.of("Titanite Pickaxe");
	}
}
