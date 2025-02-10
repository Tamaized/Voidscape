package tamaized.voidscape.datagen.assets.bakedmodel;

import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import tamaized.beanification.Component;
import tamaized.beanification.Directory;
import tamaized.voidscape.Voidscape;

import java.util.List;

@Component
public class ItemModelProviderFactory {

	@Directory(ItemModelHolder.class)
	private List<ItemModelHolder> itemModelHolders;

	public ItemModelProvider make(GatherDataEvent event) {
		return new ItemModelProvider(
			event.getGenerator().getPackOutput(),
			Voidscape.MODID,
			event.getExistingFileHelper()
		) {
			@Override
			protected void registerModels() {
				itemModelHolders.forEach(holder -> holder.buildIfEmpty(this));
			}
		};
	}

}
