package tamaized.voidscape.datagen.assets.bakedmodel.item.charred;

import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.Nullable;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.assets.bakedmodel.item.FullbrightItemModelHolder;
import tamaized.voidscape.registry.ModItemComponentDirectory;

import java.util.Optional;

@Component
public class CharredQuiverItemModelHolder extends FullbrightItemModelHolder {

	@Autowired
	private ModItemComponentDirectory items;

	@Override
	protected @Nullable DeferredHolder<Item, ? extends Item> itemForName() {
		return items.toolSetComponentDirectory().charredToolSet().CHARRED_QUIVER;
	}

	@Override
	protected String texturePath() {
		return "item/charred/quiver";
	}

	@Override
	public Optional<String> lang() {
		return Optional.of("Charred Quiver");
	}
}
