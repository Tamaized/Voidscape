package tamaized.voidscape.datagen.assets.bakedmodel.block;

import net.neoforged.neoforge.client.model.generators.BlockModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.assets.bakedmodel.BlockModelHolder;
import tamaized.voidscape.datagen.assets.bakedmodel.block.fullbright.CubeColumnFullbrightBlockModelHolder;

@Component
public class FleshBlockBlockModelHolder extends BlockModelHolder {

	@Autowired
	private CubeColumnFullbrightBlockModelHolder cubeColumnFullbrightBlockModelHolder;

	public ModelFile build(BlockModelProvider provider) {
		return provider.withExistingParent("block/flesh_block", cubeColumnFullbrightBlockModelHolder.getOrBuild(provider).getLocation())
			.texture("end", "block/flesh_block_top")
			.texture("side", "block/flesh_block_side");
	}

}
