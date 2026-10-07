package tamaized.voidscape.datagen.assets.bakedmodel.item.entropic;

import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.assets.bakedmodel.item.BreakableFullbrightItemModelHolder;
import tamaized.voidscape.registry.ModToolSetComponentDirectory;

import java.util.Optional;

@Component
public class EntropicAxeItemModelHolder extends BreakableFullbrightItemModelHolder {

	@Autowired
	private ModToolSetComponentDirectory tools;

	@Override
	protected DeferredHolder<Item, ? extends Item> itemForName() {
		return tools.entropicToolSet().ENTROPIC_AXE;
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
	protected Identifier modelParent() {
		return Identifier.withDefaultNamespace("item/handheld");
	}

	@Override
	protected String texturePath() {
		return "item/entropic/axe";
	}

	@Override
	public Optional<String> lang() {
		return Optional.of("Entropic Axe");
	}
}
