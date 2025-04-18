package tamaized.voidscape.datagen.bootstrap.feature.configured;

import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.util.HugeFungusConfiguredFeatureUtil;
import tamaized.voidscape.registry.ModBlockComponentDirectory;
import tamaized.voidscape.registry.feature.ModFeatures;

@Component
public class ThunderVinesConfiguredFeatureBootstrap extends ConfiguredFeatureBootstrapHolder {

	@Autowired
	private ModFeatures features;

	@Autowired
	private ModBlockComponentDirectory blocks;

	@Autowired
	private HugeFungusConfiguredFeatureUtil hugeFungusConfiguredFeatureUtil;

	@Override
	public String name() {
		return "thunder_vines";
	}

	@Override
	public ConfiguredFeature<?, ?> make(BootstrapContext<ConfiguredFeature<?, ?>> context) {
		return new ConfiguredFeature<>(features.THUNDER_VINES.get(), NoneFeatureConfiguration.INSTANCE);
	}
}
