package tamaized.voidscape.datagen.assets.bakedmodel.item.astral;

import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.loaders.ItemLayerModelBuilder;
import org.jetbrains.annotations.Nullable;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.client.ItemModelOverridePredicates;
import tamaized.voidscape.datagen.assets.bakedmodel.ItemModelHolder;
import tamaized.voidscape.registry.ModArmorSetComponentDirectory;

@Component
public class AstralHelmetItemModelHolder extends ItemModelHolder {

	@Autowired
	private ModArmorSetComponentDirectory armor;

	@Autowired
	private ItemModelOverridePredicates itemModelOverridePredicates;

	@Nullable
	private ModelFile brokenModel;

	public ModelFile build(ItemModelProvider provider) {
		// @formatter:off
		return provider.withExistingParent(
				splitName(armor.astralArmorSet().ASTRAL_HELMET),
				"item/generated"
			)
			.texture("layer0", "item/astral/helmet")
			.customLoader(ItemLayerModelBuilder::begin).emissive(15, 15, 0).end()
			.override()
				.predicate(itemModelOverridePredicates.BROKEN, 1)
				.model(getBrokenModel(provider))
			.end();
		// @formatter:on
	}

	public ModelFile getBrokenModel(ItemModelProvider provider) {
		if (brokenModel == null) {
			brokenModel = provider.withExistingParent(
					splitName(armor.astralArmorSet().ASTRAL_HELMET) + "_broken",
					"item/generated"
				)
				.texture("layer0", "item/astral/helmet_broken")
				.customLoader(ItemLayerModelBuilder::begin).emissive(15, 15, 0).end();
		}
		return brokenModel;
	}

}
