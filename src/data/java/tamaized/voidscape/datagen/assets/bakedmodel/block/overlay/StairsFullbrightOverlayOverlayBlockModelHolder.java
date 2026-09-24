package tamaized.voidscape.datagen.assets.bakedmodel.block.overlay;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemDisplayContext;
import tamaized.beanification.Component;
import tamaized.datagenutil.assets.bakedmodel.ExtendedTextureMapping;
import tamaized.datagenutil.assets.bakedmodel.FurtherExtendedModelTemplateBuilder;
import tamaized.datagenutil.assets.bakedmodel.block.BlockModelHolder;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.datagen.util.ModTextureSlots;

@Component
public class StairsFullbrightOverlayOverlayBlockModelHolder extends BlockModelHolder {

	@Override
	public Identifier finalize(BlockModelGenerators provider, FurtherExtendedModelTemplateBuilder model) {
		return model
			.buildExtended(m -> m
				.parent(Identifier.withDefaultNamespace("block/block"))
				.transform(ItemDisplayContext.GUI, t -> t
					.rotation(30, 135, 0)
					.translation(0, 0, 0)
					.scale(0.625F))
				.transform(ItemDisplayContext.HEAD, t -> t
					.rotation(0, -90, 0)
					.translation(0, 0, 0)
					.scale(1))
				.transform(ItemDisplayContext.THIRD_PERSON_LEFT_HAND, t -> t
					.rotation(75, -135, 0)
					.translation(0, 2.5F, 0)
					.scale(0.375F))
				.element(e -> e
					.from(0, 0, 0).to(16, 8, 16)
					.face(Direction.DOWN, f -> f.uvs(0, 0, 16, 16).texture(TextureSlot.BOTTOM).cullface(Direction.DOWN))
					.face(Direction.UP, f -> f.uvs(0, 0, 16, 16).texture(TextureSlot.TOP))
					.face(Direction.NORTH, f -> f.uvs(0, 8, 16, 16).texture(TextureSlot.SIDE).cullface(Direction.NORTH))
					.face(Direction.SOUTH, f -> f.uvs(0, 8, 16, 16).texture(TextureSlot.SIDE).cullface(Direction.SOUTH))
					.face(Direction.WEST, f -> f.uvs(0, 8, 16, 16).texture(TextureSlot.SIDE).cullface(Direction.WEST))
					.face(Direction.EAST, f -> f.uvs(0, 8, 16, 16).texture(TextureSlot.SIDE).cullface(Direction.EAST)))
				.element(e -> e
					.from(8, 8, 0).to(16, 16, 16)
					.face(Direction.UP, f -> f.uvs(8, 0, 16, 16).texture(TextureSlot.TOP).cullface(Direction.UP))
					.face(Direction.NORTH, f -> f.uvs(0, 0, 8, 8).texture(TextureSlot.SIDE).cullface(Direction.NORTH))
					.face(Direction.SOUTH, f -> f.uvs(8, 0, 16, 8).texture(TextureSlot.SIDE).cullface(Direction.SOUTH))
					.face(Direction.WEST, f -> f.uvs(0, 0, 16, 8).texture(TextureSlot.SIDE))
					.face(Direction.EAST, f -> f.uvs(0, 0, 16, 8).texture(TextureSlot.SIDE).cullface(Direction.EAST)))
				.element(e -> e
					.from(0, 0, 0).to(16, 8, 16)
					.face(Direction.DOWN, f -> f.uvs(0, 0, 16, 16).texture(ModTextureSlots.OVERLAY_BOTTOM).lightEmission(15).cullface(Direction.DOWN))
					.face(Direction.UP, f -> f.uvs(0, 0, 16, 16).texture(ModTextureSlots.OVERLAY_TOP).lightEmission(15))
					.face(Direction.NORTH, f -> f.uvs(0, 8, 16, 16).texture(ModTextureSlots.OVERLAY_SIDE).lightEmission(15).cullface(Direction.NORTH))
					.face(Direction.SOUTH, f -> f.uvs(0, 8, 16, 16).texture(ModTextureSlots.OVERLAY_SIDE).lightEmission(15).cullface(Direction.SOUTH))
					.face(Direction.WEST, f -> f.uvs(0, 8, 16, 16).texture(ModTextureSlots.OVERLAY_SIDE).lightEmission(15).cullface(Direction.WEST))
					.face(Direction.EAST, f -> f.uvs(0, 8, 16, 16).texture(ModTextureSlots.OVERLAY_SIDE).lightEmission(15).cullface(Direction.EAST)))
				.element(e -> e
					.from(8, 8, 0).to(16, 16, 16)
					.face(Direction.UP, f -> f.uvs(8, 0, 16, 16).texture(ModTextureSlots.OVERLAY_TOP).lightEmission(15).cullface(Direction.UP))
					.face(Direction.NORTH, f -> f.uvs(0, 0, 8, 8).texture(ModTextureSlots.OVERLAY_SIDE).lightEmission(15).cullface(Direction.NORTH))
					.face(Direction.SOUTH, f -> f.uvs(8, 0, 16, 8).texture(ModTextureSlots.OVERLAY_SIDE).lightEmission(15).cullface(Direction.SOUTH))
					.face(Direction.WEST, f -> f.uvs(0, 0, 16, 8).texture(ModTextureSlots.OVERLAY_SIDE).lightEmission(15))
					.face(Direction.EAST, f -> f.uvs(0, 0, 16, 8).texture(ModTextureSlots.OVERLAY_SIDE).lightEmission(15).cullface(Direction.EAST))))
			.create(Identifier.fromNamespaceAndPath(Voidscape.MODID, "block/overlay/stairs_fullbright_overlay"), textures(), provider.modelOutput);
	}

	@Override
	protected void defineTextureSlots(ExtendedTextureMapping mapping) {
		mapping.putRef(TextureSlot.PARTICLE, TextureSlot.SIDE);
	}
}
