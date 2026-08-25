package tamaized.voidscape.datagen.bootstrap.feature.configured;

import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.registry.ModBlockComponentDirectory;

@Component
public class NullTreeConfiguredFeatureBootstrap extends ConfiguredFeatureBootstrapHolder {

	@Autowired
	private ModBlockComponentDirectory blocks;

	@Override
	public String name() {
		return "null_tree";
	}

	@Override
	public ConfiguredFeature<?, ?> make(BootstrapContext<ConfiguredFeature<?, ?>> context) {
		return new ConfiguredFeature<>(Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
			SimpleStateProvider.simple(blocks.nullBiomeBlocks().NULL_WHITE.get()),
			new StraightTrunkPlacer(4, 2, 0),
			SimpleStateProvider.simple(blocks.nullBiomeBlocks().NULL_WHITE.get()),
			new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.ZERO, 3),
			new TwoLayersFeatureSize(1, 0, 1)
		)
			.belowTrunkProvider(SimpleStateProvider.simple(blocks.nullBiomeBlocks().NULL_BLACK.get()))
			.ignoreVines()
			.build());
	}
}
