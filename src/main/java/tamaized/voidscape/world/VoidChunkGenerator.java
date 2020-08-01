package tamaized.voidscape.world;


import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.DimensionSettings;
import net.minecraft.world.gen.NoiseChunkGenerator;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.util.Random;

public class VoidChunkGenerator extends NoiseChunkGenerator {

	public static final Codec<VoidChunkGenerator> codec = RecordCodecBuilder.create((p_236091_0_) -> p_236091_0_.
			group(BiomeProvider.field_235202_a_.
					fieldOf("biome_source").
					forGetter(ChunkGenerator::getBiomeProvider), DimensionSettings.field_236098_b_.fieldOf("settings").
					forGetter(VoidChunkGenerator::getDimensionSettings)).
			apply(p_236091_0_, p_236091_0_.stable(VoidChunkGenerator::new)));

	private VoidChunkGenerator(BiomeProvider biomeProvider1, long seed, DimensionSettings dimensionSettings) {
		super(biomeProvider1, seed, dimensionSettings);
		int horizontalNoiseGranularity = dimensionSettings.func_236113_b_().func_236174_e_() * 2;
		ObfuscationReflectionHelper.setPrivateValue(NoiseChunkGenerator.class, this, horizontalNoiseGranularity, "field_222564_k");
		int noise = 16 / horizontalNoiseGranularity;
		ObfuscationReflectionHelper.setPrivateValue(NoiseChunkGenerator.class, this, noise, "field_222565_l");
		ObfuscationReflectionHelper.setPrivateValue(NoiseChunkGenerator.class, this, noise, "field_222567_n");
	}

	private VoidChunkGenerator(BiomeProvider biomeProvider, DimensionSettings dimensionSettings) {
		this(biomeProvider, new Random().nextLong(), dimensionSettings);
	}

	@Override
	protected Codec<? extends ChunkGenerator> func_230347_a_() {
		return codec;
	}

	@Override
	public ChunkGenerator func_230349_a_(long seed) {
		return new VoidChunkGenerator(biomeProvider.func_230320_a_(seed), seed, getDimensionSettings());
	}

	private DimensionSettings getDimensionSettings() {
		return field_236080_h_;
	}

	@Override
	public int func_230355_e_() {
		return 0;
	}
}
