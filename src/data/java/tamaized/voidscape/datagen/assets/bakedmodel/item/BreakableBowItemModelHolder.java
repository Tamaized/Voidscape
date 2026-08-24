package tamaized.voidscape.datagen.assets.bakedmodel.item;

import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.renderer.item.properties.conditional.Broken;
import net.minecraft.client.renderer.item.properties.numeric.UseDuration;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemDisplayContext;
import net.neoforged.neoforge.client.model.generators.template.ExtendedModelTemplateBuilder;
import tamaized.datagenutil.assets.bakedmodel.FurtherExtendedModelTemplateBuilder;

import java.util.Objects;
import java.util.function.UnaryOperator;

public abstract class BreakableBowItemModelHolder extends FullbrightItemModelHolder {

	@Override
	public Identifier finalize(ItemModelGenerators provider, FurtherExtendedModelTemplateBuilder model) {
		Identifier base = makeModel(provider, name(), texturePath(), heldTransforms());
		Identifier broken = makeVariant(provider, "broken");
		Identifier pulling0 = makeVariant(provider, "pulling_0");
		Identifier pulling1 = makeVariant(provider, "pulling_1");
		Identifier pulling2 = makeVariant(provider, "pulling_2");
		provider.itemModelOutput.accept(
			Objects.requireNonNull(itemForName()).value(),
			ItemModelUtils.conditional(
				ItemModelUtils.isUsingItem(),
				ItemModelUtils.rangeSelect(
					new UseDuration(false),
					0.05F,
					ItemModelUtils.plainModel(pulling0),
					ItemModelUtils.override(ItemModelUtils.plainModel(pulling1), 0.65F),
					ItemModelUtils.override(ItemModelUtils.plainModel(pulling2), 0.9F)
				),
				ItemModelUtils.conditional(new Broken(), ItemModelUtils.plainModel(broken), ItemModelUtils.plainModel(base))
			)
		);
		return base;
	}

	private Identifier makeVariant(ItemModelGenerators provider, String suffix) {
		return makeModel(provider, Identifier.withDefaultNamespace("item/bow"), name(suffix), texturePath() + "_" + suffix);
	}

	private UnaryOperator<ExtendedModelTemplateBuilder> heldTransforms() {
		return builder -> builder
			.transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, t -> t
				.rotation(-80, 260, -40)
				.translation(-1, -2, 2.5F)
				.scale(0.9F))
			.transform(ItemDisplayContext.THIRD_PERSON_LEFT_HAND, t -> t
				.rotation(-80, -280, 40)
				.translation(-1, -2, 2.5F)
				.scale(0.9F))
			.transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND, t -> t
				.rotation(0, -90, 25)
				.translation(1.13F, 3.2F, 1.13F)
				.scale(0.68F))
			.transform(ItemDisplayContext.FIRST_PERSON_LEFT_HAND, t -> t
				.rotation(0, 90, -25)
				.translation(1.13F, 3.2F, 1.13F)
				.scale(0.68F));
	}
}
