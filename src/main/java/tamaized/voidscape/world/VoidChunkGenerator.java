package tamaized.voidscape.world;


import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.DimensionSettings;
import net.minecraft.world.gen.NoiseChunkGenerator;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

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
}
