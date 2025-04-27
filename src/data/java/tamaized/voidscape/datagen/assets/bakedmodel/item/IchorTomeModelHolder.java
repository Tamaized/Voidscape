package tamaized.voidscape.datagen.assets.bakedmodel.item;

import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.loaders.ItemLayerModelBuilder;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.Nullable;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.assets.bakedmodel.ItemModelHolder;
import tamaized.voidscape.registry.ModItemComponentDirectory;

@Component
public class IchorTomeModelHolder extends ItemModelHolder {

	@Autowired
	private ModItemComponentDirectory items;

	@Override
	protected @Nullable DeferredHolder<Item, ? extends Item> itemForName() {
		return items.toolSetComponentDirectory().spellTomeSet().ICHOR_TOME;
	}

	@Override
	public ModelFile build(ItemModelProvider provider) {
		return provider.withExistingParent(name(), "item/generated")
			.customLoader(ItemLayerModelBuilder::begin).emissive(15, 15, 0).end()
			.texture("layer0", "item/tome_ichor");
	}
}
