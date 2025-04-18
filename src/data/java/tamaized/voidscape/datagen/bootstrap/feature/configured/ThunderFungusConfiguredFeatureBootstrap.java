package tamaized.voidscape.datagen.bootstrap.feature.configured;

import net.minecraft.core.Direction;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.HugeFungusConfiguration;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.util.HugeFungusConfiguredFeatureUtil;
import tamaized.voidscape.registry.ModBlockComponentDirectory;
import tamaized.voidscape.registry.feature.ModFeatures;

@Component
public class ThunderFungusConfiguredFeatureBootstrap extends ConfiguredFeatureBootstrapHolder {

	@Autowired
	private ModFeatures features;

	@Autowired
	private ModBlockComponentDirectory blocks;

	@Autowired
	private HugeFungusConfiguredFeatureUtil hugeFungusConfiguredFeatureUtil;

	@Override
	public String name() {
		return "thunder_fungus";
	}

	@Override
	public ConfiguredFeature<?, ?> make(BootstrapContext<ConfiguredFeature<?, ?>> context) {
		return new ConfiguredFeature<>(Feature.HUGE_FUNGUS, new HugeFungusConfiguration(
			blocks.thunderForestBiomeBlocks().THUNDER_NYLIUM.get().defaultBlockState(),
			blocks.thunderForestBiomeBlocks().THUNDER_STEM.get().defaultBlockState().setValue(RotatedPillarBlock.AXIS, Direction.Axis.Y),
			blocks.thunderForestBiomeBlocks().THUNDER_WART.get().defaultBlockState(),
			Blocks.SHROOMLIGHT.defaultBlockState(),
			hugeFungusConfiguredFeatureUtil.BLOCK_PREDICATE,
			false
		));
	}
}
