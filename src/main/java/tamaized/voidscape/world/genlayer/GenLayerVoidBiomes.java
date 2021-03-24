package tamaized.voidscape.world.genlayer;

import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.layer.traits.IAreaTransformer0;
import tamaized.voidscape.registry.ModBiomes;
import tamaized.voidscape.world.VoidscapeSeededBiomeProvider;

public enum GenLayerVoidBiomes implements IAreaTransformer0 {
	INSTANCE;

	private VoidscapeSeededBiomeProvider provider;

	GenLayerVoidBiomes() {

	}

	public GenLayerVoidBiomes setup(VoidscapeSeededBiomeProvider provider) {
		this.provider = provider;
		return this;
	}

	@Override
	public int applyPixel(INoiseRandom iNoiseRandom, int x, int y) {
		return iNoiseRandom.nextRandom(4) == 0 ? getRandomBiome(iNoiseRandom) : provider.getBiomeId(ModBiomes.VOID);
	}

	private int getRandomBiome(INoiseRandom random) {
		return provider.getBiomeId(VoidscapeSeededBiomeProvider.BIOMES.get(1 + random.nextRandom(VoidscapeSeededBiomeProvider.BIOMES.size() - 1)));
	}
}