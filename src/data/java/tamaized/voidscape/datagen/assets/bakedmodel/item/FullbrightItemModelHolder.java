package tamaized.voidscape.datagen.assets.bakedmodel.item;

import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.client.model.ExtraFaceData;
import net.neoforged.neoforge.client.model.generators.template.ExtendedModelTemplateBuilder;
import tamaized.datagenutil.assets.bakedmodel.ExtendedTextureMapping;
import tamaized.datagenutil.assets.bakedmodel.FurtherExtendedModelTemplateBuilder;
import tamaized.datagenutil.assets.bakedmodel.item.ItemModelHolder;
import tamaized.voidscape.Voidscape;

import java.util.Objects;
import java.util.function.UnaryOperator;

public abstract class FullbrightItemModelHolder extends ItemModelHolder {

	private final ExtraFaceData fullbright = new ExtraFaceData(ExtraFaceData.DEFAULT.color(), 15, ExtraFaceData.DEFAULT.ambientOcclusion());

	protected Identifier modelParent() {
		return Identifier.withDefaultNamespace("item/generated");
	}

	protected String texturePath() {
		return name();
	}

	protected final Identifier modLoc(String path) {
		return Identifier.fromNamespaceAndPath(Voidscape.MODID, path);
	}

	protected final Identifier makeModel(ItemModelGenerators provider, String path, String texture) {
		return makeModel(provider, modelParent(), path, texture, UnaryOperator.identity());
	}

	protected final Identifier makeModel(ItemModelGenerators provider, String path, String texture, UnaryOperator<ExtendedModelTemplateBuilder> config) {
		return makeModel(provider, modelParent(), path, texture, config);
	}

	protected final Identifier makeModel(ItemModelGenerators provider, Identifier parent, String path, String texture) {
		return makeModel(provider, parent, path, texture, UnaryOperator.identity());
	}

	protected final Identifier makeModel(ItemModelGenerators provider, Identifier parent, String path, String texture, UnaryOperator<ExtendedModelTemplateBuilder> config) {
		return config
			.apply(
				ExtendedModelTemplateBuilder.builder()
					.parent(parent)
					.requiredTextureSlot(TextureSlot.LAYER0)
					.itemLayerFaceData("layer0", fullbright)
			)
			.build()
			.create(modLoc(path), new TextureMapping().put(TextureSlot.LAYER0, new Material(modLoc(texture))), provider.modelOutput);
	}

	@Override
	public Identifier finalize(ItemModelGenerators provider, FurtherExtendedModelTemplateBuilder model) {
		Identifier id = makeModel(provider, name(), texturePath());
		provider.itemModelOutput.accept(Objects.requireNonNull(itemForName()).value(), ItemModelUtils.plainModel(id));
		return id;
	}

	@Override
	protected void defineTextureSlots(ExtendedTextureMapping mapping) {
	}
}
