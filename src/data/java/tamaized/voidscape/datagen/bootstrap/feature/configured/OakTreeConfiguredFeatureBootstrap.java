package tamaized.voidscape.datagen.bootstrap.feature.configured;

import net.minecraft.core.Direction;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import tamaized.beanification.Component;

@Component
public class OakTreeConfiguredFeatureBootstrap extends ConfiguredFeatureBootstrapHolder {

	@Override
	public String name() {
		return "oak_tree";
	}

	@Override
	public ConfiguredFeature<?, ?> make(BootstrapContext<ConfiguredFeature<?, ?>> context) {
		return new ConfiguredFeature<>(Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
			SimpleStateProvider.simple(Blocks.OAK_LOG.defaultBlockState()
				.setValue(RotatedPillarBlock.AXIS, Direction.Axis.Y)
			),
			new StraightTrunkPlacer(4, 2, 0),
			SimpleStateProvider.simple(Blocks.OAK_LEAVES.defaultBlockState()
				.setValue(LeavesBlock.DISTANCE, 7)
				.setValue(LeavesBlock.PERSISTENT, false)
			),
			new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.ZERO, 3),
			new TwoLayersFeatureSize(1, 0, 1)
		)
			.ignoreVines()
			.build());
	}
}
