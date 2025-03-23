package tamaized.voidscape.datagen.generator;

import net.neoforged.neoforge.data.event.GatherDataEvent;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.data.advancement.AdvancementProviderFactory;
import tamaized.voidscape.datagen.data.datamap.DatamapProviderFactory;
import tamaized.voidscape.datagen.data.loot.LootTableProviderFactory;
import tamaized.voidscape.datagen.data.tag.TagProviderFactory;

@Component
public class DataGenerator {

	@Autowired
	private TagProviderFactory tagProviderFactory;

	@Autowired
	private DatamapProviderFactory datamapProviderFactory;

	@Autowired
	private AdvancementProviderFactory advancementProviderFactory;

	@Autowired
	private LootTableProviderFactory lootTableProviderFactory;

	public void generate(GatherDataEvent event) {
		tagProviderFactory.generate(event);
		datamapProviderFactory.generate(event);
		event.getGenerator().addProvider(event.includeServer(), advancementProviderFactory.make(event));
		event.getGenerator().addProvider(event.includeServer(), lootTableProviderFactory.make(event));
	}

}
