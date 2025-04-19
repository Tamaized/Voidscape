package tamaized.voidscape.datagen.bootstrap.feature.placed;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.features.TreeFeatures;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import tamaized.beanification.Component;
import tamaized.voidscape.features.placements.SeekDownPlacementMod;

import java.util.List;

@Component
public class CrimsonFungusPlacedFeatureBootstrap extends PlacedFeatureBootstrapHolder {

	@Override
	public String name() {
		return "crimson_fungus";
	}

	@Override
	public PlacedFeature make(BootstrapContext<PlacedFeature> context) {
		return new PlacedFeature(
			context.lookup(Registries.CONFIGURED_FEATURE).getOrThrow(
				TreeFeatures.CRIMSON_FUNGUS
			),
			List.of(
				CountPlacement.of(6),
				InSquarePlacement.spread(),
				new SeekDownPlacementMod(true)
			)
		);
	}
}
