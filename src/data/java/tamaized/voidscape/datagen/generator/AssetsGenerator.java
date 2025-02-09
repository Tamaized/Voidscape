package tamaized.voidscape.datagen.generator;

import net.neoforged.neoforge.data.event.GatherDataEvent;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.assets.bakedmodel.BlockModelProviderFactory;
import tamaized.voidscape.datagen.assets.bakedmodel.ItemModelProviderFactory;

@Component
public class AssetsGenerator {

	@Autowired
	private BlockModelProviderFactory blockModelProviderFactory;

	@Autowired
	private ItemModelProviderFactory itemModelProviderFactory;

	public void generate(GatherDataEvent event) {
		event.getGenerator().addProvider(event.includeClient(), blockModelProviderFactory.make(event));
		event.getGenerator().addProvider(event.includeClient(), itemModelProviderFactory.make(event));
	}

}
