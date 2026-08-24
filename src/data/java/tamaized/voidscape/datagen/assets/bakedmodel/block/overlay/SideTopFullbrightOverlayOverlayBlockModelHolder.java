package tamaized.voidscape.datagen.assets.bakedmodel.block.overlay;

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
public class SideTopFullbrightOverlayOverlayBlockModelHolder extends BlockModelHolder {

	@Autowired
	private BaseFullbrightOverlayOverlayBlockModelHolder parent;

	@Override
	public Optional<ModelHolder<BlockModelGenerators>> parent() {
		return Optional.of(parent);
	}

	@Override
	public Identifier finalize(BlockModelGenerators provider, FurtherExtendedModelTemplateBuilder model) {
		return model
			.buildExtended()
			.create(Identifier.fromNamespaceAndPath(Voidscape.MODID, "block/overlay/side_top_fullbright_overlay"), textures(), provider.modelOutput);
	}

	@Override
	protected void defineTextureSlots(ExtendedTextureMapping mapping) {
		mapping
			.putRef(TextureSlot.PARTICLE, TextureSlot.TOP)
			.putRef(TextureSlot.DOWN, TextureSlot.BOTTOM)
			.putRef(TextureSlot.UP, TextureSlot.TOP)
			.putRef(TextureSlot.NORTH, TextureSlot.SIDE)
			.putRef(TextureSlot.EAST, TextureSlot.SIDE)
			.putRef(TextureSlot.SOUTH, TextureSlot.SIDE)
			.putRef(TextureSlot.WEST, TextureSlot.SIDE)
			.putRef(ModTextureSlots.OVERLAY_DOWN, ModTextureSlots.OVERLAY_BOTTOM)
			.putRef(ModTextureSlots.OVERLAY_UP, ModTextureSlots.OVERLAY_TOP)
			.putRef(ModTextureSlots.OVERLAY_NORTH, ModTextureSlots.OVERLAY_SIDE)
			.putRef(ModTextureSlots.OVERLAY_EAST, ModTextureSlots.OVERLAY_SIDE)
			.putRef(ModTextureSlots.OVERLAY_SOUTH, ModTextureSlots.OVERLAY_SIDE)
			.putRef(ModTextureSlots.OVERLAY_WEST, ModTextureSlots.OVERLAY_SIDE);
	}
}
