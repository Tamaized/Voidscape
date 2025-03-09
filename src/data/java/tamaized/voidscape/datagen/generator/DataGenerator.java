package tamaized.voidscape.datagen.generator;

import net.neoforged.neoforge.data.event.GatherDataEvent;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.data.tag.TagProviderFactory;

@Component
public class DataGenerator {

	@Autowired
	private TagProviderFactory tagProviderFactory;

	public void generate(GatherDataEvent event) {
		tagProviderFactory.generate(event);
	}

}
