package tamaized.voidscape.datagen.datapack.voidscape_aether_compat.feature.configured;

import com.aetherteam.aether.data.resources.registries.AetherConfiguredFeatures;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.bootstrap.feature.placed.PlacedFeatureBootstrapHolder;
import tamaized.voidscape.datagen.util.DirectReferenceHolder;
import tamaized.voidscape.datagen.util.PlacementModUtil;
import tamaized.voidscape.features.placements.SeekDownPlacementMod;

import java.util.List;

@Component
public class WhiteFlowerPatchFeatureBootstrap extends PlacedFeatureBootstrapHolder {

	@Autowired
	private PlacementModUtil placementModUtil;

	@Override
	public String name() {
		return "white_flower_patch";
	}

	@Override
	public PlacedFeature make(BootstrapContext<PlacedFeature> context) {
		return new PlacedFeature(
			DirectReferenceHolder.of(AetherConfiguredFeatures.WHITE_FLOWER_PATCH_CONFIGURATION),
			List.of(
				CountPlacement.of(6),
				InSquarePlacement.spread(),
				new SeekDownPlacementMod(true),
				placementModUtil.AIR_ABOVE,
				BiomeFilter.biome()
			)
		);
	}
}
