package tamaized.voidscape.datagen.data.tag;

import net.neoforged.neoforge.data.event.GatherDataEvent;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.data.tag.block.BlockTagProviderFactory;
import tamaized.voidscape.datagen.data.tag.damagetype.DamageTypeTagProviderFactory;
import tamaized.voidscape.datagen.data.tag.item.ItemTagProviderFactory;

@Component
public class TagProviderFactory {

	@Autowired
	private BlockTagProviderFactory blockTagProviderFactory;

	@Autowired
	private ItemTagProviderFactory itemTagProviderFactory;

	@Autowired
	private DamageTypeTagProviderFactory damageTypeTagProviderFactory;

	public void generate(GatherDataEvent event) {
		event.getGenerator().addProvider(event.includeServer(), blockTagProviderFactory.make(event));
		event.getGenerator().addProvider(event.includeServer(), itemTagProviderFactory.make(event));
		event.getGenerator().addProvider(event.includeServer(), damageTypeTagProviderFactory.make(event));
	}

}
