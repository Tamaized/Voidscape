package tamaized.voidscape.datagen.assets.bakedmodel.block;

import net.minecraft.client.renderer.RenderType;
import net.neoforged.neoforge.client.model.generators.BlockModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.assets.bakedmodel.BlockModelHolder;
import tamaized.voidscape.datagen.assets.bakedmodel.block.overlay.FullFullbrightOverlayOverlayBlockModelHolder;
import tamaized.voidscape.registry.ModBlockComponentDirectory;

@Component
public class CharredBrickBlockModelHolder extends BlockModelHolder {

	@Autowired
	private ModBlockComponentDirectory blocks;

	@Autowired
	private FullFullbrightOverlayOverlayBlockModelHolder fullFullbrightOverlayOverlayBlockModelHolder;

	public ModelFile build(BlockModelProvider provider) {
		return provider.withExistingParent(
				name(blocks.materialBlocks().CHARRED_BRICK),
				fullFullbrightOverlayOverlayBlockModelHolder.getOrBuild(provider).getLocation()
			)
			.renderType(RenderType.cutoutMipped().name)
			.texture("base", "block/charred_brick")
			.texture("overlay", "block/charred_brick_overlay")
			.texture("particle", "#base");
	}

}
