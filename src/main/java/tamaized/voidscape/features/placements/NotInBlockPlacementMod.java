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

public class NotInBlockPlacementMod extends PlacementModifier {

	public static final MapCodec<NotInBlockPlacementMod> CODEC = MapCodec.unit(NotInBlockPlacementMod::new);

	@Autowired
	private static ModFeaturePlacements featurePlacements;

	public NotInBlockPlacementMod() {
	}

	@Override
	public Stream<BlockPos> getPositions(PlacementContext context, RandomSource random, BlockPos pos) {
		return context.getBlockState(pos).isAir() ? Stream.of(pos) : Stream.empty();
	}

	@Override
	public PlacementModifierType<?> type() {
		return featurePlacements.NOT_IN_BLOCK.get();
	}

}