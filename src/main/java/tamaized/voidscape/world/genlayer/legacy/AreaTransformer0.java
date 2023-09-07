package tamaized.voidscape.world.genlayer.legacy;

public interface AreaTransformer0 {
	default <R extends Area> AreaFactory<R> run(BigContext<R> p_76985_) {
		return () -> {
			return p_76985_.createResult((p_164642_, p_164643_) -> {
				p_76985_.initRandom((long) p_164642_, (long) p_164643_);
				return this.applyPixel(p_76985_, p_164642_, p_164643_);
			});
		};
	}

	int applyPixel(Context p_76990_, int p_76991_, int p_76992_);
}