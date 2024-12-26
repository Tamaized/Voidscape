package tamaized.voidscape.features.placements;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;

import java.util.function.Supplier;
import java.util.stream.Stream;

public class RandomYPlacementMod extends PlacementModifier {

	public static final Codec<RandomYPlacementMod> CODEC = RecordCodecBuilder.create((p_242803_0_) -> p_242803_0_.group(Codec.
		INT.fieldOf("y").orElse(0).forGetter(c -> c.y)).apply(p_242803_0_, RandomYPlacementMod::new));

	public static Supplier<PlacementModifierType<RandomYPlacementMod>> TYPE = registerPlacementModifierType("random_y", () -> () -> CODEC);

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
		return TYPE.get();
	}

}