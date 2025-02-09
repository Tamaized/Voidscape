package tamaized.voidscape.datagen.assets.bakedmodel.block.overlay;

import net.neoforged.neoforge.client.model.generators.BlockModelProvider;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.assets.bakedmodel.block.BlockModelHolder;

@Component
public class FullOverlayBlockModelHolder extends BlockModelHolder {

	@Autowired
	private BaseOverlayBlockModelHolder baseOverlayBlockModelHolder;

	public void build(BlockModelProvider provider) {
		set(
			// @formatter:off
			provider.withExistingParent("block/overlay/full", baseOverlayBlockModelHolder.getOrBuild(provider).getLocation())
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
				.texture("overlay-west", "#overlay")
			// @formatter:on
		);
	}

}
