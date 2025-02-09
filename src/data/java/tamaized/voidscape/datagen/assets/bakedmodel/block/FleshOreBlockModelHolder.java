package tamaized.voidscape.datagen.assets.bakedmodel.block;

import net.minecraft.client.renderer.RenderType;
import net.neoforged.neoforge.client.model.generators.BlockModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.assets.bakedmodel.BlockModelHolder;
import tamaized.voidscape.datagen.assets.bakedmodel.block.overlay.FullFullbrightOverlayOverlayBlockModelHolder;

@Component
public class FleshOreBlockModelHolder extends BlockModelHolder {

	@Autowired
	private FullFullbrightOverlayOverlayBlockModelHolder fullFullbrightOverlayOverlayBlockModelHolder;

	public ModelFile build(BlockModelProvider provider) {
		return provider.withExistingParent("block/flesh_ore", fullFullbrightOverlayOverlayBlockModelHolder.getOrBuild(provider).getLocation())
			.renderType(RenderType.cutoutMipped().name)
			.texture("base", provider.mcLoc("block/netherrack"))
			.texture("side", "block/flesh_ore")
			.texture("particle", "#base");
	}

}
