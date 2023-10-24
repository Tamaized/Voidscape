package tamaized.voidscape;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = Voidscape.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {

	public static Client CLIENT_CONFIG;
	public static Common COMMON_CONFIG;

	public static class Client {

		public Donator DONATOR;

		public Client(ForgeConfigSpec.Builder builder) {
			builder.push("donator");
			DONATOR = new Donator(builder);
			builder.pop();
		}

		public static class Donator {

			public ForgeConfigSpec.BooleanValue enabled;
			public ForgeConfigSpec.IntValue color;
			public boolean dirty = true;

			private Donator(ForgeConfigSpec.Builder builder) {
				enabled = builder.
						translation(translation("enabled")).
						comment("Enables the Donator perk wing render").
						define("enabled", false);
				color = builder.
						translation(translation("color")).
						comment("Changes the Donator Wing Render Color, Format: RRGGBB Hex").
						defineInRange("color", 0x7700FF, Integer.MIN_VALUE, Integer.MAX_VALUE);
			}

		}

	}

	public static class Common {

		public ForgeConfigSpec.ConfigValue<List<? extends String>> bedrockTeleportationDimensionBlacklist;

		public Common(ForgeConfigSpec.Builder builder) {
			bedrockTeleportationDimensionBlacklist = builder.
					translation("config." + Voidscape.MODID + ".bedrock_teleportation_dimension_blacklist").
					comment("""
								Prevent standing on bedrock at low Y levels from teleporting you to the void from these dimensions
								Example: minecraft:overworld
								""").
					defineList("bedrockTeleportationDimensionBlacklist", new ArrayList<>(), s -> s instanceof String);
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
