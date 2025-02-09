package tamaized.voidscape.datagen.assets.bakedmodel.block.overlay;

import net.neoforged.neoforge.client.model.generators.BlockModelProvider;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.assets.bakedmodel.block.BlockModelHolder;

@Component
public class SideOverlayBlockModelHolder extends BlockModelHolder {

	@Autowired
	private BaseOverlayBlockModelHolder baseOverlayBlockModelHolder;

	public void build(BlockModelProvider provider) {
		set(
			// @formatter:off
			provider.withExistingParent("block/overlay/side", baseOverlayBlockModelHolder.getOrBuild(provider).getLocation())
				.texture("particle", "#side")
				.texture("down", "#end")
				.texture("up", "#end")
				.texture("north", "#side")
				.texture("east", "#side")
				.texture("south", "#side")
				.texture("west", "#side")
				.texture("overlay-down", "#overlay-end")
				.texture("overlay-up", "#overlay-end")
				.texture("overlay-north", "#overlay-side")
				.texture("overlay-east", "#overlay-side")
				.texture("overlay-south", "#overlay-side")
				.texture("overlay-west", "#overlay-side")
			// @formatter:on
		);
	}

}
