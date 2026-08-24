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

import java.util.Optional;

@Component
public class CubeAllFullbrightBlockModelHolder extends BlockModelHolder {

	@Autowired
	private CubeFullbrightBlockModelHolder parent;

	@Override
	public Optional<ModelHolder<BlockModelGenerators>> parent() {
		return Optional.of(parent);
	}

	@Override
	public Identifier finalize(BlockModelGenerators provider, FurtherExtendedModelTemplateBuilder model) {
		return model
			.buildExtended()
			.create(Identifier.fromNamespaceAndPath(Voidscape.MODID, "block/fullbright/cube_all"), textures(), provider.modelOutput);
	}

	@Override
	protected void defineTextureSlots(ExtendedTextureMapping mapping) {
		mapping
			.putRef(TextureSlot.PARTICLE, TextureSlot.ALL)
			.putRef(TextureSlot.DOWN, TextureSlot.ALL)
			.putRef(TextureSlot.UP, TextureSlot.ALL)
			.putRef(TextureSlot.NORTH, TextureSlot.ALL)
			.putRef(TextureSlot.EAST, TextureSlot.ALL)
			.putRef(TextureSlot.SOUTH, TextureSlot.ALL)
			.putRef(TextureSlot.WEST, TextureSlot.ALL);
	}
}
