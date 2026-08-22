package tamaized.voidscape.datagen.data.tag.damagetype;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.beanification.Directory;
import tamaized.datagenutil.data.tag.ExposedKeyTagProvider;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.datagen.bootstrap.RegistryProvider;

import java.util.List;

@Component
public class DamageTypeTagProviderFactory {

	@Autowired
	private RegistryProvider registryProvider;

	@Directory(IDamageTypeTagProviderFactory.class)
	List<IDamageTypeTagProviderFactory> factories;

	public ExposedKeyTagProvider<DamageType> make(GatherDataEvent event) {
		return new ExposedKeyTagProvider<>(
			event.getGenerator().getPackOutput(),
			Registries.DAMAGE_TYPE,
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
