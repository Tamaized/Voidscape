package tamaized.voidscape.datagen.bootstrap.feature.configured;

import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.features.config.ClusterConfig;
import tamaized.voidscape.registry.feature.ModFeatures;

@Component
public class AmethystConfiguredFeatureBootstrap extends ConfiguredFeatureBootstrapHolder {

	@Autowired
	private ModFeatures features;

	@Override
	public String name() {
		return "amethyst";
	}

	@Override
	public ConfiguredFeature<?, ?> make(BootstrapContext<ConfiguredFeature<?, ?>> context) {
		return new ConfiguredFeature<>(features.CLUSTER.get(), new ClusterConfig(
			BlockStateProvider.simple(Blocks.AMETHYST_BLOCK),
			BlockPredicate.matchesBlocks(Blocks.END_STONE),
			0.5F,
			9
		));
	}
}
