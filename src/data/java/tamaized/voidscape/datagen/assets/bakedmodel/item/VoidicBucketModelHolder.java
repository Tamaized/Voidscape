package tamaized.voidscape.datagen.assets.bakedmodel.item;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.loaders.DynamicFluidContainerModelBuilder;
import net.neoforged.neoforge.client.model.generators.loaders.ItemLayerModelBuilder;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.Nullable;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.assets.bakedmodel.ItemModelHolder;
import tamaized.voidscape.registry.fluid.ModFluidBuckets;
import tamaized.voidscape.registry.fluid.ModFluids;
import tamaized.voidscape.registry.tool.ModItemComponentDirectory;

@Component
public class VoidicBucketModelHolder extends ItemModelHolder {

	@Autowired
	private ModFluidBuckets fluidBuckets;

	@Autowired
	private ModFluids fluids;

	@Override
	protected @Nullable DeferredHolder<Item, ? extends Item> itemForName() {
		return fluidBuckets.VOIDIC;
	}

	@Override
	public ModelFile build(ItemModelProvider provider) {
		return provider.withExistingParent(name(), ResourceLocation.fromNamespaceAndPath("neoforge", "item/bucket"))
			.customLoader(DynamicFluidContainerModelBuilder::begin).fluid(fluids.VOIDIC_SOURCE.get()).end();
	}
}
