package tamaized.voidscape.config.client;

import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.beanification.PostConstruct;
import tamaized.voidscape.config.ConfigUtil;

@Component
public class ClientConfig {

	@Autowired("client")
	private ConfigUtil configUtil;

	@Autowired
	private DonatorSettings donatorSettings;

	@PostConstruct
	private void postConstruct() {
		ModConfigSpec spec = new ModConfigSpec.Builder().configure(this::setup).getRight();
		ModLoadingContext.get().getActiveContainer().registerConfig(ModConfig.Type.CLIENT, spec);
	}

	private ClientConfig setup(ModConfigSpec.Builder builder) {
		donatorSettings.setup(builder);
		return this;
	}

}
