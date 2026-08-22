package tamaized.voidscape.datagen.data.tag.entity;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.beanification.Directory;
import tamaized.datagenutil.data.tag.ExposedEntityTypeTagProvider;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.datagen.bootstrap.RegistryProvider;

import java.util.List;

@Component
public class EntityTypeTagProviderFactory {

	@Autowired
	private RegistryProvider registryProvider;

	@Directory(IEntityTypeTagProviderFactory.class)
	List<IEntityTypeTagProviderFactory> factories;

	public EntityTypeTagsProvider make(GatherDataEvent event) {
		return new ExposedEntityTypeTagProvider(
			event.getGenerator().getPackOutput(),
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
