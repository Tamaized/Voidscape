package tamaized.voidscape.datagen.assets.bakedmodel.block.fullbright;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.resources.Identifier;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.datagenutil.assets.bakedmodel.ExtendedTextureMapping;
import tamaized.datagenutil.assets.bakedmodel.FurtherExtendedModelTemplateBuilder;
import tamaized.datagenutil.assets.bakedmodel.ModelHolder;
import tamaized.datagenutil.assets.bakedmodel.block.BlockModelHolder;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.datagen.util.ModTextureSlots;

import java.util.Optional;

@Component
public class CubeColumnOverlayFullbrightBlockModelHolder extends BlockModelHolder {

	@Autowired
	private CubeOverlayFullbrightBlockModelHolder parent;

	@Override
	public Optional<ModelHolder<BlockModelGenerators>> parent() {
		return Optional.of(parent);
	}

	@Override
	public Identifier finalize(BlockModelGenerators provider, FurtherExtendedModelTemplateBuilder model) {
		return model
			.buildExtended()
			.create(Identifier.fromNamespaceAndPath(Voidscape.MODID, "block/fullbright/cube_column_overlay"), textures(), provider.modelOutput);
	}

	@Override
	protected void defineTextureSlots(ExtendedTextureMapping mapping) {
		mapping
			.putRef(TextureSlot.PARTICLE, TextureSlot.SIDE)
			.putRef(TextureSlot.DOWN, TextureSlot.END)
			.putRef(TextureSlot.UP, TextureSlot.END)
			.putRef(TextureSlot.NORTH, TextureSlot.SIDE)
			.putRef(TextureSlot.EAST, TextureSlot.SIDE)
			.putRef(TextureSlot.SOUTH, TextureSlot.SIDE)
			.putRef(TextureSlot.WEST, TextureSlot.SIDE)
			.putRef(ModTextureSlots.DOWN_OVERLAY, ModTextureSlots.END_OVERLAY)
			.putRef(ModTextureSlots.UP_OVERLAY, ModTextureSlots.END_OVERLAY)
			.putRef(ModTextureSlots.NORTH_OVERLAY, ModTextureSlots.SIDE_OVERLAY)
			.putRef(ModTextureSlots.EAST_OVERLAY, ModTextureSlots.SIDE_OVERLAY)
			.putRef(ModTextureSlots.SOUTH_OVERLAY, ModTextureSlots.SIDE_OVERLAY)
			.putRef(ModTextureSlots.WEST_OVERLAY, ModTextureSlots.SIDE_OVERLAY);
	}
}
