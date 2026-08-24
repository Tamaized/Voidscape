package tamaized.voidscape.datagen.assets.bakedmodel.item;

import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.renderer.item.properties.conditional.Broken;
import net.minecraft.resources.Identifier;
import tamaized.datagenutil.assets.bakedmodel.FurtherExtendedModelTemplateBuilder;

import java.util.Objects;

public abstract class BreakableFullbrightItemModelHolder extends FullbrightItemModelHolder {

	@Override
	public Identifier finalize(ItemModelGenerators provider, FurtherExtendedModelTemplateBuilder model) {
		Identifier normal = makeModel(provider, name(), texturePath());
		Identifier broken = makeModel(provider, name("broken"), texturePath() + "_broken");
		provider.itemModelOutput.accept(
			Objects.requireNonNull(itemForName()).value(),
			ItemModelUtils.conditional(new Broken(), ItemModelUtils.plainModel(broken), ItemModelUtils.plainModel(normal))
		);
		return normal;
	}
}
