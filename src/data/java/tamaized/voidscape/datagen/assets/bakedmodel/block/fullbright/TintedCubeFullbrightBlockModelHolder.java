package tamaized.voidscape.datagen.assets.bakedmodel.block.fullbright;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import tamaized.beanification.Component;
import tamaized.datagenutil.assets.bakedmodel.ExtendedTextureMapping;
import tamaized.datagenutil.assets.bakedmodel.FurtherExtendedModelTemplateBuilder;
import tamaized.datagenutil.assets.bakedmodel.block.BlockModelHolder;
import tamaized.voidscape.Voidscape;

@Component
public class TintedCubeFullbrightBlockModelHolder extends BlockModelHolder {

	@Override
	public Identifier finalize(BlockModelGenerators provider, FurtherExtendedModelTemplateBuilder model) {
		return model
			.buildExtended(m -> m
				.parent(Identifier.withDefaultNamespace("block/block"))
				.ambientOcclusion(false)
				.element(e -> e
					.from(0, 0, 0).to(16, 16, 16)
					.shade(false)
					.face(Direction.DOWN, f -> f.texture(TextureSlot.DOWN).cullface(Direction.DOWN).lightEmission(15).tintindex(0))
					.face(Direction.UP, f -> f.texture(TextureSlot.UP).cullface(Direction.UP).lightEmission(15).tintindex(0))
					.face(Direction.NORTH, f -> f.texture(TextureSlot.NORTH).cullface(Direction.NORTH).lightEmission(15).tintindex(0))
					.face(Direction.SOUTH, f -> f.texture(TextureSlot.SOUTH).cullface(Direction.SOUTH).lightEmission(15).tintindex(0))
					.face(Direction.WEST, f -> f.texture(TextureSlot.WEST).cullface(Direction.WEST).lightEmission(15).tintindex(0))
					.face(Direction.EAST, f -> f.texture(TextureSlot.EAST).cullface(Direction.EAST).lightEmission(15).tintindex(0))))
			.create(Identifier.fromNamespaceAndPath(Voidscape.MODID, "block/fullbright/tinted_cube"), textures(), provider.modelOutput);
	}

	@Override
	protected void defineTextureSlots(ExtendedTextureMapping mapping) {
	}
}
