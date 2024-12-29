package tamaized.voidscape.biome.genlayer.legacy;

public interface AreaTransformer0 {
	default <R extends Area> AreaFactory<R> run(BigContext<R> context) {
		return () -> context.createResult((u, v) -> {
			context.initRandom(u, v);
			return this.applyPixel(context, u, v);
		});
	}

	int applyPixel(Context context, int u, int v);
}