package tamaized.voidscape.world.genlayer.legacy;

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

	public LazyAreaContext(int p_76523_, long p_76524_, long p_76525_) {
		this.seed = mixSeed(p_76524_, p_76525_);
		this.biomeNoise = new ImprovedNoise(new LegacyRandomSource(p_76524_));
		this.cache = new Long2IntLinkedOpenHashMap(16, 0.25F);
		this.cache.defaultReturnValue(Integer.MIN_VALUE);
		this.maxCache = p_76523_;
	}

	private static long mixSeed(long p_76549_, long p_76550_) {
		long i = LinearCongruentialGenerator.next(p_76550_, p_76550_);
		i = LinearCongruentialGenerator.next(i, p_76550_);
		i = LinearCongruentialGenerator.next(i, p_76550_);
		long j = LinearCongruentialGenerator.next(p_76549_, i);
		j = LinearCongruentialGenerator.next(j, i);
		return LinearCongruentialGenerator.next(j, i);
	}

	@Override
	public LazyArea createResult(PixelTransformer p_76552_) {
		return new LazyArea(this.cache, this.maxCache, p_76552_);
	}

	@Override
	public LazyArea createResult(PixelTransformer p_76541_, LazyArea p_76542_) {
		return new LazyArea(this.cache, Math.min(1024, p_76542_.getMaxCache() * 4), p_76541_);
	}

	@Override
	public LazyArea createResult(PixelTransformer p_76544_, LazyArea p_76545_, LazyArea p_76546_) {
		return new LazyArea(this.cache, Math.min(1024, Math.max(p_76545_.getMaxCache(), p_76546_.getMaxCache()) * 4), p_76544_);
	}

	@Override
	public void initRandom(long p_76529_, long p_76530_) {
		long i = this.seed;
		i = LinearCongruentialGenerator.next(i, p_76529_);
		i = LinearCongruentialGenerator.next(i, p_76530_);
		i = LinearCongruentialGenerator.next(i, p_76529_);
		i = LinearCongruentialGenerator.next(i, p_76530_);
		this.rval = i;
	}

	@Override
	public int nextRandom(int p_76527_) {
		int i = Math.floorMod(this.rval >> 24, p_76527_);
		this.rval = LinearCongruentialGenerator.next(this.rval, this.seed);
		return i;
	}

	@Override
	public ImprovedNoise getBiomeNoise() {
		return this.biomeNoise;
	}
}