package tamaized.voidscape.datagen.bootstrap.feature.placed;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.features.NetherFeatures;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.util.PlacementModUtil;
import tamaized.voidscape.features.placements.SeekDownPlacementMod;

import java.util.List;

@Component
public class CrimsonRootsPlacedFeatureBootstrap extends PlacedFeatureBootstrapHolder {

	@Autowired
	private PlacementModUtil placementModUtil;

	@Override
	public String name() {
		return "crimson_roots";
	}

	@Override
	public PlacedFeature make(BootstrapContext<PlacedFeature> context) {
		return new PlacedFeature(
			context.lookup(Registries.CONFIGURED_FEATURE).getOrThrow(
				NetherFeatures.PATCH_CRIMSON_ROOTS
			),
			List.of(
				new SeekDownPlacementMod(true),
				placementModUtil.AIR_ABOVE
			)
		);
	}
}
