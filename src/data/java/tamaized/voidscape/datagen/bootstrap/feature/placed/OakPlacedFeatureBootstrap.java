package tamaized.voidscape.datagen.bootstrap.feature.placed;

import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.features.TreeFeatures;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.placement.*;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.bootstrap.feature.configured.NetherGoldConfiguredFeatureBootstrap;
import tamaized.voidscape.datagen.util.PlacementModUtil;
import tamaized.voidscape.features.placements.SeekDownPlacementMod;

import java.util.List;

@Component
public class OakPlacedFeatureBootstrap extends PlacedFeatureBootstrapHolder {

	@Autowired
	private NetherGoldConfiguredFeatureBootstrap parentFeature;

	@Autowired
	private PlacementModUtil placementModUtil;

	@Override
	public String name() {
		return "oak";
	}

	@Override
	public PlacedFeature make(BootstrapContext<PlacedFeature> context) {
		return new PlacedFeature(
			context.lookup(Registries.CONFIGURED_FEATURE).getOrThrow(
				TreeFeatures.OAK
			),
			List.of(
				CountPlacement.of(6),
				InSquarePlacement.spread(),
				new SeekDownPlacementMod(true),
				BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(
					Blocks.OAK_SAPLING.defaultBlockState(),
					Vec3i.ZERO
				)),
				BiomeFilter.biome()
			)
		);
	}
}
