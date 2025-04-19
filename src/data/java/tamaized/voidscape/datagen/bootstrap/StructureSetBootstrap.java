package tamaized.voidscape.datagen.bootstrap;

import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.beanification.Directory;
import tamaized.voidscape.datagen.bootstrap.structureset.StructureSetBootstrapHolder;

import java.util.List;

@Component
public class StructureSetBootstrap implements IBootstrap {

	@Directory(StructureSetBootstrapHolder.class)
	private List<StructureSetBootstrapHolder> structureSets;

	@Autowired
	private StructureBootstrap structureBootstrap;

	@Override
	public int priority() {
		return structureBootstrap.priority() + 1;
	}

	public RegistrySetBuilder bootstrap(RegistrySetBuilder builder) {
		return builder.add(Registries.STRUCTURE_SET, context -> structureSets.forEach(structureSet -> structureSet.getOrMake(context)));
	}

}
