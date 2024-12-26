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

public class NotAirBelowPlacementMod extends PlacementModifier {

	public static final MapCodec<NotAirBelowPlacementMod> CODEC = MapCodec.unit(NotAirBelowPlacementMod::new);

	@Autowired
	private static ModFeaturePlacements featurePlacements;

	public NotAirBelowPlacementMod() {
	}

	@Override
	public Stream<BlockPos> getPositions(PlacementContext context, RandomSource random, BlockPos pos) {
		return context.getBlockState(pos.below()).isAir() ? Stream.empty() : Stream.of(pos);
	}

	@Override
	public PlacementModifierType<?> type() {
		return featurePlacements.NOT_AIR_BELOW.get();
	}

}