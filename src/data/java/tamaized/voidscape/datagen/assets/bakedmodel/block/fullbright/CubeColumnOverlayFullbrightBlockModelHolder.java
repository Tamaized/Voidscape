package tamaized.voidscape.datagen.assets.bakedmodel.block.fullbright;

import net.neoforged.neoforge.client.model.generators.BlockModelProvider;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.assets.bakedmodel.block.BlockModelHolder;

@Component
public class CubeColumnOverlayFullbrightBlockModelHolder extends BlockModelHolder {

	@Autowired
	private CubeOverlayFullbrightBlockModelHolder cubeOverlayFullbrightBlockModelHolder;

	public void build(BlockModelProvider provider) {
		set(
			// @formatter:off
			provider.withExistingParent("block/fullbright/cube_column_overlay", cubeOverlayFullbrightBlockModelHolder.get().orElseThrow().getLocation())
				.texture("particle", "#side")
				.texture("down", "#end")
				.texture("up", "#end")
				.texture("north", "#side")
				.texture("east", "#side")
				.texture("south", "#side")
				.texture("west", "#side")
				.texture("down-overlay", "#end-overlay")
				.texture("up-overlay", "#end-overlay")
				.texture("north-overlay", "#side-overlay")
				.texture("east-overlay", "#side-overlay")
				.texture("south-overlay", "#side-overlay")
				.texture("west-overlay", "#side-overlay")
			// @formatter:on
		);
	}

}
