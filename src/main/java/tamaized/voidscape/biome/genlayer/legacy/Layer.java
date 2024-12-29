package tamaized.voidscape.biome.genlayer.legacy;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.world.level.biome.Biome;
import org.jetbrains.annotations.Nullable;

public class Layer {
	public final LazyArea area;

	public Layer(AreaFactory<LazyArea> factory) {
		this.area = factory.make();
	}

	@Nullable
	public Holder<Biome> get(Registry<Biome> registry, int u, int v) {
		return null;
	}
}