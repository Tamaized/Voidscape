package tamaized.voidscape.datagen.datapack.voidscape_aether_compat;

import net.minecraft.core.RegistrySetBuilder;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.bootstrap.IBootstrap;
import tamaized.voidscape.datagen.bootstrap.NoiseGeneratorSettingsBootstrap;

@Component
public class VoidscapeAetherCompatNoiseGeneratorSettingsBootstrap implements IBootstrap {

	@Autowired
	private NoiseGeneratorSettingsBootstrap noiseGeneratorSettingsBootstrap;

	@Override
	public RegistrySetBuilder bootstrap(RegistrySetBuilder builder) {
		return noiseGeneratorSettingsBootstrap.bootstrap(builder, true);
	}

}
