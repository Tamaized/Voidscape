package tamaized.voidscape.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import tamaized.beanification.Autowired;
import tamaized.beanification.Configurable;
import tamaized.voidscape.registry.feature.ModConfiguredFeatures;

@Configurable
public class ThunderNyliumBlock extends Block implements BonemealableBlock {

	@Autowired
	private ModConfiguredFeatures configuredFeatures;

    public ThunderNyliumBlock(Properties pProperties) {
        super(pProperties);
    }

	@Override
    public boolean isValidBonemealTarget(LevelReader pLevel, BlockPos pPos, BlockState pState) {
        return pLevel.getBlockState(pPos.above()).isAir();
    }

	@Override
	public boolean isBonemealSuccess(Level pLevel, RandomSource pRandom, BlockPos pPos, BlockState pState) {
        return true;
    }

	@Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        BlockPos blockpos = pos.above();
        ChunkGenerator chunkgenerator = level.getChunkSource().getGenerator();
        this.place(level.registryAccess(), configuredFeatures.THUNDER_FOREST_VEGETATION_BONEMEAL, level, chunkgenerator, random, blockpos);

    }

    private void place(RegistryAccess registryAccess, ResourceKey<ConfiguredFeature<?, ?>> pFeatureKey, ServerLevel pLevel, ChunkGenerator pChunkGenerator, RandomSource pRandom, BlockPos pPos) {
		registryAccess.get(pFeatureKey).ifPresent((p_255920_) -> {
            p_255920_.value().place(pLevel, pChunkGenerator, pRandom, pPos);
        });
    }

}
