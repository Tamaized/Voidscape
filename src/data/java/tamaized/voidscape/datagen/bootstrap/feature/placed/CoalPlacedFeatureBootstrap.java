package tamaized.voidscape.datagen.bootstrap.feature.placed;

import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.bootstrap.feature.configured.CoalConfiguredFeatureBootstrap;
import tamaized.voidscape.features.placements.RandomYPlacementMod;

import java.util.List;

@Component
public class CoalPlacedFeatureBootstrap extends PlacedFeatureBootstrapHolder {

	@Autowired
	private CoalConfiguredFeatureBootstrap parentFeature;

	@Override
	public String name() {
		return "coal";
	}

	@Override
	public PlacedFeature make(BootstrapContext<PlacedFeature> context) {
		return new PlacedFeature(
			parentFeature.get().orElseThrow(),
			List.of(
				CountPlacement.of(2),
				InSquarePlacement.spread(),
				new RandomYPlacementMod(13)
			)
		);
	}
}
