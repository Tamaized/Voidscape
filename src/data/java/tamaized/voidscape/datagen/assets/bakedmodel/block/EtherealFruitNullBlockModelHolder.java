package tamaized.voidscape.datagen.assets.bakedmodel.block;

import net.minecraft.client.renderer.RenderType;
import net.neoforged.neoforge.client.model.generators.BlockModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.assets.bakedmodel.BlockModelHolder;
import tamaized.voidscape.datagen.assets.bakedmodel.block.fullbright.TintedCrossFullbrightBlockModelHolder;
import tamaized.voidscape.registry.ModBlockComponentDirectory;

@Component
public class EtherealFruitNullBlockModelHolder extends BlockModelHolder {

	@Autowired
	private ModBlockComponentDirectory blocks;

	@Autowired
	private TintedCrossFullbrightBlockModelHolder parent;

	public ModelFile build(BlockModelProvider provider) {
		return provider.withExistingParent(
				name(blocks.etherealFruitBlocks().NULL),
				parent.getOrBuild(provider).getLocation()
			)
			.renderType(RenderType.cutoutMipped().name)
			.texture("cross", "block/ethereal_fruit_null")
			.texture("particle", "#cross");
	}

}
