package tamaized.voidscape.datagen.assets.bakedmodel;

import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.datagen.assets.bakedmodel.item.RootItemModelProviderFactory;

@Component
public class ItemModelProviderFactory {

	@Autowired
	private RootItemModelProviderFactory rootItemModelProviderFactory;

	public ItemModelProvider make(GatherDataEvent event) {
		return new ItemModelProvider(
			event.getGenerator().getPackOutput(),
			Voidscape.MODID,
			event.getExistingFileHelper()
		) {
			@Override
			protected void registerModels() {
				rootItemModelProviderFactory.make(this);
			}
		};
	}

}
