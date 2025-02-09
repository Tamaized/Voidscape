package tamaized.voidscape.datagen.assets.bakedmodel.block;

import net.minecraft.client.renderer.RenderType;
import net.neoforged.neoforge.client.model.generators.BlockModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.assets.bakedmodel.BlockModelHolder;

@Component
public class HatcheryBlockModelHolder extends BlockModelHolder {

	public ModelFile build(BlockModelProvider provider) {
		return provider.withExistingParent("block/hatchery", provider.modLoc("block/germinator"))
			.renderType(RenderType.cutoutMipped().name)
			.texture("0", "block/machine/hatchery/frame")
			.texture("1", "block/machine/core")
			.texture("2", "block/machine/inner")
			.texture("particle", "#0");
	}

}
