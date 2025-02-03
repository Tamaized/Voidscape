package tamaized.voidscape.datagen.generator;

import net.neoforged.neoforge.data.event.GatherDataEvent;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.assets.bakedmodel.BlockModelProviderFactory;

@Component
public class AssetsGenerator {

	@Autowired
	private BlockModelProviderFactory blockModelProviderFactory;

	public void generate(GatherDataEvent event) {
		event.getGenerator().addProvider(event.includeClient(), blockModelProviderFactory.make(event));
	}

}
