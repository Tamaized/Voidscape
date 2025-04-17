package tamaized.voidscape.datagen.bootstrap.biome;

import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;

public interface IBiomeBootstrap {

	ResourceKey<Biome> key();

	Biome make(BootstrapContext<Biome> context);

}
