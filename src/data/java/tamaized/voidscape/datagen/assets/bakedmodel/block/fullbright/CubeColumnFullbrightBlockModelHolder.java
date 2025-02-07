package tamaized.voidscape.datagen.assets.bakedmodel.block.fullbright;

import net.neoforged.neoforge.client.model.generators.BlockModelProvider;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.assets.bakedmodel.block.BlockModelHolder;

@Component
public class CubeColumnFullbrightBlockModelHolder extends BlockModelHolder {

	@Autowired
	private CubeFullbrightBlockModelHolder cubeFullbrightBlockModelHolder;

	public void build(BlockModelProvider provider) {
		set(
			// @formatter:off
			provider.withExistingParent("block/fullbright/cube_column", cubeFullbrightBlockModelHolder.getOrBuild(provider).getLocation())
				.texture("particle", "#side")
				.texture("down", "#end")
				.texture("up", "#end")
				.texture("north", "#side")
				.texture("east", "#side")
				.texture("south", "#side")
				.texture("west", "#side")
			// @formatter:on
		);
	}

}
