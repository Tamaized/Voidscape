package tamaized.voidscape.world.genlayer.legacy;

public interface AreaTransformer1 extends DimensionTransformer {
	default <R extends Area> AreaFactory<R> run(BigContext<R> p_77003_, AreaFactory<R> p_77004_) {
		return () -> {
			R r = p_77004_.make();
			return p_77003_.createResult((p_164647_, p_164648_) -> {
				p_77003_.initRandom((long) p_164647_, (long) p_164648_);
				return this.applyPixel(p_77003_, r, p_164647_, p_164648_);
			}, r);
		};
	}

	int applyPixel(BigContext<?> p_76998_, Area p_76999_, int p_77000_, int p_77001_);
}