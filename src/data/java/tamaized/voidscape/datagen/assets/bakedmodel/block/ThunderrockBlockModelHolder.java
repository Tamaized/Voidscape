package tamaized.voidscape.datagen.assets.bakedmodel.block;

import net.neoforged.neoforge.client.model.generators.BlockModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.assets.bakedmodel.BlockModelHolder;
import tamaized.voidscape.registry.ModBlockComponentDirectory;

@Component
public class ThunderrockBlockModelHolder extends BlockModelHolder {

	@Autowired
	private ModBlockComponentDirectory blocks;

	public ModelFile build(BlockModelProvider provider) {
		return provider.withExistingParent(
				name(blocks.spireBlocks().THUNDERROCK),
				"block/cube_all"
			)
			.texture("all", provider.mcLoc("block/bedrock"))
			.texture("particle", "#all");
	}

}
