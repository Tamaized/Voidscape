package tamaized.voidscape.datagen.assets.bakedmodel.block;

import net.minecraft.client.renderer.RenderType;
import net.neoforged.neoforge.client.model.generators.BlockModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.assets.bakedmodel.BlockModelHolder;
import tamaized.voidscape.datagen.assets.bakedmodel.block.overlay.FullFullbrightOverlayOverlayBlockModelHolder;

@Component
public class CoopBlockModelHolder extends BlockModelHolder {

	public ModelFile build(BlockModelProvider provider) {
		return provider.withExistingParent("block/coop", provider.modLoc("block/germinator"))
			.renderType(RenderType.cutoutMipped().name)
			.texture("0", "block/machine/coop/frame")
			.texture("1", "block/machine/core")
			.texture("2", "block/machine/inner")
			.texture("particle", "#0");
	}

}
