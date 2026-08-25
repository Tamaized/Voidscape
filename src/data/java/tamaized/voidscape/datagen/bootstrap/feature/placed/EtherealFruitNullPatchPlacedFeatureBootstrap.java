package tamaized.voidscape.datagen.bootstrap.feature.placed;

import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.placement.*;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.bootstrap.feature.configured.EtherealFruitNullConfiguredFeatureBootstrap;
import tamaized.voidscape.datagen.util.PlacementModUtil;
import tamaized.voidscape.features.placements.SeekDownPlacementMod;

import java.util.List;

@Component
public class EtherealFruitNullPatchPlacedFeatureBootstrap extends PlacedFeatureBootstrapHolder {

	@Autowired
	private EtherealFruitNullConfiguredFeatureBootstrap parentFeature;

	@Autowired
	private PlacementModUtil placementModUtil;

	@Override
	public String name() {
		return "ethereal_fruit_null_patch";
	}

	@Override
	public PlacedFeature make(BootstrapContext<PlacedFeature> context) {
		return new PlacedFeature(
			parentFeature.get().orElseThrow(),
			List.of(
				CountPlacement.of(6),
				InSquarePlacement.spread(),
				new SeekDownPlacementMod(true),
				placementModUtil.AIR_ABOVE,
				placementModUtil.NOT_AIR_BELOW,
				BlockPredicateFilter.forPredicate(BlockPredicate.ONLY_IN_AIR_PREDICATE),
				BiomeFilter.biome(),
				CountPlacement.of(16),
				RandomOffsetPlacement.ofTriangle(3, 3),
				BlockPredicateFilter.forPredicate(BlockPredicate.ONLY_IN_AIR_PREDICATE)
			)
		);
	}
}
