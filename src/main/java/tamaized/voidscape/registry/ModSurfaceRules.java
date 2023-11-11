package tamaized.voidscape.registry;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryObject;
import tamaized.regutil.RegUtil;
import tamaized.regutil.RegistryClass;

public class ModSurfaceRules implements RegistryClass {

	private static final DeferredRegister<Codec<? extends SurfaceRules.ConditionSource>> REGISTRY = RegUtil.create(Registries.MATERIAL_CONDITION);

	private static final RegistryObject<Codec<AirAboveConditionSource>> AIR_ABOVE = REGISTRY.register("air_above", AirAboveConditionSource.CODEC::codec);

	static class AirAboveConditionSource implements SurfaceRules.ConditionSource {

		private static final KeyDispatchDataCodec<AirAboveConditionSource> CODEC = KeyDispatchDataCodec.of(Codec.unit(AirAboveConditionSource::new));

		@Override
		public KeyDispatchDataCodec<? extends SurfaceRules.ConditionSource> codec() {
			return CODEC;
		}

		@Override
		public SurfaceRules.Condition apply(SurfaceRules.Context context) {
			return () -> context.blockY < (context.context.getMinGenY() + context.context.getGenDepth()) &&

					context.chunk.getBlockState(new BlockPos(context.blockX, context.blockY + 1, context.blockZ)).isAir();
		}
	}

	@Override
	@SuppressWarnings("ResultOfMethodCallIgnored")
	public void init(IEventBus bus) {
		AirAboveConditionSource.CODEC.getClass();
	}

}
