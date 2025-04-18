package tamaized.voidscape.datagen.bootstrap.feature.configured;

import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.HugeFungusConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.placement.BlockPredicateFilter;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.util.HugeFungusConfiguredFeatureUtil;
import tamaized.voidscape.features.config.ClusterConfig;
import tamaized.voidscape.registry.ModBlockComponentDirectory;
import tamaized.voidscape.registry.feature.ModFeatures;

import java.util.List;

@Component
public class ThunderFungusBlockConfiguredFeatureBootstrap extends ConfiguredFeatureBootstrapHolder {

	@Autowired
	private ModFeatures features;

	@Autowired
	private ModBlockComponentDirectory blocks;

	@Autowired
	private HugeFungusConfiguredFeatureUtil hugeFungusConfiguredFeatureUtil;

	@Override
	public String name() {
		return "thunder_fungus_block";
	}

	@Override
	public ConfiguredFeature<?, ?> make(BootstrapContext<ConfiguredFeature<?, ?>> context) {
		return new ConfiguredFeature<>(Feature.RANDOM_PATCH, new RandomPatchConfiguration(
			48,
			3,
			3,
			Holder.direct(new PlacedFeature(
				Holder.direct(new ConfiguredFeature<>(
					Feature.SIMPLE_BLOCK,
					new SimpleBlockConfiguration(BlockStateProvider.simple(blocks.thunderForestBiomeBlocks().THUNDER_FUNGUS.get()))
				)),
				List.of(
					BlockPredicateFilter.forPredicate(BlockPredicate.ONLY_IN_AIR_PREDICATE)
				)
			))
		));
	}
}
