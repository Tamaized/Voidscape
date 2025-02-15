package tamaized.voidscape.datagen.assets.blockstate;

import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.datagen.assets.bakedmodel.BlockModelProviderFactory;

@Component
public class BlockStateProviderFactory {

	@Autowired
	private BlockModelProviderFactory blockModelProviderFactory;

	public BlockStateProvider make(GatherDataEvent event) {
		return new BlockStateProvider(
			event.getGenerator().getPackOutput(),
			Voidscape.MODID,
			event.getExistingFileHelper()
		) {
			@Override
			protected void registerStatesAndModels() {
				blockModelProviderFactory.makeBlockstates(this);
			}
		};
	}

}
