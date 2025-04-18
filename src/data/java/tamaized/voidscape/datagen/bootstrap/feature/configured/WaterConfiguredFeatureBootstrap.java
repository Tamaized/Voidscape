package tamaized.voidscape.datagen.bootstrap.feature.configured;

import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluids;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.features.config.FluidFeatureConfig;
import tamaized.voidscape.registry.feature.ModFeatures;

@Component
public class WaterConfiguredFeatureBootstrap extends ConfiguredFeatureBootstrapHolder {

	@Autowired
	private ModFeatures features;

	@Override
	public String name() {
		return "water";
	}

	@Override
	public ConfiguredFeature<?, ?> make(BootstrapContext<ConfiguredFeature<?, ?>> context) {
		return new ConfiguredFeature<>(features.FLUID.get(), new FluidFeatureConfig(
			Fluids.WATER.defaultFluidState()
				.setValue(FlowingFluid.FALLING, true)
		));
	}
}
