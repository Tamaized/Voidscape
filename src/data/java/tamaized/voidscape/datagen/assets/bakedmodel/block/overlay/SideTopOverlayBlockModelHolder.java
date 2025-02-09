package tamaized.voidscape.datagen.assets.bakedmodel.block.overlay;

import net.neoforged.neoforge.client.model.generators.BlockModelProvider;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.assets.bakedmodel.block.BlockModelHolder;

@Component
public class SideTopOverlayBlockModelHolder extends BlockModelHolder {

	@Autowired
	private BaseOverlayBlockModelHolder baseOverlayBlockModelHolder;

	public void build(BlockModelProvider provider) {
		set(
			// @formatter:off
			provider.withExistingParent("block/overlay/side_top", baseOverlayBlockModelHolder.getOrBuild(provider).getLocation())
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
				.texture("overlay-west", "#overlay-side")
			// @formatter:on
		);
	}

}
