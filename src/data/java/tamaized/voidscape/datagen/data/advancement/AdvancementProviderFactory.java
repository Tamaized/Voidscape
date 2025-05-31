package tamaized.voidscape.datagen.data.advancement;

import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.beanification.Directory;
import tamaized.voidscape.datagen.bootstrap.RegistryProvider;

import java.util.List;

@Component
public class AdvancementProviderFactory {

	@Autowired
	private RegistryProvider registryProvider;

	@Directory(AbstractAdvancementSubProvider.class)
	List<AdvancementProvider.AdvancementGenerator> subProviders;

	public AdvancementProvider make(GatherDataEvent event) {
		return new AdvancementProvider(
			event.getGenerator().getPackOutput(),
			registryProvider.retrieve(event),
			event.getExistingFileHelper(),
			subProviders
		);
	}

}
