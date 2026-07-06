package tamaized.voidscape.features.placements;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import tamaized.beanification.Autowired;
import tamaized.voidscape.registry.feature.ModFeaturePlacements;

import java.util.stream.Stream;

public class UnderBlockPlacementMod extends PlacementModifier {

	public static final MapCodec<UnderBlockPlacementMod> CODEC = RecordCodecBuilder.mapCodec(
		schema -> schema.group(
			BlockState.CODEC.fieldOf("state").forGetter(c -> c.state)
		).apply(schema, UnderBlockPlacementMod::new)
	);

	@Autowired
	private static ModFeaturePlacements featurePlacements;

	private final BlockState state;

	public UnderBlockPlacementMod(BlockState state) {
		this.state = state;
	}

	@Override
	public Stream<BlockPos> getPositions(PlacementContext context, RandomSource random, BlockPos pos) {
		for (int i = 1; i < 14; i++) {
			BlockPos check = pos.above(i);
			if (check.getY() > context.getLevel().getMaxY())
				break;
			if (context.getBlockState(check).is(state.getBlock()))
				return Stream.of(pos);
		}
		return Stream.empty();
	}

	@Override
	public PlacementModifierType<?> type() {
		return featurePlacements.UNDER_BLOCK.get();
	}

}