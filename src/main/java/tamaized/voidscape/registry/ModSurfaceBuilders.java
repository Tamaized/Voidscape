package tamaized.voidscape.registry;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Random;

public class ModSurfaceBuilders {

	private static final DeferredRegister<SurfaceBuilder<?>> REGISTRY = RegUtil.create(ForgeRegistries.SURFACE_BUILDERS);

	public static final RegistryObject<SurfaceBuilder<SurfaceBuilderConfig>> EXACT = REGISTRY.register("exact", () -> new SurfaceBuilder<SurfaceBuilderConfig>(SurfaceBuilderConfig.CODEC) {
		@Override
		public void apply(Random random, IChunk chunkIn, Biome biomeIn, int x, int z, int y, double noise, BlockState defaultBlock, BlockState defaultFluid, int seaLevel, long seed, SurfaceBuilderConfig config) {
			BlockPos pos = new BlockPos(x & 15, y, z & 15);
			if (chunkIn.getBlockState(pos).is(defaultBlock.getBlock())) {
				chunkIn.setBlockState(pos, chunkIn.getBlockState(pos.above()).is(Blocks.AIR) ? config.getTopMaterial() : config.getUnderMaterial(), false);
			}

		}
	});

	public static void classload() {

	}

}
