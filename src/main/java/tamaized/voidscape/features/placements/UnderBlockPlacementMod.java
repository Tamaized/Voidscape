package tamaized.voidscape.features.placements;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;

import java.util.function.Supplier;
import java.util.stream.Stream;

public class UnderBlockPlacementMod extends PlacementModifier {

	public static final Codec<UnderBlockPlacementMod> CODEC = RecordCodecBuilder.create((p_242803_0_) -> p_242803_0_.group(
		BlockState.CODEC.fieldOf("state").forGetter(c -> c.state)
	).apply(p_242803_0_, UnderBlockPlacementMod::new));

	public static Supplier<PlacementModifierType<UnderBlockPlacementMod>> TYPE = registerPlacementModifierType("under_block", () -> () -> CODEC);

	private final BlockState state;

	public UnderBlockPlacementMod(BlockState state) {
		this.state = state;
	}

	@Override
	public Stream<BlockPos> getPositions(PlacementContext context, RandomSource random, BlockPos pos) {
		for (int i = 1; i < 14; i++) {
			BlockPos check = pos.above(i);
			if (check.getY() > context.getLevel().getMaxBuildHeight())
				break;
			if (context.getBlockState(check).is(state.getBlock()))
				return Stream.of(pos);
		}
		return Stream.empty();
	}

	@Override
	public PlacementModifierType<?> type() {
		return TYPE.get();
	}

}