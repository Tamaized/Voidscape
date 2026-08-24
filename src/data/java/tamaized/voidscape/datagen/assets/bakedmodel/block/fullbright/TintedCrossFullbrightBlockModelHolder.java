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
public class TintedCrossFullbrightBlockModelHolder extends BlockModelHolder {

	@Override
	public Identifier finalize(BlockModelGenerators provider, FurtherExtendedModelTemplateBuilder model) {
		return model
			.buildExtended(m -> m
				.ambientOcclusion(false)
				.element(e -> e
					.from(0.8F, 0, 8).to(15.2F, 16, 8)
					.rotation(r -> r
						.origin(8, 8, 8)
						.singleAxis(Direction.Axis.Y, 45)
						.rescale(true))
					.shade(false)
					.face(Direction.NORTH, f -> f.uvs(0, 0, 16, 16).texture(TextureSlot.CROSS).lightEmission(15).tintindex(0))
					.face(Direction.SOUTH, f -> f.uvs(0, 0, 16, 16).texture(TextureSlot.CROSS).lightEmission(15).tintindex(0)))
				.element(e -> e
					.from(8, 0, 0.8F).to(8, 16, 15.2F)
					.rotation(r -> r
						.origin(8, 8, 8)
						.singleAxis(Direction.Axis.Y, 45)
						.rescale(true))
					.shade(false)
					.face(Direction.WEST, f -> f.uvs(0, 0, 16, 16).texture(TextureSlot.CROSS).lightEmission(15).tintindex(0))
					.face(Direction.EAST, f -> f.uvs(0, 0, 16, 16).texture(TextureSlot.CROSS).lightEmission(15).tintindex(0))))
			.create(Identifier.fromNamespaceAndPath(Voidscape.MODID, "block/fullbright/tinted_cross"), textures(), provider.modelOutput);
	}

	@Override
	protected void defineTextureSlots(ExtendedTextureMapping mapping) {
		mapping.putRef(TextureSlot.PARTICLE, TextureSlot.CROSS);
	}
}
