package tamaized.voidscape.datagen.assets.bakedmodel.item.corrupt;

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
public class CorruptBowItemModelHolder extends ItemModelHolder {

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

	@Override
	protected DeferredHolder<Item, ? extends Item> itemForName() {
		return tools.corruptToolSet().CORRUPT_BOW;
	}

	public ModelFile build(ItemModelProvider provider) {
		// @formatter:off
		return provider.withExistingParent(
				name(),
				"item/generated"
			)
			.texture("layer0", "item/corrupt/bow/bow")
			.customLoader(ItemLayerModelBuilder::begin).emissive(15, 15, 0).end()
			.transforms()
				.transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND)
					.rotation(-80, 260, -40)
					.translation(-1, -2, 2.5F)
					.scale(0.9F)
				.end()
				.transform(ItemDisplayContext.THIRD_PERSON_LEFT_HAND)
					.rotation(-80, -280, 40)
					.translation(-1, -2, 2.5F)
					.scale(0.9F)
				.end()
				.transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND)
					.rotation(0, -90, 25)
					.translation(1.13F, 3.2F, 1.13F)
					.scale(0.68F)
				.end()
				.transform(ItemDisplayContext.FIRST_PERSON_LEFT_HAND)
					.rotation(0, 90, -25)
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
				.predicate(itemModelOverridePredicates.PULL, 0.65F)
				.model(getPulling1Model(provider))
			.end()
			.override()
				.predicate(itemModelOverridePredicates.PULLING, 1)
				.predicate(itemModelOverridePredicates.PULL, 0.9F)
				.model(getPulling2Model(provider))
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

	private ModelFile makeAdditionalModel(ItemModelProvider provider, String name) {
		return provider.withExistingParent(
				name(name),
				"item/bow"
			)
			.texture("layer0", "item/corrupt/bow/bow_" + name)
			.customLoader(ItemLayerModelBuilder::begin).emissive(15, 15, 0).end();
	}

	@Override
	public Optional<String> lang() {
		return Optional.of("Corrupt Bow");
	}

}
