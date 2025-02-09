package tamaized.voidscape.datagen.assets.bakedmodel.block.overlay;

import net.neoforged.neoforge.client.model.generators.BlockModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.assets.bakedmodel.BlockModelHolder;

@Component
public class SideTopFullbrightOverlayOverlayBlockModelHolder extends BlockModelHolder {

	@Autowired
	private BaseFullbrightOverlayOverlayBlockModelHolder parent;

	public ModelFile build(BlockModelProvider provider) {
		// @formatter:off
		return provider.withExistingParent("block/overlay/side_top_fullbright_overlay", parent.getOrBuild(provider).getLocation())
			.texture("particle", "#top")
			.texture("down", "#bottom")
			.texture("up", "#top")
			.texture("north", "#side")
			.texture("east", "#side")
			.texture("south", "#side")
			.texture("west", "#side")
			.texture("overlay-down", "#overlay-bottom")
			.texture("overlay-up", "#overlay-top")
			.texture("overlay-north", "#overlay-side")
			.texture("overlay-east", "#overlay-side")
			.texture("overlay-south", "#overlay-side")
			.texture("overlay-west", "#overlay-side");
		// @formatter:on
	}

}
