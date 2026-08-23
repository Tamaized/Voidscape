package tamaized.voidscape.datagen.bootstrap;

import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.beanification.Directory;
import tamaized.voidscape.datagen.bootstrap.biome.IBiomeBootstrap;

import java.util.List;

@Component
public class BiomeBootstrap implements IBootstrap {

	@Directory(IBiomeBootstrap.class)
	private List<IBiomeBootstrap> biomes;

	@Autowired
	private PlacedFeatureBootstrap placedFeatureBootstrap;

	@Override
	public int priority() {
		return placedFeatureBootstrap.priority() + 1;
	}

	@Override
	public RegistrySetBuilder bootstrap(RegistrySetBuilder builder) {
		return builder.add(Registries.BIOME, context -> biomes.forEach(biome -> context.register(biome.key(), biome.make(context))));
	}

}
