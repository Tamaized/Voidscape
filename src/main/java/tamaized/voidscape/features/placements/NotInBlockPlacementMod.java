package tamaized.voidscape.features.placements;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;

import java.util.function.Supplier;
import java.util.stream.Stream;

public class NotInBlockPlacementMod extends PlacementModifier {

	public static final Codec<NotInBlockPlacementMod> CODEC = Codec.unit(NotInBlockPlacementMod::new);

	public static Supplier<PlacementModifierType<NotInBlockPlacementMod>> TYPE = registerPlacementModifierType("not_in_block", () -> () -> CODEC);

	public NotInBlockPlacementMod() {
	}

	@Override
	public Stream<BlockPos> getPositions(PlacementContext context, RandomSource random, BlockPos pos) {
		return context.getBlockState(pos).isAir() ? Stream.of(pos) : Stream.empty();
	}

	@Override
	public PlacementModifierType<?> type() {
		return TYPE.get();
	}

}