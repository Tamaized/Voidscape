package tamaized.voidscape.datagen.datapack.voidscape_aether_compat.feature.configured;

import com.aetherteam.aether.data.resources.registries.AetherConfiguredFeatures;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.bootstrap.feature.placed.PlacedFeatureBootstrapHolder;
import tamaized.voidscape.datagen.datapack.voidscape_aether_compat.VoidscapeAetherCompatRegistryProvider;
import tamaized.voidscape.datagen.util.DirectReferenceHolder;
import tamaized.voidscape.features.placements.RandomYPlacementMod;

import java.util.List;

@Component
public class ZaniteOreBuriedFeatureBootstrap extends PlacedFeatureBootstrapHolder {

	@Autowired
	private VoidscapeAetherCompatRegistryProvider registryProvider;

	@Override
	public String name() {
		return "zanite_ore";
	}

	@Override
	public PlacedFeature make(BootstrapContext<PlacedFeature> context) {
		return new PlacedFeature(
			registryProvider.createOwnedHolderReference(
				Registries.CONFIGURED_FEATURE,
				AetherConfiguredFeatures.ORE_ZANITE_CONFIGURATION
			),
			List.of(
				CountPlacement.of(2),
				InSquarePlacement.spread(),
				new RandomYPlacementMod(13),
				BiomeFilter.biome()
			)
		);
	}
}
