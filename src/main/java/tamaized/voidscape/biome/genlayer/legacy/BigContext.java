package tamaized.voidscape.biome.genlayer.legacy;

public interface BigContext<R extends Area> extends Context {
	void initRandom(long u, long v);

	R createResult(PixelTransformer transformer);

	default R createResult(PixelTransformer transformer, R result) {
		return this.createResult(transformer);
	}

	default R createResult(PixelTransformer transformer, R resultA, R resultB) {
		return this.createResult(transformer);
	}

	default int random(int a, int b) {
		return this.nextRandom(2) == 0 ? a : b;
	}

	default int random(int a, int b, int c, int d) {
		int i = this.nextRandom(4);
		if (i == 0) {
			return a;
		} else if (i == 1) {
			return b;
		} else {
			return i == 2 ? c : d;
		}
	}
}