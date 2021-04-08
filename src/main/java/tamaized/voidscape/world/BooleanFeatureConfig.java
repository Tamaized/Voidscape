package tamaized.voidscape.world;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.gen.feature.IFeatureConfig;

public class BooleanFeatureConfig implements IFeatureConfig {
	public static final Codec<BooleanFeatureConfig> CODEC = RecordCodecBuilder.create((p_242803_0_) -> p_242803_0_.group(Codec.
			BOOL.fieldOf("invert").orElse(false).forGetter(c -> c.invert)).apply(p_242803_0_, BooleanFeatureConfig::new));
	private boolean invert;

	public BooleanFeatureConfig(boolean val) {
		invert = val;
	}

	public boolean get() {
		return invert;
	}
}
