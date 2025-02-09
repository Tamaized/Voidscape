package tamaized.voidscape.datagen.assets.bakedmodel.block.fullbright;

import net.neoforged.neoforge.client.model.generators.BlockModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.assets.bakedmodel.BlockModelHolder;

@Component
public class CubeColumnFullbrightBlockModelHolder extends BlockModelHolder {

	@Autowired
	private CubeFullbrightBlockModelHolder parent;

	public ModelFile build(BlockModelProvider provider) {
		// @formatter:off
		return provider.withExistingParent("block/fullbright/cube_column", parent.getOrBuild(provider).getLocation())
			.texture("particle", "#side")
			.texture("down", "#end")
			.texture("up", "#end")
			.texture("north", "#side")
			.texture("east", "#side")
			.texture("south", "#side")
			.texture("west", "#side");
		// @formatter:on
	}

}
