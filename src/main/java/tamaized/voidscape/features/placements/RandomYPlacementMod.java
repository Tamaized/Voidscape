package tamaized.voidscape.features.placements;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import tamaized.beanification.Autowired;
import tamaized.voidscape.registry.feature.ModFeaturePlacements;

import java.util.stream.Stream;

public class RandomYPlacementMod extends PlacementModifier {

	public static final MapCodec<RandomYPlacementMod> CODEC = RecordCodecBuilder.mapCodec(
		schema -> schema.group(
			Codec.INT.fieldOf("y").orElse(0).forGetter(c -> c.y)
		).apply(schema, RandomYPlacementMod::new)
	);

	@Autowired
	private static ModFeaturePlacements featurePlacements;

	private final int y;

	public RandomYPlacementMod(int y) {
		this.y = y;
	}

	@Override
	public Stream<BlockPos> getPositions(PlacementContext context, RandomSource random, BlockPos pos) {
		return Stream.of(pos.above(random.nextInt(y) - y / 2));
	}

	@Override
	public PlacementModifierType<?> type() {
		return featurePlacements.RANDOM_Y.get();
	}

}