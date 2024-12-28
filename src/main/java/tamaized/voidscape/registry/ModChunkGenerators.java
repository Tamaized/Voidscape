package tamaized.voidscape.registry;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.neoforged.neoforge.registries.DeferredRegister;
import tamaized.beanification.Component;
import tamaized.regutil.RegUtil;
import tamaized.voidscape.dimension.VoidChunkGenerator;

import java.util.function.Supplier;

@Component
public class ModChunkGenerators {

	private final DeferredRegister<MapCodec<? extends ChunkGenerator>> REGISTRY = RegUtil.create(Registries.CHUNK_GENERATOR);

	public final Supplier<MapCodec<VoidChunkGenerator>> VOID = REGISTRY.register("void", () -> VoidChunkGenerator.CODEC);

}
