package tamaized.voidscape.datagen.data.tag.item;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.beanification.Directory;
import tamaized.datagenutil.data.tag.ExposedKeyTagProvider;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.datagen.bootstrap.RegistryProvider;

import java.util.List;

@Component
public class ItemTagProviderFactory {

	@Autowired
	private RegistryProvider registryProvider;

	@Directory(IItemTagProviderFactory.class)
	List<IItemTagProviderFactory> factories;

	public TagsProvider<Item> make(GatherDataEvent event) {
		return new ExposedKeyTagProvider<>(
			event.getGenerator().getPackOutput(),
			Registries.ITEM,
			registryProvider.retrieve(event),
			Voidscape.MODID
		) {
			@Override
			protected void addTags(HolderLookup.Provider provider) {
				factories.forEach(f -> f.make(this, provider));
			}
		};
	}

}
