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
public class FullOverlayBlockModelHolder extends BlockModelHolder {

	@Autowired
	private BaseOverlayBlockModelHolder parent;

	@Override
	public Optional<ModelHolder<BlockModelGenerators>> parent() {
		return Optional.of(parent);
	}

	@Override
	public Identifier finalize(BlockModelGenerators provider, FurtherExtendedModelTemplateBuilder model) {
		return model
			.buildExtended()
			.create(Identifier.fromNamespaceAndPath(Voidscape.MODID, "block/overlay/full"), textures(), provider.modelOutput);
	}

	@Override
	protected void defineTextureSlots(ExtendedTextureMapping mapping) {
		mapping
			.putRef(TextureSlot.PARTICLE, ModTextureSlots.BASE)
			.putRef(TextureSlot.DOWN, ModTextureSlots.BASE)
			.putRef(TextureSlot.UP, ModTextureSlots.BASE)
			.putRef(TextureSlot.NORTH, ModTextureSlots.BASE)
			.putRef(TextureSlot.EAST, ModTextureSlots.BASE)
			.putRef(TextureSlot.SOUTH, ModTextureSlots.BASE)
			.putRef(TextureSlot.WEST, ModTextureSlots.BASE)
			.putRef(ModTextureSlots.OVERLAY_DOWN, ModTextureSlots.OVERLAY)
			.putRef(ModTextureSlots.OVERLAY_UP, ModTextureSlots.OVERLAY)
			.putRef(ModTextureSlots.OVERLAY_NORTH, ModTextureSlots.OVERLAY)
			.putRef(ModTextureSlots.OVERLAY_EAST, ModTextureSlots.OVERLAY)
			.putRef(ModTextureSlots.OVERLAY_SOUTH, ModTextureSlots.OVERLAY)
			.putRef(ModTextureSlots.OVERLAY_WEST, ModTextureSlots.OVERLAY);
	}
}
