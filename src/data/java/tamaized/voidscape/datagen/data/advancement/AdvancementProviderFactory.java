package tamaized.voidscape.datagen.data.advancement;

import net.minecraft.data.advancements.AdvancementProvider;
import net.minecraft.data.advancements.AdvancementSubProvider;
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
	List<AdvancementSubProvider> subProviders;

	public AdvancementProvider make(GatherDataEvent event) {
		return new AdvancementProvider(
			event.getGenerator().getPackOutput(),
			registryProvider.retrieve(event),
			subProviders
		);
	}

}
