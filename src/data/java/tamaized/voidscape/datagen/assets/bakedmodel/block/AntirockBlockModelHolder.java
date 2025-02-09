package tamaized.voidscape.datagen.assets.bakedmodel.block;

import net.neoforged.neoforge.client.model.generators.BlockModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.assets.bakedmodel.BlockModelHolder;
import tamaized.voidscape.datagen.assets.bakedmodel.block.fullbright.TintedCubeAllFullbrightBlockModelHolder;
import tamaized.voidscape.registry.ModBlockComponentDirectory;

@Component
public class AntirockBlockModelHolder extends BlockModelHolder {

	@Autowired
	private ModBlockComponentDirectory blocks;

	@Autowired
	private TintedCubeAllFullbrightBlockModelHolder parent;

	public ModelFile build(BlockModelProvider provider) {
		return provider.withExistingParent(
				name(blocks.spireBlocks().ANTIROCK),
				parent.getOrBuild(provider).getLocation()
			)
			.texture("all", provider.mcLoc("block/bedrock"));
	}

}
