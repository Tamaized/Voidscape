package tamaized.voidscape.datagen.bootstrap;

import net.minecraft.core.RegistrySetBuilder;

public interface IBootstrap {

	default int priority() {
		return 0;
	}

	RegistrySetBuilder bootstrap(RegistrySetBuilder builder);

}
