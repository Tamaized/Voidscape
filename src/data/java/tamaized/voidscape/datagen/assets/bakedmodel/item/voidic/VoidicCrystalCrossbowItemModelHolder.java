package tamaized.voidscape.datagen.assets.bakedmodel.item.voidic;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
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
public class VoidicCrystalCrossbowItemModelHolder extends VoidicCrystalItemModelHolder {

	@Autowired
	private ModToolSetComponentDirectory tools;

	@Autowired
	private ItemModelOverridePredicates itemModelOverridePredicates;

	@Nullable
	private ModelFile brokenModel;

	@Nullable
	private ModelFile pulling0Model;

	@Nullable
	private ModelFile pulling1Model;

	@Nullable
	private ModelFile pulling2Model;

	@Nullable
	private ModelFile arrowModel;

	@Nullable
	private ModelFile fireworkModel;

	@Override
	protected DeferredHolder<Item, ? extends Item> itemForName() {
		return tools.voidicCrystalToolSet().VOIDIC_CRYSTAL_XBOW;
	}

	public ModelFile build(ItemModelProvider provider) {
		// @formatter:off
		return provider.withExistingParent(
				splitName(),
				"item/generated"
			)
			.texture("layer0", "item/voidic/xbow/crossbow_standby")
			.customLoader(ItemLayerModelBuilder::begin).emissive(15, 15, 0).end()
			.transforms()
				.transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND)
					.rotation(-90, 0, -60)
					.translation(2, 0.1F, -3)
					.scale(0.9F)
				.end()
				.transform(ItemDisplayContext.THIRD_PERSON_LEFT_HAND)
					.rotation(-90, 0, 30)
					.translation(2, 0.1F, -3)
					.scale(0.9F)
				.end()
				.transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND)
					.rotation(-90, 0, 30)
					.translation(1.13F, 3.2F, 1.13F)
					.scale(0.68F)
				.end()
				.transform(ItemDisplayContext.FIRST_PERSON_LEFT_HAND)
					.rotation(-90, 0, 35)
					.translation(1.13F, 3.2F, 1.13F)
					.scale(0.68F)
				.end()
			.end()
			.override()
				.predicate(itemModelOverridePredicates.BROKEN, 1)
				.model(getBrokenModel(provider))
			.end()
			.override()
				.predicate(itemModelOverridePredicates.PULLING, 1)
				.model(getPulling0Model(provider))
			.end()
			.override()
				.predicate(itemModelOverridePredicates.PULLING, 1)
				.predicate(itemModelOverridePredicates.PULL, 0.58F)
				.model(getPulling1Model(provider))
			.end()
			.override()
				.predicate(itemModelOverridePredicates.PULLING, 1)
				.predicate(itemModelOverridePredicates.PULL, 1)
				.model(getPulling2Model(provider))
			.end()
			.override()
				.predicate(itemModelOverridePredicates.CHARGED, 1)
				.model(getArrowModel(provider))
			.end()
			.override()
				.predicate(itemModelOverridePredicates.CHARGED, 1)
				.predicate(itemModelOverridePredicates.FIREWORK, 1)
				.model(getFireworkModel(provider))
			.end();
		// @formatter:on
	}

	public ModelFile getBrokenModel(ItemModelProvider provider) {
		if (brokenModel == null) {
			brokenModel = makeAdditionalModel(provider, "broken");
		}
		return brokenModel;
	}

	public ModelFile getPulling0Model(ItemModelProvider provider) {
		if (pulling0Model == null) {
			pulling0Model = makeAdditionalModel(provider, "pulling_0");
		}
		return pulling0Model;
	}

	public ModelFile getPulling1Model(ItemModelProvider provider) {
		if (pulling1Model == null) {
			pulling1Model = makeAdditionalModel(provider, "pulling_1");
		}
		return pulling1Model;
	}

	public ModelFile getPulling2Model(ItemModelProvider provider) {
		if (pulling2Model == null) {
			pulling2Model = makeAdditionalModel(provider, "pulling_2");
		}
		return pulling2Model;
	}

	public ModelFile getArrowModel(ItemModelProvider provider) {
		if (arrowModel == null) {
			arrowModel = makeAdditionalModel(provider, "arrow");
		}
		return arrowModel;
	}

	public ModelFile getFireworkModel(ItemModelProvider provider) {
		if (fireworkModel == null) {
			fireworkModel = makeAdditionalModel(provider, "firework");
		}
		return fireworkModel;
	}

	private ModelFile makeAdditionalModel(ItemModelProvider provider, String name) {
		return provider.withExistingParent(
				splitName(name),
				"item/crossbow"
			)
			.texture("layer0", "item/voidic/xbow/crossbow_" + name)
			.customLoader(ItemLayerModelBuilder::begin).emissive(15, 15, 0).end();
	}

	@Override
	public Optional<String> lang() {
		return Optional.of("Voidic Crystal Crossbow");
	}

}
