package tamaized.voidscape.datagen.data.advancement;

import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.beanification.Directory;
import tamaized.voidscape.datagen.RegistryProvider;
import tamaized.voidscape.datagen.data.tag.block.BlockTagProviderFactory;
import tamaized.voidscape.datagen.data.tag.damagetype.DamageTypeTagProviderFactory;
import tamaized.voidscape.datagen.data.tag.item.ItemTagProviderFactory;

import java.util.List;

@Component
public class AdvancementProviderFactory {

	@Autowired
	private RegistryProvider registryProvider;

	@Directory(AbstractAdvancementSubProvider.class)
	List<AdvancementProvider.AdvancementGenerator> subProviders;

	public void generate(GatherDataEvent event) {
		event.getGenerator().addProvider(event.includeServer(), new AdvancementProvider(
			event.getGenerator().getPackOutput(),
			registryProvider.retrieve(event),
			event.getExistingFileHelper(),
			subProviders
		));
	}

}
