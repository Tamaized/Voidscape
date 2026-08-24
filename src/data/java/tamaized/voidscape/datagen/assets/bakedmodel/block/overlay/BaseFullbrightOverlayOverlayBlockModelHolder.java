package tamaized.voidscape.datagen.assets.bakedmodel.block.overlay;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import tamaized.beanification.Component;
import tamaized.datagenutil.assets.bakedmodel.ExtendedTextureMapping;
import tamaized.datagenutil.assets.bakedmodel.FurtherExtendedModelTemplateBuilder;
import tamaized.datagenutil.assets.bakedmodel.block.BlockModelHolder;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.datagen.util.ModTextureSlots;

@Component
public class BaseFullbrightOverlayOverlayBlockModelHolder extends BlockModelHolder {

	@Override
	public Identifier finalize(BlockModelGenerators provider, FurtherExtendedModelTemplateBuilder model) {
		return model
			.buildExtended(m -> m
				.parent(Identifier.withDefaultNamespace("block/block"))
				.element(e -> e
					.from(0, 0, 0).to(16, 16, 16)
					.face(Direction.DOWN, f -> f.texture(TextureSlot.DOWN).cullface(Direction.DOWN))
					.face(Direction.UP, f -> f.texture(TextureSlot.UP).cullface(Direction.UP))
					.face(Direction.NORTH, f -> f.texture(TextureSlot.NORTH).cullface(Direction.NORTH))
					.face(Direction.SOUTH, f -> f.texture(TextureSlot.SOUTH).cullface(Direction.SOUTH))
					.face(Direction.WEST, f -> f.texture(TextureSlot.WEST).cullface(Direction.WEST))
					.face(Direction.EAST, f -> f.texture(TextureSlot.EAST).cullface(Direction.EAST)))
				.element(e -> e
					.from(0, 0, 0).to(16, 16, 16)
					.face(Direction.DOWN, f -> f.texture(ModTextureSlots.OVERLAY_DOWN).cullface(Direction.DOWN).lightEmission(15))
					.face(Direction.UP, f -> f.texture(ModTextureSlots.OVERLAY_UP).cullface(Direction.UP).lightEmission(15))
					.face(Direction.NORTH, f -> f.texture(ModTextureSlots.OVERLAY_NORTH).cullface(Direction.NORTH).lightEmission(15))
					.face(Direction.SOUTH, f -> f.texture(ModTextureSlots.OVERLAY_SOUTH).cullface(Direction.SOUTH).lightEmission(15))
					.face(Direction.WEST, f -> f.texture(ModTextureSlots.OVERLAY_WEST).cullface(Direction.WEST).lightEmission(15))
					.face(Direction.EAST, f -> f.texture(ModTextureSlots.OVERLAY_EAST).cullface(Direction.EAST).lightEmission(15))))
			.create(Identifier.fromNamespaceAndPath(Voidscape.MODID, "block/overlay/base_fullbright_overlay"), textures(), provider.modelOutput);
	}

	@Override
	protected void defineTextureSlots(ExtendedTextureMapping mapping) {
	}
}
