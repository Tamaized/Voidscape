package tamaized.voidscape.world.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.material.FluidState;

public class FluidFeatureConfig implements FeatureConfiguration {
	public static final Codec<FluidFeatureConfig> CODEC = RecordCodecBuilder.create((config) -> config.
			group(FluidState.CODEC.fieldOf("state").forGetter((conf) -> conf.state)).
			apply(config, FluidFeatureConfig::new));
	public final FluidState state;

	public FluidFeatureConfig(FluidState p_68131_) {
		this.state = p_68131_;
	}
}
