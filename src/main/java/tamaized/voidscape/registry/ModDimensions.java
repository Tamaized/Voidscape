package tamaized.voidscape.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import tamaized.beanification.Component;
import tamaized.voidscape.Voidscape;

@Component
public class ModDimensions {

	public final ResourceKey<Level> WORLD_KEY_VOID = ResourceKey.create(Registries.DIMENSION, ResourceLocation.fromNamespaceAndPath(Voidscape.MODID, "void"));

}
