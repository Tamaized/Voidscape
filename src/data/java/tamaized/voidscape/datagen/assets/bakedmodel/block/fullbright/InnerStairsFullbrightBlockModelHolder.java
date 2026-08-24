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
public class InnerStairsFullbrightBlockModelHolder extends BlockModelHolder {

	@Override
	public Identifier finalize(BlockModelGenerators provider, FurtherExtendedModelTemplateBuilder model) {
		return model
			.buildExtended(m -> m
				.ambientOcclusion(false)
				.element(e -> e
					.from(0, 0, 0).to(16, 8, 16)
					.shade(false)
					.face(Direction.DOWN, f -> f.uvs(0, 0, 16, 16).texture(TextureSlot.BOTTOM).lightEmission(15).cullface(Direction.DOWN))
					.face(Direction.UP, f -> f.uvs(0, 0, 16, 16).texture(TextureSlot.TOP).lightEmission(15))
					.face(Direction.NORTH, f -> f.uvs(0, 8, 16, 16).texture(TextureSlot.SIDE).lightEmission(15).cullface(Direction.NORTH))
					.face(Direction.SOUTH, f -> f.uvs(0, 8, 16, 16).texture(TextureSlot.SIDE).lightEmission(15).cullface(Direction.SOUTH))
					.face(Direction.WEST, f -> f.uvs(0, 8, 16, 16).texture(TextureSlot.SIDE).lightEmission(15).cullface(Direction.WEST))
					.face(Direction.EAST, f -> f.uvs(0, 8, 16, 16).texture(TextureSlot.SIDE).lightEmission(15).cullface(Direction.EAST)))
				.element(e -> e
					.from(8, 8, 0).to(16, 16, 16)
					.shade(false)
					.face(Direction.UP, f -> f.uvs(8, 0, 16, 16).texture(TextureSlot.TOP).lightEmission(15).cullface(Direction.UP))
					.face(Direction.NORTH, f -> f.uvs(0, 0, 8, 8).texture(TextureSlot.SIDE).lightEmission(15).cullface(Direction.NORTH))
					.face(Direction.SOUTH, f -> f.uvs(8, 0, 16, 8).texture(TextureSlot.SIDE).lightEmission(15).cullface(Direction.SOUTH))
					.face(Direction.WEST, f -> f.uvs(0, 0, 16, 8).texture(TextureSlot.SIDE).lightEmission(15))
					.face(Direction.EAST, f -> f.uvs(0, 0, 16, 8).texture(TextureSlot.SIDE).lightEmission(15).cullface(Direction.EAST)))
				.element(e -> e
					.from(0, 8, 8).to(8, 16, 16)
					.shade(false)
					.face(Direction.UP, f -> f.uvs(0, 8, 8, 16).texture(TextureSlot.TOP).lightEmission(15).cullface(Direction.UP))
					.face(Direction.NORTH, f -> f.uvs(8, 0, 16, 8).texture(TextureSlot.SIDE).lightEmission(15))
					.face(Direction.SOUTH, f -> f.uvs(0, 0, 8, 8).texture(TextureSlot.SIDE).lightEmission(15).cullface(Direction.SOUTH))
					.face(Direction.WEST, f -> f.uvs(8, 0, 16, 8).texture(TextureSlot.SIDE).lightEmission(15).cullface(Direction.WEST))))
			.create(Identifier.fromNamespaceAndPath(Voidscape.MODID, "block/fullbright/inner_stairs"), textures(), provider.modelOutput);
	}

	@Override
	protected void defineTextureSlots(ExtendedTextureMapping mapping) {
		mapping.putRef(TextureSlot.PARTICLE, TextureSlot.SIDE);
	}
}
