package tamaized.voidscape.client;


import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import tamaized.voidscape.Voidscape;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Voidscape.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientListener {

	/*public static final KeyBinding KEY = new KeyBinding(

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
			if (KEY.isPressed()) {
				if (Turmoil.STATE == Turmoil.State.CLOSED)
					//Turmoil.STATE = Turmoil.State.OPEN;
					OverlayMessageHandler.queueMessage(new TranslationTextComponent("test").setStyle(new Style().setObfuscated(true)));
				else
					Turmoil.STATE = Turmoil.State.CLOSED;
			}
		});
	}

	@SubscribeEvent
	public static void setup(FMLClientSetupEvent event) {
		ClientRegistry.registerKeyBinding(KEY);
	}*/

}
