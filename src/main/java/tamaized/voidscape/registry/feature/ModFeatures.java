package tamaized.voidscape.registry.feature;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import tamaized.beanification.Component;
import tamaized.regutil.RegUtil;
import tamaized.voidscape.features.ClusterFeature;
import tamaized.voidscape.features.FluidFeature;
import tamaized.voidscape.features.SpireFeature;
import tamaized.voidscape.features.ThunderVinesFeature;
import tamaized.voidscape.features.config.BooleanFeatureConfig;
import tamaized.voidscape.features.config.ClusterConfig;
import tamaized.voidscape.features.config.FluidFeatureConfig;

import java.util.function.Supplier;

@Component
public class ModFeatures {

	public final Supplier<Feature<BooleanFeatureConfig>> SPIRE = RegUtil.register(Registries.FEATURE, "spire", SpireFeature::new);

	public final Supplier<Feature<FluidFeatureConfig>> FLUID = RegUtil.register(Registries.FEATURE, "fluid", FluidFeature::new);

	public final Supplier<Feature<ClusterConfig>> CLUSTER = RegUtil.register(Registries.FEATURE, "cluster", ClusterFeature::new);

    public final Supplier<Feature<NoneFeatureConfiguration>> THUNDER_VINES = RegUtil.register(Registries.FEATURE, "thunder_vines", ThunderVinesFeature::new);

}
