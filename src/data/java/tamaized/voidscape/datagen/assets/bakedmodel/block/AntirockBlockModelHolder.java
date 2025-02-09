package tamaized.voidscape.datagen.assets.bakedmodel.block;

import net.neoforged.neoforge.client.model.generators.BlockModelProvider;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.assets.bakedmodel.BlockModelHolder;
import tamaized.voidscape.datagen.assets.bakedmodel.block.fullbright.TintedCubeAllFullbrightBlockModelHolder;

@Component
public class AntirockBlockModelHolder extends BlockModelHolder {

	@Autowired
	private TintedCubeAllFullbrightBlockModelHolder tintedCubeAllFullbrightBlockModelHolder;

	public void build(BlockModelProvider provider) {
		set(
			// @formatter:off
			provider.withExistingParent("block/antirock", tintedCubeAllFullbrightBlockModelHolder.getOrBuild(provider).getLocation())
				.texture("all", provider.mcLoc("block/bedrock"))
			// @formatter:on
		);
	}

}
