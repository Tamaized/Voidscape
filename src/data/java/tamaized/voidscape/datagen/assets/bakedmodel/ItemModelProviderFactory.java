package tamaized.voidscape.datagen.assets.bakedmodel;

import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.beanification.Directory;
import tamaized.voidscape.Voidscape;

import java.util.List;
import java.util.Objects;

@Component
public class ItemModelProviderFactory {

	@Directory(ItemModelHolder.class)
	private List<ItemModelHolder> itemModelHolders;

	@Autowired
	private BlockModelProviderFactory blockModelProviderFactory;

	public ItemModelProvider make(GatherDataEvent event) {
		return new ItemModelProvider(
			event.getGenerator().getPackOutput(),
			Voidscape.MODID,
			event.getExistingFileHelper()
		) {
			@Override
			protected void registerModels() {
				itemModelHolders.forEach(holder -> holder.buildIfEmpty(this));
				blockModelProviderFactory.makeBlockItems(this);
			}
		};
	}

	public void addLangEntries(LanguageProvider provider) {
		itemModelHolders.forEach(holder -> holder.lang().ifPresent(
			lang -> provider.addItem(Objects.requireNonNull(holder.itemForName()), lang)
		));
	}

}
