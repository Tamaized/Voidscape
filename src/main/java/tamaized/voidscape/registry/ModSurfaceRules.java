package tamaized.voidscape.registry;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegisterEvent;
import tamaized.regutil.RegistryClass;
import tamaized.voidscape.Voidscape;

import java.util.function.Consumer;

public class ModSurfaceRules implements RegistryClass {

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
	public void init(IEventBus bus) {
		bus.addListener((Consumer<RegisterEvent>) event -> {
			Registry.register(Registry.CONDITION, new ResourceLocation(Voidscape.MODID, "air_above"), AirAboveConditionSource.CODEC.codec());
		});
	}

}
