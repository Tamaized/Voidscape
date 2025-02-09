package tamaized.voidscape.datagen.assets.bakedmodel.block.fullbright;

import net.neoforged.neoforge.client.model.generators.BlockModelProvider;
import tamaized.beanification.Component;
import tamaized.beanification.Directory;
import tamaized.voidscape.datagen.assets.bakedmodel.BlockModelHolder;

import java.util.List;

@Component
public class FullbrightBlockModelProviderFactory {

	@Directory(BlockModelHolder.class)
	private List<BlockModelHolder> blockModelHolders;

	public void make(BlockModelProvider provider) {
		blockModelHolders.forEach(holder -> holder.buildIfEmpty(provider));
	}

}
