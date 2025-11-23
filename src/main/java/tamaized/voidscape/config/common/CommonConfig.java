package tamaized.voidscape.config.common;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.beanification.PostConstruct;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.config.ConfigUtil;

import java.util.ArrayList;
import java.util.List;

@Component
public class CommonConfig {

	@Autowired("common")
	private ConfigUtil configUtil;

	public ModConfigSpec.ConfigValue<List<? extends String>> bedrockTeleportationDimensionBlacklist;
	public ModConfigSpec.BooleanValue bedrockTeleportationDimensionWhitelist;

	@PostConstruct
	private void postConstruct(IEventBus modBus) {
		ModConfigSpec spec = new ModConfigSpec.Builder().configure(this::setup).getRight();
		ModLoadingContext.get().getActiveContainer().registerConfig(ModConfig.Type.COMMON, spec);
	}

	private CommonConfig setup(ModConfigSpec.Builder builder) {
		bedrockTeleportationDimensionBlacklist = builder
			.translation(configUtil.translationKey("bedrock_teleportation_dimension_blacklist"))
			.comment("""
				Prevent standing on bedrock at low Y levels from teleporting you to the void from these dimensions
				Example: minecraft:overworld""")
			.defineListAllowEmpty("bedrockTeleportationDimensionBlacklist", new ArrayList<>(), () -> "", s -> s instanceof String);

		bedrockTeleportationDimensionWhitelist = builder
			.translation(configUtil.translationKey("bedrockTeleportationDimensionWhitelist"))
			.comment("Changes the bedrock teleportation dimension blacklist config to be a whitelist instead")
			.define("bedrockTeleportationDimensionWhitelist", false);

		return this;
	}

}
