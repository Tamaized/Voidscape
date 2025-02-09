package tamaized.voidscape.datagen.assets.bakedmodel.block.overlay;

import net.neoforged.neoforge.client.model.generators.BlockModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.assets.bakedmodel.BlockModelHolder;

@Component
public class FullFullbrightOverlayOverlayBlockModelHolder extends BlockModelHolder {

	@Autowired
	private BaseFullbrightOverlayOverlayBlockModelHolder parent;

	public ModelFile build(BlockModelProvider provider) {
		// @formatter:off
		return provider.withExistingParent("block/overlay/full_fullbright_overlay", parent.getOrBuild(provider).getLocation())
			.texture("particle", "#base")
			.texture("down", "#base")
			.texture("up", "#base")
			.texture("north", "#base")
			.texture("east", "#base")
			.texture("south", "#base")
			.texture("west", "#base")
			.texture("overlay-down", "#overlay")
			.texture("overlay-up", "#overlay")
			.texture("overlay-north", "#overlay")
			.texture("overlay-east", "#overlay")
			.texture("overlay-south", "#overlay")
			.texture("overlay-west", "#overlay");
		// @formatter:on
	}

}
