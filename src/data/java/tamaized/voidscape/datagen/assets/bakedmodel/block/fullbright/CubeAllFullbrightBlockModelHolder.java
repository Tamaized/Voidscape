package tamaized.voidscape.datagen.assets.bakedmodel.block.fullbright;

import net.neoforged.neoforge.client.model.generators.BlockModelProvider;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.assets.bakedmodel.block.BlockModelHolder;

@Component
public class CubeAllFullbrightBlockModelHolder extends BlockModelHolder {

	@Autowired
	private CubeFullbrightBlockModelHolder cubeFullbrightBlockModelHolder;

	public void build(BlockModelProvider provider) {
		set(
			// @formatter:off
			provider.withExistingParent("block/fullbright/cube_all", cubeFullbrightBlockModelHolder.get().orElseThrow().getLocation())
				.texture("particle", "#all")
				.texture("down", "#all")
				.texture("up", "#all")
				.texture("north", "#all")
				.texture("east", "#all")
				.texture("south", "#all")
				.texture("west", "#all")
			// @formatter:on
		);
	}

}
