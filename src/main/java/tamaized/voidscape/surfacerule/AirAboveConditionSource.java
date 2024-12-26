package tamaized.voidscape.surfacerule;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.SurfaceRules;

public class AirAboveConditionSource implements SurfaceRules.ConditionSource {

	public static final KeyDispatchDataCodec<AirAboveConditionSource> CODEC = KeyDispatchDataCodec.of(MapCodec.unit(AirAboveConditionSource::new));

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
