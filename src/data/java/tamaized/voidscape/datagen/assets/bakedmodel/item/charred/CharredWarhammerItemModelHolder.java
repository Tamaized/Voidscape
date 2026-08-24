package tamaized.voidscape.datagen.assets.bakedmodel.item.charred;

import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.renderer.item.properties.conditional.Broken;
import net.minecraft.client.renderer.item.properties.select.DisplayContext;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.neoforged.neoforge.client.model.generators.template.ExtendedModelTemplateBuilder;
import net.neoforged.neoforge.registries.DeferredHolder;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.datagenutil.assets.bakedmodel.FurtherExtendedModelTemplateBuilder;
import tamaized.voidscape.datagen.assets.bakedmodel.item.BreakableFullbrightItemModelHolder;
import tamaized.voidscape.registry.ModToolSetComponentDirectory;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.UnaryOperator;

@Component
public class CharredWarhammerItemModelHolder extends BreakableFullbrightItemModelHolder {

	@Autowired
	private ModToolSetComponentDirectory tools;

	@Override
	protected DeferredHolder<Item, ? extends Item> itemForName() {
		return tools.charredToolSet().CHARRED_WARHAMMER;
	}

	@Override
	protected Identifier modelParent() {
		return Identifier.withDefaultNamespace("item/handheld");
	}

	@Override
	protected String texturePath() {
		return "item/charred/warhammer";
	}

	@Override
	public Identifier finalize(ItemModelGenerators provider, FurtherExtendedModelTemplateBuilder model) {
		Identifier small = makeModel(provider, name("small"), texturePath() + "_small");
		Identifier smallBroken = makeModel(provider, name("small_broken"), texturePath() + "_small_broken");
		Identifier inHand = makeModel(provider, name("in_hand"), texturePath(), inHandTransforms());
		Identifier inHandBroken = makeModel(provider, name("in_hand_broken"), texturePath() + "_broken", inHandTransforms());
		provider.itemModelOutput.accept(
			Objects.requireNonNull(itemForName()).value(),
			ItemModelUtils.select(
				new DisplayContext(),
				ItemModelUtils.conditional(new Broken(), ItemModelUtils.plainModel(smallBroken), ItemModelUtils.plainModel(small)),
				ItemModelUtils.when(
					List.of(
						ItemDisplayContext.FIRST_PERSON_LEFT_HAND,
						ItemDisplayContext.FIRST_PERSON_RIGHT_HAND,
						ItemDisplayContext.THIRD_PERSON_LEFT_HAND,
						ItemDisplayContext.THIRD_PERSON_RIGHT_HAND
					),
					ItemModelUtils.conditional(new Broken(), ItemModelUtils.plainModel(inHandBroken), ItemModelUtils.plainModel(inHand))
				)
			)
		);
		return small;
	}

	private UnaryOperator<ExtendedModelTemplateBuilder> inHandTransforms() {
		return builder -> builder
			.transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, t -> t
				.rotation(0, -90, 55)
				.translation(0, 7, 0.5F)
				.scale(1.5F, 1.5F, 1F))
			.transform(ItemDisplayContext.THIRD_PERSON_LEFT_HAND, t -> t
				.rotation(0, 90, -55)
				.translation(0, 7, 0.5F)
				.scale(1.5F, 1.5F, 1F))
			.transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND, t -> t
				.rotation(0, -90, 25)
				.translation(1.13F, 3.2F, 1.13F)
				.scale(1.25F, 1.25F, 1F))
			.transform(ItemDisplayContext.FIRST_PERSON_LEFT_HAND, t -> t
				.rotation(0, 90, -25)
				.translation(1.13F, 3.2F, 1.13F)
				.scale(1.25F, 1.25F, 1F))
			.transform(ItemDisplayContext.GROUND, t -> t
				.rotation(0, 3, 0)
				.scale(0.8F, 0.8F, 0.6F));
	}

	@Override
	public Optional<String> lang() {
		return Optional.of("Charred War Hammer");
	}
}
