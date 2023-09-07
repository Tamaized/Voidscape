package tamaized.voidscape.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import tamaized.voidscape.Voidscape;

import java.util.function.Supplier;

/**
 * Avoid adding to this class, this is strictly for {@link ClientListener} initiliazation from {@link Voidscape#Voidscape()} via {@link DistExecutor#safeRunWhenOn(Dist, Supplier)}
 */
public class ClientInitiator {

	public static void call() {
		ClientListener.init();
	}

}
