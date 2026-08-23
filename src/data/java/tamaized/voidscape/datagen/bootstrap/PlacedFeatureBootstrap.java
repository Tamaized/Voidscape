package tamaized.voidscape.datagen.bootstrap;

import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.beanification.Directory;
import tamaized.voidscape.datagen.bootstrap.feature.placed.PlacedFeatureBootstrapHolder;

import java.util.List;

@Component
public class PlacedFeatureBootstrap implements IBootstrap {

	@Directory(PlacedFeatureBootstrapHolder.class)
	private List<PlacedFeatureBootstrapHolder> placedFeatures;

	@Autowired
	private ConfiguredFeatureBootstrap configuredFeatureBootstrap;

	@Override
	public int priority() {
		return configuredFeatureBootstrap.priority() + 1;
	}

	@Override
	public RegistrySetBuilder bootstrap(RegistrySetBuilder builder) {
		return builder.add(Registries.PLACED_FEATURE, context -> placedFeatures.forEach(placedFeature -> placedFeature.getOrMake(context)));
	}

}
