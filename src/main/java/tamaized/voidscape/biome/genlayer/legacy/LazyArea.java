package tamaized.voidscape.biome.genlayer.legacy;

import it.unimi.dsi.fastutil.longs.Long2IntLinkedOpenHashMap;
import net.minecraft.world.level.ChunkPos;

public class LazyArea implements Area {
	private final PixelTransformer transformer;
	private final Long2IntLinkedOpenHashMap cache;
	private final int maxCache;

	public LazyArea(Long2IntLinkedOpenHashMap cache, int maxSize, PixelTransformer transformer) {
		this.cache = cache;
		this.maxCache = maxSize;
		this.transformer = transformer;
	}

	@Override
	public int get(int x, int z) {
		long i = ChunkPos.asLong(x, z);
		synchronized (this.cache) {
			int j = this.cache.get(i);
			if (j != Integer.MIN_VALUE) {
				return j;
			} else {
				int k = this.transformer.apply(x, z);
				this.cache.put(i, k);
				if (this.cache.size() > this.maxCache) {
					for (int l = 0; l < this.maxCache / 16; ++l) {
						this.cache.removeFirstInt();
					}
				}

				return k;
			}
		}
	}

	public int getMaxCache() {
		return this.maxCache;
	}
}
