package tamaized.voidscape.datagen.assets.bakedmodel.block;

import net.neoforged.neoforge.client.model.generators.BlockModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.assets.bakedmodel.BlockModelHolder;
import tamaized.voidscape.datagen.assets.bakedmodel.block.fullbright.SlabTopFullbrightBlockModelHolder;
import tamaized.voidscape.datagen.assets.bakedmodel.block.fullbright.StairsFullbrightBlockModelHolder;
import tamaized.voidscape.registry.ModBlockComponentDirectory;

@Component
public class ThunderStairsBlockModelHolder extends BlockModelHolder {

	@Autowired
	private ModBlockComponentDirectory blocks;

	@Autowired
	private StairsFullbrightBlockModelHolder parent;

	public ModelFile build(BlockModelProvider provider) {
		return provider.withExistingParent(
				name(blocks.thunderForestBiomeBlocks().THUNDER_STAIRS),
				parent.getOrBuild(provider).getLocation()
			)
			.texture("bottom", "block/thunder_planks")
			.texture("side", "block/thunder_planks")
			.texture("top", "block/thunder_planks");
	}

}
