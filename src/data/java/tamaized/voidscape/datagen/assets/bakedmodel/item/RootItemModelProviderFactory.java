package tamaized.voidscape.datagen.assets.bakedmodel.item;

import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import tamaized.beanification.Component;
import tamaized.beanification.Directory;
import tamaized.voidscape.datagen.assets.bakedmodel.ItemModelHolder;

import java.util.List;

@Component
public class RootItemModelProviderFactory {

	@Directory(ItemModelHolder.class)
	private List<ItemModelHolder> itemModelHolders;

	public void make(ItemModelProvider provider) {
		itemModelHolders.forEach(holder -> holder.buildIfEmpty(provider));
	}

}
