package tamaized.voidscape.registry;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.biome.BiomeSource;
import net.neoforged.neoforge.registries.DeferredRegister;
import tamaized.beanification.Component;
import tamaized.regutil.RegUtil;
import tamaized.voidscape.biome.LayeredBiomeProvider;

import java.util.function.Supplier;

@Component
public class ModBiomeSources {

	public final Supplier<MapCodec<LayeredBiomeProvider>> LAYERED = RegUtil.register(Registries.BIOME_SOURCE, "layered", () -> LayeredBiomeProvider.CODEC);

}
