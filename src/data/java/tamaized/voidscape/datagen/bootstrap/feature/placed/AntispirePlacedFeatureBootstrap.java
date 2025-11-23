package tamaized.voidscape.datagen.bootstrap.feature.placed;

import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.bootstrap.feature.configured.AntispireConfiguredFeatureBootstrap;

import java.util.List;

@Component
public class AntispirePlacedFeatureBootstrap extends PlacedFeatureBootstrapHolder {

	@Autowired
	private AntispireConfiguredFeatureBootstrap parentFeature;

	@Override
	public String name() {
		return "antispire";
	}

	@Override
	public PlacedFeature make(BootstrapContext<PlacedFeature> context) {
		return new PlacedFeature(
			parentFeature.get().orElseThrow(),
			List.of(
				InSquarePlacement.spread(),
				BiomeFilter.biome()
			)
		);
	}
}
