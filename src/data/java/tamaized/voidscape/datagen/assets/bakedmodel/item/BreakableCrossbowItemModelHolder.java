package tamaized.voidscape.datagen.assets.bakedmodel.item;

import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.renderer.item.properties.conditional.Broken;
import net.minecraft.client.renderer.item.properties.numeric.CrossbowPull;
import net.minecraft.client.renderer.item.properties.select.Charge;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.neoforged.neoforge.client.model.generators.template.ExtendedModelTemplateBuilder;
import tamaized.datagenutil.assets.bakedmodel.FurtherExtendedModelTemplateBuilder;

import java.util.Objects;
import java.util.function.UnaryOperator;

public abstract class BreakableCrossbowItemModelHolder extends FullbrightItemModelHolder {

	@Override
	public Identifier finalize(ItemModelGenerators provider, FurtherExtendedModelTemplateBuilder model) {
		Identifier standby = makeModel(provider, name(), texturePath() + "_standby", heldTransforms());
		Identifier broken = makeVariant(provider, "broken");
		Identifier pulling0 = makeVariant(provider, "pulling_0");
		Identifier pulling1 = makeVariant(provider, "pulling_1");
		Identifier pulling2 = makeVariant(provider, "pulling_2");
		Identifier arrow = makeVariant(provider, "arrow");
		Identifier firework = makeVariant(provider, "firework");
		provider.itemModelOutput.accept(
			Objects.requireNonNull(itemForName()).value(),
			ItemModelUtils.select(
				new Charge(),
				ItemModelUtils.conditional(
					ItemModelUtils.isUsingItem(),
					ItemModelUtils.rangeSelect(
						new CrossbowPull(),
						ItemModelUtils.plainModel(pulling0),
						ItemModelUtils.override(ItemModelUtils.plainModel(pulling1), 0.58F),
						ItemModelUtils.override(ItemModelUtils.plainModel(pulling2), 1.0F)
					),
					ItemModelUtils.conditional(new Broken(), ItemModelUtils.plainModel(broken), ItemModelUtils.plainModel(standby))
				),
				ItemModelUtils.when(CrossbowItem.ChargeType.ARROW, ItemModelUtils.plainModel(arrow)),
				ItemModelUtils.when(CrossbowItem.ChargeType.ROCKET, ItemModelUtils.plainModel(firework))
			)
		);
		return standby;
	}

	private Identifier makeVariant(ItemModelGenerators provider, String suffix) {
		return makeModel(provider, Identifier.withDefaultNamespace("item/crossbow"), name(suffix), texturePath() + "_" + suffix);
	}

	private UnaryOperator<ExtendedModelTemplateBuilder> heldTransforms() {
		return builder -> builder
			.transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, t -> t
				.rotation(-90, 0, -60)
				.translation(2, 0.1F, -3)
				.scale(0.9F))
			.transform(ItemDisplayContext.THIRD_PERSON_LEFT_HAND, t -> t
				.rotation(-90, 0, 30)
				.translation(2, 0.1F, -3)
				.scale(0.9F))
			.transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND, t -> t
				.rotation(-90, 0, -55)
				.translation(1.13F, 3.2F, 1.13F)
				.scale(0.68F))
			.transform(ItemDisplayContext.FIRST_PERSON_LEFT_HAND, t -> t
				.rotation(-90, 0, 35)
				.translation(1.13F, 3.2F, 1.13F)
				.scale(0.68F));
	}
}
