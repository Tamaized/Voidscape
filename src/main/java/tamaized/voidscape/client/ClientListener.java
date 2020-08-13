package tamaized.voidscape.client;


import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.lwjgl.glfw.GLFW;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.turmoil.SubCapability;

import java.util.Locale;
import java.util.function.Consumer;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Voidscape.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientListener {

	public static final KeyBinding KEY = new KeyBinding(

			"Summon forth your inner turmoil",

			KeyConflictContext.IN_GAME,

			InputMappings.Type.KEYSYM,

			GLFW.GLFW_KEY_Z,

			Voidscape.MODID.substring(0, 1).toUpperCase(Locale.US).concat(Voidscape.MODID.substring(1))

	);

	static {
		MinecraftForge.EVENT_BUS.addListener((Consumer<TickEvent.ClientTickEvent>) event -> {
			if (event.phase == TickEvent.Phase.START)
				return;
			if (KEY.isPressed() && Minecraft.getInstance().player != null && Minecraft.getInstance().world != null)
				Minecraft.getInstance().player.getCapability(SubCapability.CAPABILITY).ifPresent(cap -> cap.get(Voidscape.subCapTurmoilData).ifPresent(data -> data.action()));

		});
	}

	@SubscribeEvent
	public static void setup(FMLClientSetupEvent event) {
		ClientRegistry.registerKeyBinding(KEY);
	}

}
