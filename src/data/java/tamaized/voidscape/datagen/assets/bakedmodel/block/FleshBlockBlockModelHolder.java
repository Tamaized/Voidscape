package tamaized.voidscape.datagen.assets.bakedmodel.block;

import net.neoforged.neoforge.client.model.generators.BlockModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.assets.bakedmodel.BlockModelHolder;
import tamaized.voidscape.datagen.assets.bakedmodel.block.fullbright.CubeColumnFullbrightBlockModelHolder;
import tamaized.voidscape.registry.ModBlockComponentDirectory;

@Component
public class FleshBlockBlockModelHolder extends BlockModelHolder {

	@Autowired
	private ModBlockComponentDirectory blocks;

	@Autowired
	private CubeColumnFullbrightBlockModelHolder parent;

	public ModelFile build(BlockModelProvider provider) {
		return provider.withExistingParent(
				name(blocks.materialBlocks().FLESH_BLOCK),
				parent.getOrBuild(provider).getLocation()
			)
			.texture("end", "block/flesh_block_top")
			.texture("side", "block/flesh_block_side");
	}

}
