package tamaized.voidscape.config.client;

import net.minecraft.client.Minecraft;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.network.PacketDistributor;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.beanification.PostConstruct;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.config.ConfigUtil;
import tamaized.voidscape.network.DonatorHandler;
import tamaized.voidscape.network.server.ServerPacketDonatorSettings;

@Component
public class DonatorSettings {

	@Autowired("donatorSettings")
	private ConfigUtil configUtil;

	@Autowired
	private DonatorHandler donatorHandler;

	public ModConfigSpec.BooleanValue enable;
	public ModConfigSpec.IntValue color;

	private boolean dirty;

	@PostConstruct
	private void postConstruct(IEventBus modBus, IEventBus forgeBus) {
		modBus.addListener(ModConfigEvent.Reloading.class, event -> {
			if (event.getConfig().getType() == ModConfig.Type.CLIENT && event.getConfig().getModId().equals(Voidscape.MODID)) {
				markDirty();
			}
		});
		forgeBus.addListener(ClientTickEvent.Pre.class, event -> {
			if (Minecraft.getInstance().level == null || Minecraft.getInstance().player == null)
				markDirty();
			else if (isDirty()) {
				unmarkDirty();
				if (donatorHandler.isDonator(Minecraft.getInstance().player.getUUID()))
					PacketDistributor.sendToServer(new ServerPacketDonatorSettings(new DonatorHandler.Settings(enable.get(), color.get())));
			}
		});
	}

	private boolean isDirty() {
		return dirty;
	}

	private void markDirty() {
		dirty = true;
	}

	private void unmarkDirty() {
		dirty = false;
	}

	void setup(ModConfigSpec.Builder builder) {
		builder.comment("Settings for players who have donated or contributed to the Mod's development").push("donatorSettings");
		{
			enable = builder
				.translation(configUtil.translationKey("enable"))
				.comment("Enables the Donator perk wing render")
				.define("enable", true);

			color = builder
				.translation(configUtil.translationKey("color"))
				.comment("Changes the Donator Wing Render Color, Format: RRGGBB Hex")
				.defineInRange("color", 0xFFA4EA, Integer.MIN_VALUE, Integer.MAX_VALUE);

		}
		builder.pop();
	}

}
