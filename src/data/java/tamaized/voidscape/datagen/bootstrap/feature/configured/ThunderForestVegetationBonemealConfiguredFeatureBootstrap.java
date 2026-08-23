package tamaized.voidscape.datagen.bootstrap.feature.configured;

import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NetherForestVegetationConfig;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.registry.ModBlockComponentDirectory;
import tamaized.voidscape.registry.feature.ModFeatures;

@Component
public class ThunderForestVegetationBonemealConfiguredFeatureBootstrap extends ConfiguredFeatureBootstrapHolder {

	@Autowired
	private ModFeatures features;

	@Autowired
	private ModBlockComponentDirectory blocks;

	@Override
	public String name() {
		return "thunder_forest_vegetation_bonemeal";
	}

	@Override
	public ConfiguredFeature<?, ?> make(BootstrapContext<ConfiguredFeature<?, ?>> context) {
		return new ConfiguredFeature<>(Feature.NETHER_FOREST_VEGETATION, new NetherForestVegetationConfig(
			new WeightedStateProvider(WeightedList.<BlockState>builder()
				.add(
					blocks.thunderForestBiomeBlocks().THUNDER_ROOTS.get().defaultBlockState(),
					87
				)
				.add(
					blocks.thunderForestBiomeBlocks().THUNDER_FUNGUS.get().defaultBlockState(),
					11
				)
			),
			3,
			1
		));
	}
}
