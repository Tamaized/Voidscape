package tamaized.voidscape.datagen.assets.bakedmodel.item.charred;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.loaders.ItemLayerModelBuilder;
import net.neoforged.neoforge.client.model.generators.loaders.SeparateTransformsModelBuilder;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.Nullable;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.client.ItemModelOverridePredicates;
import tamaized.voidscape.datagen.assets.bakedmodel.ItemModelHolder;
import tamaized.voidscape.registry.ModToolSetComponentDirectory;

import java.util.Optional;

@Component
public class CharredWarhammerItemModelHolder extends ItemModelHolder {

	@Autowired
	private ModToolSetComponentDirectory tools;

	@Autowired
	private ItemModelOverridePredicates itemModelOverridePredicates;

	@Nullable
	private ItemModelBuilder smallModel;

	@Nullable
	private ModelFile brokenSmallModel;

	@Nullable
	private ItemModelBuilder bigModel;

	@Nullable
	private ModelFile brokenBigModel;

	@Override
	protected DeferredHolder<Item, ? extends Item> itemForName() {
		return tools.charredToolSet().CHARRED_WARHAMMER;
	}

	public ModelFile build(ItemModelProvider provider) {
		return provider.withExistingParent(
				name(),
				"item/handheld"
			)
			.customLoader(SeparateTransformsModelBuilder::begin)
			// @formatter:off
				.base(getSmallModel(provider))
				.perspective(ItemDisplayContext.FIRST_PERSON_LEFT_HAND, getBigModel(provider))
				.perspective(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND, getBigModel(provider))
				.perspective(ItemDisplayContext.THIRD_PERSON_LEFT_HAND, getBigModel(provider))
				.perspective(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, getBigModel(provider))
			// @formatter:on
			.end();
	}

	public ItemModelBuilder getSmallModel(ItemModelProvider provider) {
		if (smallModel == null) {
			smallModel = provider.withExistingParent(
					name("small"),
					"item/handheld"
				)
				.texture("layer0", "item/charred/warhammer_small")
				.customLoader(ItemLayerModelBuilder::begin).emissive(15, 15, 0).end()
				.override()
				// @formatter:off
					.predicate(itemModelOverridePredicates.BROKEN, 1)
					.model(getBrokenSmallModel(provider))
				// @formatter:on
				.end();
		}
		return smallModel;
	}

	public ModelFile getBrokenSmallModel(ItemModelProvider provider) {
		if (brokenSmallModel == null) {
			brokenSmallModel = provider.withExistingParent(
					name("small_broken"),
					"item/handheld"
				)
				.texture("layer0", "item/charred/warhammer_small_broken")
				.customLoader(ItemLayerModelBuilder::begin).emissive(15, 15, 0).end();
		}
		return brokenSmallModel;
	}

	public ItemModelBuilder getBigModel(ItemModelProvider provider) {
		if (bigModel == null) {
			bigModel = provider.withExistingParent(
					name("in_hand"),
					"item/handheld"
				)
				.texture("layer0", "item/charred/warhammer")
				.customLoader(ItemLayerModelBuilder::begin).emissive(15, 15, 0).end()
				.transforms()
				// @formatter:off
					.transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND)
						.rotation(0, -90, 55)
						.translation(0, 7, 0.5F)
						.scale(1.5F, 1.5F, 1F)
					.end()
					.transform(ItemDisplayContext.THIRD_PERSON_LEFT_HAND)
						.rotation(0, 90, -55)
						.translation(0, 7, 0.5F)
						.scale(1.5F, 1.5F, 1F)
					.end()
					.transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND)
						.rotation(0, -90, 25)
						.translation(1.13F, 3.2F, 1.13F)
						.scale(1.25F, 1.25F, 1F)
					.end()
					.transform(ItemDisplayContext.FIRST_PERSON_LEFT_HAND)
						.rotation(0, 90, -25)
						.translation(1.13F, 3.2F, 1.13F)
						.scale(1.25F, 1.25F, 1F)
					.end()
					.transform(ItemDisplayContext.GROUND)
						.rotation(0, 3, 0)
						.scale(0.8F, 0.8F, 0.6F)
					.end()
				// @formatter:on
				.end()
				.override()
				// @formatter:off
					.predicate(itemModelOverridePredicates.BROKEN, 1)
					.model(getBrokenBigModel(provider))
				// @formatter:on
				.end();
		}
		return bigModel;
	}

	public ModelFile getBrokenBigModel(ItemModelProvider provider) {
		if (brokenBigModel == null) {
			brokenBigModel = provider.withExistingParent(
					name("in_hand_broken"),
					"item/handheld"
				)
				.texture("layer0", "item/charred/warhammer_broken")
				.customLoader(ItemLayerModelBuilder::begin).emissive(15, 15, 0).end()
				.transforms()
				// @formatter:off
					.transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND)
						.rotation(0, -90, 55)
						.translation(0, 7, 0.5F)
						.scale(1.5F, 1.5F, 1F)
					.end()
					.transform(ItemDisplayContext.THIRD_PERSON_LEFT_HAND)
						.rotation(0, 90, -55)
						.translation(0, 7, 0.5F)
						.scale(1.5F, 1.5F, 1F)
					.end()
					.transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND)
						.rotation(0, -90, 25)
						.translation(1.13F, 3.2F, 1.13F)
						.scale(1.25F, 1.25F, 1F)
					.end()
					.transform(ItemDisplayContext.FIRST_PERSON_LEFT_HAND)
						.rotation(0, 90, -25)
						.translation(1.13F, 3.2F, 1.13F)
						.scale(1.25F, 1.25F, 1F)
					.end()
					.transform(ItemDisplayContext.GROUND)
						.rotation(0, 3, 0)
						.scale(0.8F, 0.8F, 0.6F)
					.end()
				// @formatter:on
				.end();
		}
		return brokenBigModel;
	}

	@Override
	public Optional<String> lang() {
		return Optional.of("Charred War Hammer");
	}

}
