package tamaized.voidscape.biome.genlayer.legacy;

public interface AreaTransformer1 extends DimensionTransformer {
	default <R extends Area> AreaFactory<R> run(BigContext<R> context, AreaFactory<R> factory) {
		return () -> {
			R r = factory.make();
			return context.createResult((u, v) -> {
				context.initRandom(u, v);
				return this.applyPixel(context, r, u, v);
			}, r);
		};
	}

	int applyPixel(BigContext<?> context, Area area, int u, int v);
}