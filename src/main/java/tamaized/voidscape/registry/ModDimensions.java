package tamaized.voidscape.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;
import tamaized.beanification.Component;
import tamaized.voidscape.Voidscape;

@Component
public class ModDimensions {

	public final ResourceKey<Level> VOID = ResourceKey.create(Registries.DIMENSION, Identifier.fromNamespaceAndPath(Voidscape.MODID, "void"));

}
