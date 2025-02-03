package tamaized.voidscape.datagen.assets.bakedmodel;

import net.neoforged.neoforge.client.model.generators.BlockModelProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.datagen.assets.bakedmodel.block.fullbright.FullbrightBlockModelProviderFactory;

@Component
public class BlockModelProviderFactory {

	@Autowired
	private FullbrightBlockModelProviderFactory fullbrightBlockModelProviderFactory;

	public BlockModelProvider make(GatherDataEvent event) {
		return new BlockModelProvider(
			event.getGenerator().getPackOutput(),
			Voidscape.MODID,
			event.getExistingFileHelper()
		) {
			@Override
			protected void registerModels() {
				fullbrightBlockModelProviderFactory.make(this);
			}
		};
	}

}
