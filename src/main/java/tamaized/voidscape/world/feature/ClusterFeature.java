package tamaized.voidscape.world.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import tamaized.voidscape.world.feature.config.ClusterConfig;

public class ClusterFeature extends Feature<ClusterConfig> {

    public ClusterFeature(Codec<ClusterConfig> pCodec) {
        super(pCodec);
    }

    @Override
    public boolean place(FeaturePlaceContext<ClusterConfig> context) {
		int count = 0;
        boolean flag = false;
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    BlockPos pos = context.origin().offset(x, y, z);
                    if (context.random().nextFloat() <= context.config().chance && place(context.level(), pos, context.config().provider.getState(context.random(), pos), context.config().predicate)) {
						flag = true;
						count++;
						if (count >= context.config().max)
							return true;
					}
                }
            }
        }
        return flag;
    }

    private boolean place(WorldGenLevel level, BlockPos pos, BlockState state, BlockPredicate predicate) {
        if (predicate.test(level, pos) && state.canSurvive(level, pos)) {
            if (state.getBlock() instanceof DoublePlantBlock) {
                if (!level.isEmptyBlock(pos.above())) {
                    return false;
                }
                DoublePlantBlock.placeAt(level, state, pos, 2);
            } else {
                level.setBlock(pos, state, 2);
            }
            return true;
        } else {
            return false;
        }
    }

}
