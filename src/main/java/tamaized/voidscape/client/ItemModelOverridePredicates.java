package tamaized.voidscape.client;

import net.minecraft.resources.ResourceLocation;
import tamaized.beanification.Component;
import tamaized.voidscape.Voidscape;

@Component
public class ItemModelOverridePredicates {

	public final ResourceLocation BROKEN = ResourceLocation.fromNamespaceAndPath(Voidscape.MODID, "broken");

	public final ResourceLocation PULL = ResourceLocation.fromNamespaceAndPath(Voidscape.MODID, "pull");

	public final ResourceLocation PULLING = ResourceLocation.fromNamespaceAndPath(Voidscape.MODID, "pulling");

	public final ResourceLocation CHARGED = ResourceLocation.fromNamespaceAndPath(Voidscape.MODID, "charged");

	public final ResourceLocation FIREWORK = ResourceLocation.fromNamespaceAndPath(Voidscape.MODID, "firework");

	public final ResourceLocation BLOCKING = ResourceLocation.fromNamespaceAndPath(Voidscape.MODID, "blocking");

}
