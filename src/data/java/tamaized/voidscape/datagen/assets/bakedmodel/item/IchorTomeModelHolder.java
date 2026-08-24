package tamaized.voidscape.datagen.assets.bakedmodel.item;

import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.Nullable;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.registry.ModItemComponentDirectory;

import java.util.Optional;

@Component
public class IchorTomeModelHolder extends FullbrightItemModelHolder {

	@Autowired
	private ModItemComponentDirectory items;

	@Override
	protected @Nullable DeferredHolder<Item, ? extends Item> itemForName() {
		return items.toolSetComponentDirectory().spellTomeSet().ICHOR_TOME;
	}

	@Override
	protected String texturePath() {
		return "item/tome_ichor";
	}

	@Override
	public Optional<String> lang() {
		return Optional.of("Ichor Spell Tome");
	}
}
