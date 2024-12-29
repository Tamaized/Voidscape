package tamaized.voidscape.biome.genlayer.legacy;

import it.unimi.dsi.fastutil.longs.Long2IntLinkedOpenHashMap;
import net.minecraft.util.LinearCongruentialGenerator;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.synth.ImprovedNoise;

public class LazyAreaContext implements BigContext<LazyArea> {
	private static final int MAX_CACHE = 1024;
	private final Long2IntLinkedOpenHashMap cache;
	private final int maxCache;
	private final ImprovedNoise biomeNoise;
	private final long seed;
	private long rval;

	public LazyAreaContext(int maxCache, long worldSeed, long layerSeed) {
		this.seed = mixSeed(worldSeed, layerSeed);
		this.biomeNoise = new ImprovedNoise(new LegacyRandomSource(worldSeed));
		this.cache = new Long2IntLinkedOpenHashMap(16, 0.25F);
		this.cache.defaultReturnValue(Integer.MIN_VALUE);
		this.maxCache = maxCache;
	}

	private static long mixSeed(long worldSeed, long layerSeed) {
		long i = LinearCongruentialGenerator.next(layerSeed, layerSeed);
		i = LinearCongruentialGenerator.next(i, layerSeed);
		i = LinearCongruentialGenerator.next(i, layerSeed);
		long j = LinearCongruentialGenerator.next(worldSeed, i);
		j = LinearCongruentialGenerator.next(j, i);
		return LinearCongruentialGenerator.next(j, i);
	}

	@Override
	public LazyArea createResult(PixelTransformer transformer) {
		return new LazyArea(this.cache, this.maxCache, transformer);
	}

	@Override
	public LazyArea createResult(PixelTransformer transformer, LazyArea area) {
		return new LazyArea(this.cache, Math.min(MAX_CACHE, area.getMaxCache() * 4), transformer);
	}

	@Override
	public LazyArea createResult(PixelTransformer transformer, LazyArea areaA, LazyArea areaB) {
		return new LazyArea(this.cache, Math.min(MAX_CACHE, Math.max(areaA.getMaxCache(), areaB.getMaxCache()) * 4), transformer);
	}

	@Override
	public void initRandom(long u, long v) {
		long i = this.seed;
		i = LinearCongruentialGenerator.next(i, u);
		i = LinearCongruentialGenerator.next(i, v);
		i = LinearCongruentialGenerator.next(i, u);
		i = LinearCongruentialGenerator.next(i, v);
		this.rval = i;
	}

	@Override
	public int nextRandom(int bound) {
		int i = Math.floorMod(this.rval >> 24, bound);
		this.rval = LinearCongruentialGenerator.next(this.rval, this.seed);
		return i;
	}

	@Override
	public ImprovedNoise getBiomeNoise() {
		return this.biomeNoise;
	}
}