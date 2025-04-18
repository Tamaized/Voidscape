package tamaized.voidscape.datagen.bootstrap.feature.configured;

import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.util.HugeFungusConfiguredFeatureUtil;
import tamaized.voidscape.features.config.BooleanFeatureConfig;
import tamaized.voidscape.registry.ModBlockComponentDirectory;
import tamaized.voidscape.registry.feature.ModFeatures;

@Component
public class ThunderspiresConfiguredFeatureBootstrap extends ConfiguredFeatureBootstrapHolder {

	@Autowired
	private ModFeatures features;

	@Autowired
	private ModBlockComponentDirectory blocks;

	@Autowired
	private HugeFungusConfiguredFeatureUtil hugeFungusConfiguredFeatureUtil;

	@Override
	public String name() {
		return "thunderspires";
	}

	@Override
	public ConfiguredFeature<?, ?> make(BootstrapContext<ConfiguredFeature<?, ?>> context) {
		return new ConfiguredFeature<>(features.SPIRE.get(), new BooleanFeatureConfig(
			false
		));
	}
}
