package tamaized.voidscape.datagen.assets.bakedmodel.block;

import net.minecraft.client.renderer.RenderType;
import net.neoforged.neoforge.client.model.generators.BlockModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.assets.bakedmodel.BlockModelHolder;
import tamaized.voidscape.datagen.assets.bakedmodel.block.overlay.FullFullbrightOverlayOverlayBlockModelHolder;

@Component
public class CharredBrickBlockModelHolder extends BlockModelHolder {

	@Autowired
	private FullFullbrightOverlayOverlayBlockModelHolder fullFullbrightOverlayOverlayBlockModelHolder;

	public ModelFile build(BlockModelProvider provider) {
		return provider.withExistingParent("block/charred_brick", fullFullbrightOverlayOverlayBlockModelHolder.getOrBuild(provider).getLocation())
			.renderType(RenderType.cutoutMipped().name)
			.texture("base", "block/charred_brick")
			.texture("overlay", "block/charred_brick_overlay")
			.texture("particle", "#base");
	}

}
