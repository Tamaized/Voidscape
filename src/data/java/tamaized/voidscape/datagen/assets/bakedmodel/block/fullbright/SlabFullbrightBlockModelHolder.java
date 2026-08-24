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
public class SlabFullbrightBlockModelHolder extends BlockModelHolder {

	@Override
	public Identifier finalize(BlockModelGenerators provider, FurtherExtendedModelTemplateBuilder model) {
		return model
			.buildExtended(m -> m
				.parent(Identifier.withDefaultNamespace("block/block"))
				.ambientOcclusion(false)
				.element(e -> e
					.from(0, 0, 0).to(16, 8, 16)
					.shade(false)
					.face(Direction.DOWN, f -> f.uvs(0, 0, 16, 16).texture(TextureSlot.BOTTOM).lightEmission(15).cullface(Direction.DOWN))
					.face(Direction.UP, f -> f.uvs(0, 0, 16, 16).texture(TextureSlot.TOP).lightEmission(15))
					.face(Direction.NORTH, f -> f.uvs(0, 8, 16, 16).texture(TextureSlot.SIDE).lightEmission(15).cullface(Direction.NORTH))
					.face(Direction.SOUTH, f -> f.uvs(0, 8, 16, 16).texture(TextureSlot.SIDE).lightEmission(15).cullface(Direction.SOUTH))
					.face(Direction.WEST, f -> f.uvs(0, 8, 16, 16).texture(TextureSlot.SIDE).lightEmission(15).cullface(Direction.WEST))
					.face(Direction.EAST, f -> f.uvs(0, 8, 16, 16).texture(TextureSlot.SIDE).lightEmission(15).cullface(Direction.EAST))))
			.create(Identifier.fromNamespaceAndPath(Voidscape.MODID, "block/fullbright/slab"), textures(), provider.modelOutput);
	}

	@Override
	protected void defineTextureSlots(ExtendedTextureMapping mapping) {
		mapping.putRef(TextureSlot.PARTICLE, TextureSlot.SIDE);
	}
}
