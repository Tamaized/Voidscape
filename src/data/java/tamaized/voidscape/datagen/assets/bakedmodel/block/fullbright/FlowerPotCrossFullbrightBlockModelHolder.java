package tamaized.voidscape.datagen.assets.bakedmodel.block.fullbright;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import tamaized.beanification.Component;
import tamaized.datagenutil.assets.bakedmodel.ExtendedTextureMapping;
import tamaized.datagenutil.assets.bakedmodel.FurtherExtendedModelTemplateBuilder;
import tamaized.datagenutil.assets.bakedmodel.block.BlockModelHolder;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.datagen.util.ModTextureSlots;

@Component
public class FlowerPotCrossFullbrightBlockModelHolder extends BlockModelHolder {

	@Override
	public Identifier finalize(BlockModelGenerators provider, FurtherExtendedModelTemplateBuilder model) {
		return model
			.buildExtended(m -> m
				.ambientOcclusion(false)
				.element(e -> e
					.from(5, 0, 5).to(6, 6, 11)
					.shade(false)
					.face(Direction.DOWN, f -> f.uvs(5, 5, 6, 11).texture(ModTextureSlots.FLOWERPOT).cullface(Direction.DOWN))
					.face(Direction.UP, f -> f.uvs(5, 5, 6, 11).texture(ModTextureSlots.FLOWERPOT))
					.face(Direction.NORTH, f -> f.uvs(10, 10, 11, 16).texture(ModTextureSlots.FLOWERPOT))
					.face(Direction.SOUTH, f -> f.uvs(5, 10, 6, 16).texture(ModTextureSlots.FLOWERPOT))
					.face(Direction.WEST, f -> f.uvs(5, 10, 11, 16).texture(ModTextureSlots.FLOWERPOT))
					.face(Direction.EAST, f -> f.uvs(5, 10, 11, 16).texture(ModTextureSlots.FLOWERPOT)))
				.element(e -> e
					.from(10, 0, 5).to(11, 6, 11)
					.shade(false)
					.face(Direction.DOWN, f -> f.uvs(10, 5, 11, 11).texture(ModTextureSlots.FLOWERPOT).cullface(Direction.DOWN))
					.face(Direction.UP, f -> f.uvs(10, 5, 11, 11).texture(ModTextureSlots.FLOWERPOT))
					.face(Direction.NORTH, f -> f.uvs(5, 10, 6, 16).texture(ModTextureSlots.FLOWERPOT))
					.face(Direction.SOUTH, f -> f.uvs(10, 10, 11, 16).texture(ModTextureSlots.FLOWERPOT))
					.face(Direction.WEST, f -> f.uvs(5, 10, 11, 16).texture(ModTextureSlots.FLOWERPOT))
					.face(Direction.EAST, f -> f.uvs(5, 10, 11, 16).texture(ModTextureSlots.FLOWERPOT)))
				.element(e -> e
					.from(6, 0, 5).to(10, 6, 6)
					.shade(false)
					.face(Direction.DOWN, f -> f.uvs(6, 10, 10, 11).texture(ModTextureSlots.FLOWERPOT).cullface(Direction.DOWN))
					.face(Direction.UP, f -> f.uvs(6, 5, 10, 6).texture(ModTextureSlots.FLOWERPOT))
					.face(Direction.NORTH, f -> f.uvs(6, 10, 10, 16).texture(ModTextureSlots.FLOWERPOT))
					.face(Direction.SOUTH, f -> f.uvs(6, 10, 10, 16).texture(ModTextureSlots.FLOWERPOT)))
				.element(e -> e
					.from(6, 0, 10).to(10, 6, 11)
					.shade(false)
					.face(Direction.DOWN, f -> f.uvs(6, 5, 10, 6).texture(ModTextureSlots.FLOWERPOT).cullface(Direction.DOWN))
					.face(Direction.UP, f -> f.uvs(6, 10, 10, 11).texture(ModTextureSlots.FLOWERPOT))
					.face(Direction.NORTH, f -> f.uvs(6, 10, 10, 16).texture(ModTextureSlots.FLOWERPOT))
					.face(Direction.SOUTH, f -> f.uvs(6, 10, 10, 16).texture(ModTextureSlots.FLOWERPOT)))
				.element(e -> e
					.from(6, 0, 6).to(10, 4, 10)
					.shade(false)
					.face(Direction.DOWN, f -> f.uvs(6, 12, 10, 16).texture(ModTextureSlots.FLOWERPOT).cullface(Direction.DOWN))
					.face(Direction.UP, f -> f.uvs(6, 6, 10, 10).texture(TextureSlot.DIRT)))
				.element(e -> e
					.from(2.6F, 4, 8).to(13.4F, 16, 8)
					.shade(false)
					.rotation(r -> r
						.origin(8, 8, 8)
						.singleAxis(Direction.Axis.Y, 45F)
						.rescale(true))
					.face(Direction.NORTH, f -> f.uvs(0, 0, 16, 16).texture(TextureSlot.PLANT).lightEmission(15))
					.face(Direction.SOUTH, f -> f.uvs(0, 0, 16, 16).texture(TextureSlot.PLANT).lightEmission(15)))
				.element(e -> e
					.from(8, 4, 2.6F).to(8, 16, 13.4F)
					.shade(false)
					.rotation(r -> r
						.origin(8, 8, 8)
						.singleAxis(Direction.Axis.Y, 45F)
						.rescale(true))
					.face(Direction.WEST, f -> f.uvs(0, 0, 16, 16).texture(TextureSlot.PLANT).lightEmission(15))
					.face(Direction.EAST, f -> f.uvs(0, 0, 16, 16).texture(TextureSlot.PLANT).lightEmission(15))))
			.create(Identifier.fromNamespaceAndPath(Voidscape.MODID, "block/fullbright/flower_pot_cross"), textures(), provider.modelOutput);
	}

	@Override
	protected void defineTextureSlots(ExtendedTextureMapping mapping) {
		mapping
			.putForced(TextureSlot.PARTICLE, new Material(Identifier.withDefaultNamespace("block/flower_pot")))
			.putForced(ModTextureSlots.FLOWERPOT, new Material(Identifier.withDefaultNamespace("block/flower_pot")))
			.putForced(TextureSlot.DIRT, new Material(Identifier.withDefaultNamespace("block/dirt")));
	}
}
