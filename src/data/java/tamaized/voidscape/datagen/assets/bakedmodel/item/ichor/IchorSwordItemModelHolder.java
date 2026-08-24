package tamaized.voidscape.datagen.assets.bakedmodel.item.ichor;

import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.assets.bakedmodel.item.BreakableFullbrightItemModelHolder;
import tamaized.voidscape.registry.ModToolSetComponentDirectory;

import java.util.Optional;

@Component
public class IchorSwordItemModelHolder extends BreakableFullbrightItemModelHolder {

	@Autowired
	private ModToolSetComponentDirectory tools;

	@Override
	protected DeferredHolder<Item, ? extends Item> itemForName() {
		return tools.ichorToolSet().ICHOR_SWORD;
	}

	@Override
	protected Identifier modelParent() {
		return Identifier.withDefaultNamespace("item/handheld");
	}

	@Override
	protected String texturePath() {
		return "item/ichor/sword";
	}

	@Override
	public Optional<String> lang() {
		return Optional.of("Ichor Sword");
	}
}
