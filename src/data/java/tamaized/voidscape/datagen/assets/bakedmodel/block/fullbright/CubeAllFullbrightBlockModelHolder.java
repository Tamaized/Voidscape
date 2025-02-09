package tamaized.voidscape.datagen.assets.bakedmodel.block.fullbright;

import net.neoforged.neoforge.client.model.generators.BlockModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.assets.bakedmodel.BlockModelHolder;

@Component
public class CubeAllFullbrightBlockModelHolder extends BlockModelHolder {

	@Autowired
	private CubeFullbrightBlockModelHolder parent;

	public ModelFile build(BlockModelProvider provider) {
		// @formatter:off
		return provider.withExistingParent("block/fullbright/cube_all", parent.getOrBuild(provider).getLocation())
			.texture("particle", "#all")
			.texture("down", "#all")
			.texture("up", "#all")
			.texture("north", "#all")
			.texture("east", "#all")
			.texture("south", "#all")
			.texture("west", "#all");
		// @formatter:on
	}

}
