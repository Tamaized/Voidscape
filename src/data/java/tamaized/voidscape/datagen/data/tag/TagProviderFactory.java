package tamaized.voidscape.datagen.data.tag;

import net.neoforged.neoforge.data.event.GatherDataEvent;
import tamaized.beanification.Component;
import tamaized.beanification.Directory;

import java.util.List;

@Component
public class TagProviderFactory {

	@Directory(ITagProviderFactory.class)
	private List<ITagProviderFactory<?>> providers;

	public void generate(GatherDataEvent event) {
		providers.forEach(provider -> event.getGenerator().addProvider(event.includeServer(), provider.make(event)));
	}

}
