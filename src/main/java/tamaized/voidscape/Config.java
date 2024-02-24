package tamaized.voidscape;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = Voidscape.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {

	public static Client CLIENT_CONFIG;
	public static Common COMMON_CONFIG;

	public static class Client {

		public Donator DONATOR;

		public Client(ModConfigSpec.Builder builder) {
			builder.push("donator");
			DONATOR = new Donator(builder);
			builder.pop();
		}

		public static class Donator {

			public ModConfigSpec.BooleanValue enabled;
			public ModConfigSpec.IntValue color;
			public boolean dirty = true;

			private Donator(ModConfigSpec.Builder builder) {
				enabled = builder.
						translation(translation("enabled")).
						comment("Enables the Donator perk wing render").
						define("enabled", true);
				color = builder.
						translation(translation("color")).
						comment("Changes the Donator Wing Render Color, Format: RRGGBB Hex").
						defineInRange("color", 0x7700FF, Integer.MIN_VALUE, Integer.MAX_VALUE);
			}

		}

	}

	public static class Common {

		public ModConfigSpec.ConfigValue<List<? extends String>> bedrockTeleportationDimensionBlacklist;
		public ModConfigSpec.BooleanValue bedrockTeleportationDimensionWhitelist;

		public Common(ModConfigSpec.Builder builder) {
			bedrockTeleportationDimensionBlacklist = builder.
					translation("config." + Voidscape.MODID + ".bedrock_teleportation_dimension_blacklist").
					comment("""
								Prevent standing on bedrock at low Y levels from teleporting you to the void from these dimensions
								Example: minecraft:overworld
								""").
					defineList("bedrockTeleportationDimensionBlacklist", new ArrayList<>(), s -> s instanceof String);
			bedrockTeleportationDimensionWhitelist = builder.
					translation(translation("bedrockTeleportationDimensionWhitelist")).
					comment("Changes the bedrock teleportation dimension blacklist config to be a whitelist instead").
					define("bedrockTeleportationDimensionWhitelist", false);
		}

	}

	@SubscribeEvent
	public static void onChange(ModConfigEvent.Reloading event) {
		if (event.getConfig().getModId().equals(Voidscape.MODID) && event.getConfig().getType() == ModConfig.Type.CLIENT) {
			CLIENT_CONFIG.DONATOR.dirty = true;
		}
	}

	private static String translation(String key) {
		return Voidscape.MODID + ".config." + key;
	}

}
