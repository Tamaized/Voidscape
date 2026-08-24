package tamaized.voidscape.datagen.assets.bakedmodel.item;

import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.item.DynamicFluidContainerModel;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.Nullable;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.datagenutil.assets.bakedmodel.ExtendedTextureMapping;
import tamaized.datagenutil.assets.bakedmodel.FurtherExtendedModelTemplateBuilder;
import tamaized.datagenutil.assets.bakedmodel.item.ItemModelHolder;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.registry.fluid.ModFluidBuckets;
import tamaized.voidscape.registry.fluid.ModFluids;

import java.util.Objects;
import java.util.Optional;

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
	public Identifier finalize(ItemModelGenerators provider, FurtherExtendedModelTemplateBuilder model) {
		provider.itemModelOutput.accept(
			Objects.requireNonNull(itemForName()).value(),
			new DynamicFluidContainerModel.Unbaked(
				new DynamicFluidContainerModel.Textures(
					Optional.empty(),
					Optional.of(new Material(Identifier.withDefaultNamespace("item/bucket"))),
					Optional.of(new Material(Identifier.fromNamespaceAndPath("neoforge", "item/mask/bucket_fluid"))),
					Optional.empty()
				),
				fluids.VOIDIC_SOURCE.get(),
				false,
				true,
				true
			)
		);
		return Identifier.fromNamespaceAndPath(Voidscape.MODID, name());
	}

	@Override
	protected void defineTextureSlots(ExtendedTextureMapping mapping) {
	}
}
