package tamaized.voidscape.datagen.bootstrap.feature.placed;

import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.bootstrap.feature.configured.ThunderspiresConfiguredFeatureBootstrap;
import tamaized.voidscape.datagen.util.PlacementModUtil;
import tamaized.voidscape.features.placements.SeekDownPlacementMod;

import java.util.List;

@Component
public class ThunderspirePlacedFeatureBootstrap extends PlacedFeatureBootstrapHolder {

	@Autowired
	private ThunderspiresConfiguredFeatureBootstrap parentFeature;

	@Autowired
	private PlacementModUtil placementModUtil;

	@Override
	public String name() {
		return "thunderspire";
	}

	@Override
	public PlacedFeature make(BootstrapContext<PlacedFeature> context) {
		return new PlacedFeature(
			parentFeature.get().orElseThrow(),
			List.of(
				InSquarePlacement.spread(),
				new SeekDownPlacementMod(false)
			)
		);
	}
}
