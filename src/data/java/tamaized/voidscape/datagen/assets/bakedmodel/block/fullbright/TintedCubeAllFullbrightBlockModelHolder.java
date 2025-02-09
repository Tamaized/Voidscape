package tamaized.voidscape.datagen.assets.bakedmodel.block.fullbright;

import net.neoforged.neoforge.client.model.generators.BlockModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.assets.bakedmodel.BlockModelHolder;

@Component
public class TintedCubeAllFullbrightBlockModelHolder extends BlockModelHolder {

	@Autowired
	private TintedCubeFullbrightBlockModelHolder tintedCubeFullbrightBlockModelHolder;

	public ModelFile build(BlockModelProvider provider) {
		// @formatter:off
		return provider.withExistingParent("block/fullbright/tinted_cube_all", tintedCubeFullbrightBlockModelHolder.getOrBuild(provider).getLocation())
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
