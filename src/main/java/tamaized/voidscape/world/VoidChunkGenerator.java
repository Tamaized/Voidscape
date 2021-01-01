package tamaized.voidscape.world;


import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.DimensionSettings;
import net.minecraft.world.gen.NoiseChunkGenerator;
import net.minecraft.world.gen.WorldGenRegion;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import tamaized.voidscape.Voidscape;

import java.util.Random;
import java.util.function.Supplier;

public class VoidChunkGenerator extends NoiseChunkGenerator {

	public static final Codec<VoidChunkGenerator> codec = RecordCodecBuilder.create((p_236091_0_) -> p_236091_0_.
			group(BiomeProvider.CODEC.
							fieldOf("biome_source").
							forGetter(ChunkGenerator::getBiomeSource),

					Codec.LONG.
							fieldOf("seed").orElse(new Random().nextLong()).
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
	public void applyBiomeDecoration(WorldGenRegion worldGenRegion_, StructureManager structureManager_) {
		try {
			super.applyBiomeDecoration(worldGenRegion_, structureManager_);
		} catch (Throwable e) {
			Voidscape.LOGGER.info("VOIDSCAPE CAUGHT A BIOME DECORATION ERROR, REPORT THIS!");
			int i = worldGenRegion_.getCenterX();
			int j = worldGenRegion_.getCenterZ();
			Biome biome = this.biomeSource.getNoiseBiome((i << 2) + 2, 2, (j << 2) + 2);
			Voidscape.LOGGER.info(biome.getRegistryName());
			e.printStackTrace();
		}
	}

}
