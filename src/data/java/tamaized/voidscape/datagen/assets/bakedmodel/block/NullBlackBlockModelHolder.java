package tamaized.voidscape.datagen.assets.bakedmodel.block;

import net.neoforged.neoforge.client.model.generators.BlockModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.assets.bakedmodel.BlockModelHolder;
import tamaized.voidscape.datagen.assets.bakedmodel.block.fullbright.CubeAllFullbrightBlockModelHolder;

@Component
public class NullBlackBlockModelHolder extends BlockModelHolder {

	@Autowired
	private CubeAllFullbrightBlockModelHolder parent;

	public ModelFile build(BlockModelProvider provider) {
		return provider.withExistingParent("block/null_black", parent.getOrBuild(provider).getLocation())
			.ao(false)
			.texture("all", "block/black")
			.texture("particle", "#all");
	}

}
