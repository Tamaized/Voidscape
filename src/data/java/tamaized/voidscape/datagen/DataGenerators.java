package tamaized.voidscape.datagen;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.beanification.connector.fml.EventBusPostConstruct;
import tamaized.voidscape.datagen.generator.AssetsGenerator;
import tamaized.voidscape.datagen.generator.DataGenerator;


@Component
public class DataGenerators {

	@Autowired
	private AssetsGenerator assetsGenerator;

	@Autowired
	private DataGenerator dataGenerator;

	@EventBusPostConstruct
	private void register(IEventBus bus) {
		bus.addListener(GatherDataEvent.Client.class, event -> {
			assetsGenerator.generate(event);
			dataGenerator.generate(event);
		});
	}

}
