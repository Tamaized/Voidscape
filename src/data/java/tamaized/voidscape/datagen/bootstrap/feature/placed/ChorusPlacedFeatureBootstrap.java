package tamaized.voidscape.datagen.bootstrap.feature.placed;

import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.bootstrap.feature.configured.ChorusConfiguredFeatureBootstrap;
import tamaized.voidscape.features.placements.SeekDownPlacementMod;

import java.util.List;

@Component
public class ChorusPlacedFeatureBootstrap extends PlacedFeatureBootstrapHolder {

	@Autowired
	private ChorusConfiguredFeatureBootstrap parentFeature;

	@Override
	public String name() {
		return "chorus";
	}

	@Override
	public PlacedFeature make(BootstrapContext<PlacedFeature> context) {
		return new PlacedFeature(
			parentFeature.get().orElseThrow(),
			List.of(
				CountPlacement.of(6),
				InSquarePlacement.spread(),
				new SeekDownPlacementMod(true),
				BiomeFilter.biome()
			)
		);
	}
}
