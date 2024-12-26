package tamaized.voidscape.features.placements;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import tamaized.beanification.Autowired;
import tamaized.voidscape.registry.feature.ModFeaturePlacements;

import java.util.stream.Stream;

import static com.mojang.serialization.Codec.BOOL;

public class SeekDownPlacementMod extends PlacementModifier {

	public static final MapCodec<SeekDownPlacementMod> CODEC = RecordCodecBuilder.mapCodec(
		schema -> schema.group(
			BOOL.fieldOf("check_below").orElse(false).forGetter(c -> c.check_below)
		).apply(schema, SeekDownPlacementMod::new)
	);

	@Autowired
	private static ModFeaturePlacements featurePlacements;

	private final boolean check_below;

	public SeekDownPlacementMod(boolean val) {
		check_below = val;
	}

	@Override
	public Stream<BlockPos> getPositions(PlacementContext context, RandomSource random, BlockPos pos) {
		final int y = pos.getY();
		BlockPos.MutableBlockPos seek = pos.mutable().move(Direction.UP, random.nextInt(15));
		BlockPos.MutableBlockPos check = seek.mutable().move(Direction.DOWN, 1);
		while ((check_below ? check : seek).getY() > y && seek.getY() > context.getLevel().getMinBuildHeight() && context.getLevel().getBlockState(check_below ? check : seek).isAir()) {
			seek.move(Direction.DOWN, 1);
			check.move(Direction.DOWN, 1);
		}
		return Stream.of(seek);
	}

	@Override
	public PlacementModifierType<?> type() {
		return featurePlacements.SEEK_DOWN.get();
	}

}