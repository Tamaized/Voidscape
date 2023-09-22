package tamaized.voidscape.world.genlayer;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import tamaized.voidscape.world.VoidscapeLayeredBiomeProvider;
import tamaized.voidscape.world.genlayer.legacy.AreaTransformer0;
import tamaized.voidscape.world.genlayer.legacy.Context;

import java.util.List;

public class GenLayerRandomWithOneMajorBiomes implements AreaTransformer0 {
	public static final Codec<GenLayerRandomWithOneMajorBiomes> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.list(VoidscapeLayeredBiomeProvider.conditionalModLoadedBiome()).fieldOf("biomes").stable().forGetter(obj -> obj.biomes),
			ResourceKey.codec(Registries.BIOME).fieldOf("majorBiome").stable().forGetter(obj -> obj.majorBiome),
			Codec.INT.fieldOf("chance").stable().forGetter(obj -> obj.chance)
	).apply(instance, instance.stable(GenLayerRandomWithOneMajorBiomes::new)));

	private final List<Either<ResourceKey<Biome>, VoidscapeLayeredBiomeProvider.ConditionalBiomeHolder>> biomes;
	private final List<ResourceKey<Biome>> loadedBiomes;
	private final ResourceKey<Biome> majorBiome;
	private final int chance;

	private VoidscapeLayeredBiomeProvider provider;

	GenLayerRandomWithOneMajorBiomes(List<Either<ResourceKey<Biome>, VoidscapeLayeredBiomeProvider.ConditionalBiomeHolder>> biomes, ResourceKey<Biome> majorBiome, int chance) {
		this.biomes = biomes;
		this.loadedBiomes = VoidscapeLayeredBiomeProvider.getConditionalBiomes(biomes);
		this.majorBiome = majorBiome;
		this.chance = chance;
	}

	public GenLayerRandomWithOneMajorBiomes setup(VoidscapeLayeredBiomeProvider provider) {
		this.provider = provider;
		return this;
	}

	@Override
	public int applyPixel(Context iNoiseRandom, int x, int y) {
		return !loadedBiomes.isEmpty() && iNoiseRandom.nextRandom(chance) == 0 ? getRandomBiome(iNoiseRandom) : provider.getBiomeId(majorBiome);
	}

	private int getRandomBiome(Context random) {
		return provider.getBiomeId(loadedBiomes.get(random.nextRandom(loadedBiomes.size())));
	}

}