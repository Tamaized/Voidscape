package tamaized.voidscape.world.genlayer;

import com.google.common.collect.ImmutableList;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import tamaized.voidscape.registry.ModBiomes;
import tamaized.voidscape.world.VoidscapeSeededBiomeProvider;
import tamaized.voidscape.world.genlayer.legacy.AreaTransformer0;
import tamaized.voidscape.world.genlayer.legacy.Context;

import java.util.List;

public enum GenLayerVoidBiomes implements AreaTransformer0 {
	INSTANCE;

	public static final List<ResourceKey<Biome>> BIOMES = ImmutableList.of(

			ModBiomes.VOID,

			ModBiomes.OVERWORLD,

			ModBiomes.NETHER,

			ModBiomes.END

	);

	private VoidscapeSeededBiomeProvider provider;

	GenLayerVoidBiomes() {

	}

	public GenLayerVoidBiomes setup(VoidscapeSeededBiomeProvider provider) {
		this.provider = provider;
		return this;
	}

	@Override
	public int applyPixel(Context iNoiseRandom, int x, int y) {
		return iNoiseRandom.nextRandom(4) == 0 ? getRandomBiome(iNoiseRandom) : provider.getBiomeId(ModBiomes.VOID);
	}

	private int getRandomBiome(Context random) {
		return provider.getBiomeId(BIOMES.get(1 + random.nextRandom(BIOMES.size() - 1)));
	}
}