package tamaized.voidscape.registry;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.Registries;
import tamaized.beanification.Component;
import tamaized.regutil.RegUtil;
import tamaized.voidscape.dimension.VoidChunkGenerator;

import java.util.function.Supplier;

@Component
public class ModChunkGenerators {

	public final Supplier<MapCodec<VoidChunkGenerator>> VOID = RegUtil.register(Registries.CHUNK_GENERATOR, "void", () -> VoidChunkGenerator.CODEC);

}
