package tamaized.voidscape.datagen.assets.bakedmodel.block.overlay;

import net.neoforged.neoforge.client.model.generators.BlockModelProvider;
import tamaized.beanification.Component;
import tamaized.beanification.Directory;
import tamaized.voidscape.datagen.assets.bakedmodel.block.BlockModelHolder;

import java.util.List;

@Component
public class OverlayBlockModelProviderFactory {

	@Directory(BlockModelHolder.class)
	private List<BlockModelHolder> blockModelHolders;


	public void make(BlockModelProvider provider) {
		blockModelHolders.forEach(holder -> holder.buildIfEmpty(provider));
	}

}
