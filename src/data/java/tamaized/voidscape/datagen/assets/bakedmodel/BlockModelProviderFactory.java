package tamaized.voidscape.datagen.assets.bakedmodel;

import net.neoforged.neoforge.client.model.generators.BlockModelProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import tamaized.beanification.Component;
import tamaized.beanification.Directory;
import tamaized.voidscape.Voidscape;

import java.util.List;

@Component
public class BlockModelProviderFactory {

	@Directory(BlockModelHolder.class)
	private List<BlockModelHolder> blockModelHolders;

	public BlockModelProvider make(GatherDataEvent event) {
		return new BlockModelProvider(
			event.getGenerator().getPackOutput(),
			Voidscape.MODID,
			event.getExistingFileHelper()
		) {
			@Override
			protected void registerModels() {
				blockModelHolders.forEach(holder -> holder.buildIfEmpty(this));
			}
		};
	}

}
