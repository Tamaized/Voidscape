package tamaized.voidscape.registry;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilderBaseConfiguration;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Random;

public class ModSurfaceBuilders {

	private static final DeferredRegister<SurfaceBuilder<?>> REGISTRY = RegUtil.create(ForgeRegistries.SURFACE_BUILDERS);

	public static final RegistryObject<SurfaceBuilder<SurfaceBuilderBaseConfiguration>> EXACT = REGISTRY.register("exact", () -> new SurfaceBuilder<>(SurfaceBuilderBaseConfiguration.CODEC) {
		@Override
		public void apply(Random random, ChunkAccess chunkIn, Biome biomeIn, int x, int z, int y, double noise, BlockState defaultBlock, BlockState defaultFluid, int seaLevel, int minSurfaceLevel, long seed, SurfaceBuilderBaseConfiguration config) {
			BlockPos pos = new BlockPos(x & 15, y, z & 15);
			if (chunkIn.getBlockState(pos).is(defaultBlock.getBlock())) {
				chunkIn.setBlockState(pos, chunkIn.getBlockState(pos.above()).is(Blocks.AIR) ? config.getTopMaterial() : config.getUnderMaterial(), false);
			}

		}
	});

	public static void classload() {

	}

}
