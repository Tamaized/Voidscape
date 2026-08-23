package tamaized.voidscape.datagen.data.datamap;

import net.neoforged.neoforge.data.event.GatherDataEvent;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;

@Component
public class DatamapProviderFactory {

	@Autowired
	private CompostablesDatamapProviderFactory compostablesDatamapProviderFactory;

	public void generate(GatherDataEvent event) {
		event.getGenerator().addProvider(true, compostablesDatamapProviderFactory.make(event));
	}

}
