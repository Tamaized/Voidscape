package tamaized.voidscape.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.loading.FMLEnvironment;
import tamaized.voidscape.Voidscape;

/**
 * Avoid adding to this class, this is strictly for {@link ClientListener} initialization from {@link Voidscape#Voidscape(IEventBus)} via {@link FMLEnvironment#dist} == {@link Dist#CLIENT}
 */
public class ClientInitiator {

	public static void call(IEventBus busMod) {
		ClientListener.init(busMod);
	}

}
