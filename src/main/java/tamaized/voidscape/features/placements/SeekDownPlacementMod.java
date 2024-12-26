package tamaized.voidscape.features.placements;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;

import java.util.function.Supplier;
import java.util.stream.Stream;

public class SeekDownPlacementMod extends PlacementModifier {

	public static final Codec<SeekDownPlacementMod> CODEC = RecordCodecBuilder.create((p_242803_0_) -> p_242803_0_.group(Codec.
		BOOL.fieldOf("check_below").orElse(false).forGetter(c -> c.check_below)).apply(p_242803_0_, SeekDownPlacementMod::new));

	public static Supplier<PlacementModifierType<SeekDownPlacementMod>> TYPE = registerPlacementModifierType("seek", () -> () -> CODEC);

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
		return TYPE.get();
	}

}