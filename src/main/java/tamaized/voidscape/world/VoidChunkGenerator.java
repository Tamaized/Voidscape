package tamaized.voidscape.world;


import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.CrashReport;
import net.minecraft.ReportedException;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import tamaized.voidscape.Voidscape;

import java.util.Arrays;
import java.util.function.Supplier;

public class VoidChunkGenerator extends NoiseBasedChunkGenerator {

	public static final Codec<VoidChunkGenerator> codec = RecordCodecBuilder.create((p_236091_0_) -> p_236091_0_.
			group(BiomeSource.CODEC.
							fieldOf("biome_source").
							forGetter(ChunkGenerator::getBiomeSource),

					Codec.LONG.
							fieldOf("seed").orElseGet(() -> HackyWorldGen.seed).
							forGetter(gen -> gen.seed),

					NoiseGeneratorSettings.CODEC.
							fieldOf("settings").
							forGetter(VoidChunkGenerator::getDimensionSettings)).
			apply(p_236091_0_, p_236091_0_.stable(VoidChunkGenerator::new)));

	private long seed;

	private VoidChunkGenerator(BiomeSource biomeProvider1, long seed, Supplier<NoiseGeneratorSettings> dimensionSettings) {
		super(biomeProvider1, seed, dimensionSettings);
		this.seed = seed;
		int horizontalNoiseGranularity = dimensionSettings.get().noiseSettings().noiseSizeHorizontal() * 2;
		ObfuscationReflectionHelper.setPrivateValue(NoiseBasedChunkGenerator.class, this, horizontalNoiseGranularity, "field_222564_k");
		int noise = 16 / horizontalNoiseGranularity;
		ObfuscationReflectionHelper.setPrivateValue(NoiseBasedChunkGenerator.class, this, noise, "field_222565_l");
		ObfuscationReflectionHelper.setPrivateValue(NoiseBasedChunkGenerator.class, this, noise, "field_222567_n");
	}

	@Override
	protected Codec<? extends ChunkGenerator> codec() {
		return codec;
	}

	@Override
	public ChunkGenerator withSeed(long seed) {
		return new VoidChunkGenerator(biomeSource.withSeed(seed), seed, getDimensionSettings());
	}

	private Supplier<NoiseGeneratorSettings> getDimensionSettings() {
		return settings;
	}

	@Override
	public int getGenDepth() {
		return 0;
	}

	@Override
	public void buildSurfaceAndBedrock(WorldGenRegion genRegion, ChunkAccess chunk) {
		ChunkPos chunkpos = chunk.getPos();
		WorldgenRandom sharedseedrandom = new WorldgenRandom();
		sharedseedrandom.setBaseChunkSeed(chunkpos.x, chunkpos.z);
		final int xChunkBase = chunkpos.getMinBlockX();
		final int zChunkBase = chunkpos.getMinBlockZ();
		double d0 = 0.0625D;
		BlockPos.MutableBlockPos blockpos$mutable = new BlockPos.MutableBlockPos();
		for (int xRelative = 0; xRelative < 16; ++xRelative) {
			for (int zRelative = 0; zRelative < 16; ++zRelative) {
				int xReal = xChunkBase + xRelative;
				int zReal = zChunkBase + zRelative;
				double noise = this.surfaceNoise.getSurfaceNoiseValue((double) xReal * d0, (double) zReal * d0, d0, (double) xRelative * d0) * 15.0D;
				for (int y = chunk.getHeight(Heightmap.Types.WORLD_SURFACE_WG, xRelative, zRelative); y > 0; y--)
					genRegion.getBiome(blockpos$mutable.set(xReal, y, zReal)).
							buildSurfaceAt(sharedseedrandom, chunk, xReal, zReal, y, noise, this.defaultBlock, this.defaultFluid, this.getSeaLevel(), settings.get().getMinSurfaceLevel(), genRegion.getSeed());
			}
		}

		this.setBedrock(chunk, sharedseedrandom);
	}

	@Override
	public void applyBiomeDecoration(WorldGenRegion worldGenRegion_, StructureFeatureManager structureManager_) {
		int centerX = worldGenRegion_.getCenter().x;
		int centerZ = worldGenRegion_.getCenter().z;
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
			WorldgenRandom rand = new WorldgenRandom();
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
