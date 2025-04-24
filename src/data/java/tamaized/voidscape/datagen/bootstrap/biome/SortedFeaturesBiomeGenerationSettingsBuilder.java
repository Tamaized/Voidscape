package tamaized.voidscape.datagen.bootstrap.biome;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import tamaized.beanification.Autowired;
import tamaized.beanification.Configurable;
import tamaized.voidscape.datagen.util.CachedBootstrapHolderGetter;

import java.util.List;

@Configurable
public class SortedFeaturesBiomeGenerationSettingsBuilder extends BiomeGenerationSettings.PlainBuilder {

	@Autowired
	private CachedBootstrapHolderGetter cachedBootstrapHolderGetter;

	private final HolderGetter<PlacedFeature> registry;

	public SortedFeaturesBiomeGenerationSettingsBuilder(BootstrapContext<Biome> context) {
		registry = context.lookup(Registries.PLACED_FEATURE);
	}

	@Override
	public BiomeGenerationSettings build() {
		features.forEach(list -> {
			List<Holder<PlacedFeature>> result = cachedBootstrapHolderGetter.retrieve(Registries.PLACED_FEATURE, registry).stream()
				.filter(h -> list.stream().anyMatch(l -> l.unwrapKey().orElseThrow().equals(h)))
				.map(k -> list.stream().filter(h -> k.equals(h.getKey())).findFirst().orElseThrow())
				.toList();
			list.clear();
			list.addAll(result);
		});

		return super.build();
	}
}
