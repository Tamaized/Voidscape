package tamaized.voidscape.client;

import net.minecraft.resources.Identifier;
import tamaized.beanification.Component;
import tamaized.voidscape.Voidscape;

@Component
public class ItemModelOverridePredicates {

	public final Identifier BROKEN = Identifier.fromNamespaceAndPath(Voidscape.MODID, "broken");

	public final Identifier PULL = Identifier.fromNamespaceAndPath(Voidscape.MODID, "pull");

	public final Identifier PULLING = Identifier.fromNamespaceAndPath(Voidscape.MODID, "pulling");

	public final Identifier CHARGED = Identifier.fromNamespaceAndPath(Voidscape.MODID, "charged");

	public final Identifier FIREWORK = Identifier.fromNamespaceAndPath(Voidscape.MODID, "firework");

	public final Identifier BLOCKING = Identifier.fromNamespaceAndPath(Voidscape.MODID, "blocking");

}
