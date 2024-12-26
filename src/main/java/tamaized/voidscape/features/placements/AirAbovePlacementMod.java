package tamaized.voidscape.features.placements;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import tamaized.beanification.Autowired;
import tamaized.voidscape.registry.feature.ModFeaturePlacements;

import java.util.stream.Stream;

public class AirAbovePlacementMod extends PlacementModifier {

	public static final MapCodec<AirAbovePlacementMod> CODEC = MapCodec.unit(AirAbovePlacementMod::new);

	@Autowired
	private static ModFeaturePlacements featurePlacements;

	public AirAbovePlacementMod() {
	}

	@Override
	public Stream<BlockPos> getPositions(PlacementContext context, RandomSource random, BlockPos pos) {
		return context.getBlockState(pos.above()).isAir() ? Stream.of(pos) : Stream.empty();
	}

	@Override
	public PlacementModifierType<?> type() {
		return featurePlacements.AIR_ABOVE.get();
	}

}