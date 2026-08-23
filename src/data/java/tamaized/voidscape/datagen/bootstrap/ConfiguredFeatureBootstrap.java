package tamaized.voidscape.datagen.bootstrap;

import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import tamaized.beanification.Component;
import tamaized.beanification.Directory;
import tamaized.voidscape.datagen.bootstrap.feature.configured.ConfiguredFeatureBootstrapHolder;

import java.util.List;

@Component
public class ConfiguredFeatureBootstrap implements IBootstrap {

	@Directory(ConfiguredFeatureBootstrapHolder.class)
	private List<ConfiguredFeatureBootstrapHolder> configuredFeatures;

	@Override
	public RegistrySetBuilder bootstrap(RegistrySetBuilder builder) {
		return builder.add(Registries.CONFIGURED_FEATURE, context -> configuredFeatures.forEach(configuredFeature -> configuredFeature.getOrMake(context)));
	}

}
