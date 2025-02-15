package tamaized.voidscape.datagen.assets.blockstate;

import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.beanification.Directory;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.datagen.assets.bakedmodel.BlockModelProviderFactory;

import java.util.List;

@Component
public class BlockStateProviderFactory {

	@Autowired
	private BlockModelProviderFactory blockModelProviderFactory;

	@Directory(BlockStateFactory.class)
	private List<BlockStateFactory> blockStates;

	public BlockStateProvider make(GatherDataEvent event) {
		return new BlockStateProvider(
			event.getGenerator().getPackOutput(),
			Voidscape.MODID,
			event.getExistingFileHelper()
		) {
			@Override
			protected void registerStatesAndModels() {
				blockModelProviderFactory.makeBlockstates(this);
				blockStates.forEach(factory -> factory.make(this));
			}
		};
	}

}
