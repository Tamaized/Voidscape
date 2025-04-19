package tamaized.voidscape.datagen.bootstrap.feature.placed;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.features.VegetationFeatures;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.util.PlacementModUtil;
import tamaized.voidscape.features.placements.SeekDownPlacementMod;
import tamaized.voidscape.features.placements.UnderBlockPlacementMod;

import java.util.List;

@Component
public class FlowerCherryPlacedFeatureBootstrap extends PlacedFeatureBootstrapHolder {

	@Autowired
	private PlacementModUtil placementModUtil;

	@Override
	public String name() {
		return "flower_cherry";
	}

	@Override
	public PlacedFeature make(BootstrapContext<PlacedFeature> context) {
		return new PlacedFeature(
			context.lookup(Registries.CONFIGURED_FEATURE).getOrThrow(
				VegetationFeatures.FLOWER_CHERRY
			),
			List.of(
				CountPlacement.of(12),
				new SeekDownPlacementMod(true),
				new UnderBlockPlacementMod(Blocks.CHERRY_LEAVES.defaultBlockState())
			)
		);
	}
}
