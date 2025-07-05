package tamaized.voidscape.datagen.assets.bakedmodel.item.astral;

import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.loaders.ItemLayerModelBuilder;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.Nullable;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.client.ItemModelOverridePredicates;
import tamaized.voidscape.datagen.assets.bakedmodel.ItemModelHolder;
import tamaized.voidscape.registry.ModToolSetComponentDirectory;

import java.util.Optional;

@Component
public class AstralShovelItemModelHolder extends ItemModelHolder {

	@Autowired
	private ModToolSetComponentDirectory tools;

	@Autowired
	private ItemModelOverridePredicates itemModelOverridePredicates;

	@Nullable
	private ModelFile brokenModel;

	@Override
	protected DeferredHolder<Item, ? extends Item> itemForName() {
		return tools.astralToolSet().ASTRAL_SHOVEL;
	}

	@Override
	public Optional<String> lang() {
		return Optional.of("Astral Shovel");
	}

	public ModelFile build(ItemModelProvider provider) {
		// @formatter:off
		return provider.withExistingParent(
				name(),
				"item/handheld"
			)
			.texture("layer0", "item/astral/shovel")
			.customLoader(ItemLayerModelBuilder::begin).emissive(15, 15, 0).end()
			.override()
				.predicate(itemModelOverridePredicates.BROKEN, 1)
				.model(getBrokenModel(provider))
			.end();
		// @formatter:on
	}

	private ModelFile getBrokenModel(ItemModelProvider provider) {
		if (brokenModel == null) {
			brokenModel = provider.withExistingParent(
					name("broken"),
					"item/handheld"
				)
				.texture("layer0", "item/astral/shovel_broken")
				.customLoader(ItemLayerModelBuilder::begin).emissive(15, 15, 0).end();
		}
		return brokenModel;
	}

}
