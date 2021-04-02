package tamaized.voidscape.world;


import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.ReportedException;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.DimensionSettings;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.NoiseChunkGenerator;
import net.minecraft.world.gen.WorldGenRegion;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import tamaized.voidscape.Voidscape;

import java.util.Arrays;
import java.util.function.Supplier;

public class VoidChunkGenerator extends NoiseChunkGenerator {

	public static final Codec<VoidChunkGenerator> codec = RecordCodecBuilder.create((p_236091_0_) -> p_236091_0_.
			group(BiomeProvider.CODEC.
							fieldOf("biome_source").
							forGetter(ChunkGenerator::getBiomeSource),

					Codec.LONG.
							fieldOf("seed").orElseGet(() -> HackyWorldGen.seed).
							forGetter(gen -> gen.seed),

					DimensionSettings.CODEC.
							fieldOf("settings").
							forGetter(VoidChunkGenerator::getDimensionSettings)).
			apply(p_236091_0_, p_236091_0_.stable(VoidChunkGenerator::new)));

	private long seed;

	private VoidChunkGenerator(BiomeProvider biomeProvider1, long seed, Supplier<DimensionSettings> dimensionSettings) {
		super(biomeProvider1, seed, dimensionSettings);
		this.seed = seed;
		int horizontalNoiseGranularity = dimensionSettings.get().noiseSettings().noiseSizeHorizontal() * 2;
		ObfuscationReflectionHelper.setPrivateValue(NoiseChunkGenerator.class, this, horizontalNoiseGranularity, "field_222564_k");
		int noise = 16 / horizontalNoiseGranularity;
		ObfuscationReflectionHelper.setPrivateValue(NoiseChunkGenerator.class, this, noise, "field_222565_l");
		ObfuscationReflectionHelper.setPrivateValue(NoiseChunkGenerator.class, this, noise, "field_222567_n");
	}

	@Override
	protected Codec<? extends ChunkGenerator> codec() {
		return codec;
	}

	@Override
	public ChunkGenerator withSeed(long seed) {
		return new VoidChunkGenerator(biomeSource.withSeed(seed), seed, getDimensionSettings());
	}

	private Supplier<DimensionSettings> getDimensionSettings() {
		return settings;
	}

	@Override
	public int getGenDepth() {
		return 0;
	}

	@Override
	public void buildSurfaceAndBedrock(WorldGenRegion genRegion, IChunk chunk) {
		ChunkPos chunkpos = chunk.getPos();
		SharedSeedRandom sharedseedrandom = new SharedSeedRandom();
		sharedseedrandom.setBaseChunkSeed(chunkpos.x, chunkpos.z);
		final int xChunkBase = chunkpos.getMinBlockX();
		final int zChunkBase = chunkpos.getMinBlockZ();
		double d0 = 0.0625D;
		BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();
		for (int xRelative = 0; xRelative < 16; ++xRelative) {
			for (int zRelative = 0; zRelative < 16; ++zRelative) {
				int xReal = xChunkBase + xRelative;
				int zReal = zChunkBase + zRelative;
				double noise = this.surfaceNoise.getSurfaceNoiseValue((double) xReal * d0, (double) zReal * d0, d0, (double) xRelative * d0) * 15.0D;
				for (int y = chunk.getHeight(Heightmap.Type.WORLD_SURFACE_WG, xRelative, zRelative); y > 0; y--)
					genRegion.getBiome(blockpos$mutable.set(xReal, y, zReal)).
							buildSurfaceAt(sharedseedrandom, chunk, xReal, zReal, y, noise, this.defaultBlock, this.defaultFluid, this.getSeaLevel(), genRegion.getSeed());
			}
		}

		this.setBedrock(chunk, sharedseedrandom);
	}

	@Override
	public void applyBiomeDecoration(WorldGenRegion worldGenRegion_, StructureManager structureManager_) {
		int centerX = worldGenRegion_.getCenterX();
		int centerZ = worldGenRegion_.getCenterZ();
		int x = centerX * 16;
		int z = centerZ * 16;
		int[] yIterator = new int[]{0};
		boolean cast;
		if (cast = biomeSource instanceof VoidscapeSeededBiomeProvider) {
			final int[] layers = Arrays.stream(VoidscapeSeededBiomeProvider.LAYERS).map(i -> i + 3).toArray();
			final int[] result = new int[yIterator.length + layers.length];
			System.arraycopy(yIterator, 0, result, 0, yIterator.length);
			System.arraycopy(layers, 0, result, yIterator.length, layers.length);
			yIterator = result;
		}
		for (int y : yIterator) {
			BlockPos pos = new BlockPos(x, y, z);
			Biome biome = cast ? ((VoidscapeSeededBiomeProvider) biomeSource).
					getRealNoiseBiome((centerX << 2) + 2, y, (centerZ << 2) + 2) : this.biomeSource.
					getNoiseBiome((centerX << 2) + 2, (y >> 2), (centerZ << 2) + 2);
			SharedSeedRandom rand = new SharedSeedRandom();
			long seed = rand.setDecorationSeed(worldGenRegion_.getSeed(), x, z);
			try {
				biome.generate(structureManager_, this, worldGenRegion_, seed, rand, pos);
			} catch (Exception var14) {
				CrashReport lvt_13_1_ = CrashReport.forThrowable(var14, "Biome decoration");
				lvt_13_1_.addCategory("Generation").setDetail("CenterX", centerX).setDetail("CenterZ", centerZ).setDetail("Seed", seed).setDetail("Biome", biome);
				new ReportedException(lvt_13_1_).printStackTrace();
				Voidscape.LOGGER.info("VOIDSCAPE CAUGHT A BIOME DECORATION ERROR, REPORT THIS!");
				Voidscape.LOGGER.info(biome.getRegistryName());
			}
		}
	}

}
