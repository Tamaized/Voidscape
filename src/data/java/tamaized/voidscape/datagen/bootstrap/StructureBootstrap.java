package tamaized.voidscape.datagen.bootstrap;

import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.beanification.Directory;
import tamaized.voidscape.datagen.bootstrap.biome.IBiomeBootstrap;
import tamaized.voidscape.datagen.bootstrap.structure.StructureBootstrapHolder;

import java.util.List;

@Component
public class StructureBootstrap implements IBootstrap {

	@Directory(StructureBootstrapHolder.class)
	private List<StructureBootstrapHolder> structures;

	@Autowired
	private BiomeBootstrap biomeBootstrap;

	@Override
	public int priority() {
		return biomeBootstrap.priority() + 1;
	}

	public RegistrySetBuilder bootstrap(RegistrySetBuilder builder) {
		return builder.add(Registries.STRUCTURE, context -> structures.forEach(structure -> structure.getOrMake(context)));
	}

}
