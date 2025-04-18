package tamaized.voidscape.datagen.bootstrap.feature.configured;

import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.features.config.BooleanFeatureConfig;
import tamaized.voidscape.registry.feature.ModFeatures;

@Component
public class ChorusConfiguredFeatureBootstrap extends ConfiguredFeatureBootstrapHolder {

	@Autowired
	private ModFeatures features;

	@Override
	public String name() {
		return "chorus";
	}

	@Override
	public ConfiguredFeature<?, ?> make(BootstrapContext<ConfiguredFeature<?, ?>> context) {
		return new ConfiguredFeature<>(Feature.CHORUS_PLANT, NoneFeatureConfiguration.INSTANCE);
	}
}
