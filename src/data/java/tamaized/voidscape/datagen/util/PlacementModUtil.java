package tamaized.voidscape.datagen.util;

import net.minecraft.core.Vec3i;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.placement.BlockPredicateFilter;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import tamaized.beanification.Component;

@Component
public class PlacementModUtil {

	public PlacementModifier AIR_ABOVE = BlockPredicateFilter.forPredicate(
		BlockPredicate.matchesBlocks(
			new Vec3i(0, 1, 0),
			Blocks.AIR
		)
	);

	public PlacementModifier NOT_AIR_BELOW = BlockPredicateFilter.forPredicate(
		BlockPredicate.not(BlockPredicate.matchesBlocks(
			new Vec3i(0, -1, 0),
			Blocks.AIR
		)
	));

}
