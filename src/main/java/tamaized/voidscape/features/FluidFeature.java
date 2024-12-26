package tamaized.voidscape.features;

import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import tamaized.voidscape.features.config.FluidFeatureConfig;

public class FluidFeature extends Feature<FluidFeatureConfig> {

	public FluidFeature() {
		super(FluidFeatureConfig.CODEC);
	}

	@Override
	public boolean place(FeaturePlaceContext<FluidFeatureConfig> context) {
		context.level().setBlock(context.origin(), context.config().state.createLegacyBlock(), 2);
		context.level().scheduleTick(context.origin(), context.config().state.getType(), 0);
		return true;
	}

}
