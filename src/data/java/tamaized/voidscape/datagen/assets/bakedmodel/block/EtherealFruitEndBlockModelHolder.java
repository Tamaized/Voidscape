package tamaized.voidscape.datagen.assets.bakedmodel.block;

import net.minecraft.client.renderer.RenderType;
import net.neoforged.neoforge.client.model.generators.BlockModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.assets.bakedmodel.BlockModelHolder;
import tamaized.voidscape.datagen.assets.bakedmodel.block.fullbright.TintedCrossFullbrightBlockModelHolder;

@Component
public class EtherealFruitEndBlockModelHolder extends BlockModelHolder {

	@Autowired
	private TintedCrossFullbrightBlockModelHolder tintedCrossFullbrightBlockModelHolder;

	public ModelFile build(BlockModelProvider provider) {
		return provider.withExistingParent("block/ethereal_fruit_end", tintedCrossFullbrightBlockModelHolder.getOrBuild(provider).getLocation())
			.renderType(RenderType.cutoutMipped().name)
			.texture("cross", "block/ethereal_fruit_end")
			.texture("particle", "#cross");
	}

}
