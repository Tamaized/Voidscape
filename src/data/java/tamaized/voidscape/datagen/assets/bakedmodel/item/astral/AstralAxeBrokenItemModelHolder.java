package tamaized.voidscape.datagen.assets.bakedmodel.item.astral;

import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.loaders.ItemLayerModelBuilder;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.client.ItemModelOverridePredicates;
import tamaized.voidscape.datagen.assets.bakedmodel.ItemModelHolder;
import tamaized.voidscape.registry.ModToolSetComponentDirectory;

@Component
public class AstralAxeBrokenItemModelHolder extends ItemModelHolder {

	@Autowired
	private ModToolSetComponentDirectory tools;

	@Autowired
	private ItemModelOverridePredicates itemModelOverridePredicates;

	public ModelFile build(ItemModelProvider provider) {
		return provider.withExistingParent(
				splitName(tools.astralToolSet().ASTRAL_AXE) + "_broken",
				"item/handheld"
			)
			.texture("layer0", "item/astral/axe_broken")
			.customLoader(ItemLayerModelBuilder::begin).emissive(15, 15, 0).end();
	}

}
