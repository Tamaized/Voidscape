package tamaized.voidscape.world.genlayer.legacy;

import net.minecraft.core.Registry;
import net.minecraft.world.level.biome.Biome;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Layer {
	private static final Logger LOGGER = LogManager.getLogger();
	public final LazyArea area;

	public Layer(AreaFactory<LazyArea> p_76714_) {
		this.area = p_76714_.make();
	}

	public Biome get(Registry<Biome> p_76716_, int p_76717_, int p_76718_) {
		return null;
	}
}