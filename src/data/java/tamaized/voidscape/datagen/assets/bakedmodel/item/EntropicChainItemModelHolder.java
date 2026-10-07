package tamaized.voidscape.datagen.assets.bakedmodel.item;

import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.Nullable;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.registry.ModItemComponentDirectory;

import java.util.Optional;

@Component
public class EntropicChainItemModelHolder extends FullbrightItemModelHolder {

	@Autowired
	private ModItemComponentDirectory items;

	@Override
	protected @Nullable DeferredHolder<Item, ? extends Item> itemForName() {
		return items.materialItems().ENTROPIC_CHAIN;
	}

	@Override
	protected boolean hasOverlay() {
		return true;
	}

	@Override
	protected String fullbrightLayer() {
		return TextureSlot.LAYER1.getId();
	}

	@Override
	public Optional<String> lang() {
		return Optional.of("Entropic Chain");
	}
}
