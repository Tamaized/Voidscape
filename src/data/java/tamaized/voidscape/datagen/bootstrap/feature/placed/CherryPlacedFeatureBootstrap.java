package tamaized.voidscape.datagen.bootstrap.feature.placed;

import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.features.TreeFeatures;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.placement.BlockPredicateFilter;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import tamaized.beanification.Component;
import tamaized.voidscape.features.placements.SeekDownPlacementMod;

import java.util.List;

@Component
public class CherryPlacedFeatureBootstrap extends PlacedFeatureBootstrapHolder {

	@Override
	public String name() {
		return "cherry";
	}

	@Override
	public PlacedFeature make(BootstrapContext<PlacedFeature> context) {
		return new PlacedFeature(
			context.lookup(Registries.CONFIGURED_FEATURE).getOrThrow(TreeFeatures.CHERRY),
			List.of(
				new SeekDownPlacementMod(true),
				BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(
					Blocks.CHERRY_SAPLING.defaultBlockState(),
					Vec3i.ZERO
				))
			)
		);
	}
}
