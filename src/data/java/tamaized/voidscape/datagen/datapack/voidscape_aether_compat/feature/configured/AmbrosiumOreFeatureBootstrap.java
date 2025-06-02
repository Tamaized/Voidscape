package tamaized.voidscape.datagen.datapack.voidscape_aether_compat.feature.configured;

import com.aetherteam.aether.data.resources.registries.AetherConfiguredFeatures;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.bootstrap.feature.placed.PlacedFeatureBootstrapHolder;
import tamaized.voidscape.datagen.util.DirectReferenceHolder;
import tamaized.voidscape.features.placements.RandomYPlacementMod;

import java.util.List;

@Component
public class AmbrosiumOreFeatureBootstrap extends PlacedFeatureBootstrapHolder {

	@Override
	public String name() {
		return "ambrosium_ore";
	}

	@Override
	public PlacedFeature make(BootstrapContext<PlacedFeature> context) {
		return new PlacedFeature(
			DirectReferenceHolder.of(AetherConfiguredFeatures.ORE_AMBROSIUM_CONFIGURATION),
			List.of(
				CountPlacement.of(2),
				InSquarePlacement.spread(),
				new RandomYPlacementMod(13),
				BiomeFilter.biome()
			)
		);
	}
}
