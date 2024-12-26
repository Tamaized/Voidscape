package tamaized.voidscape.registry.feature;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.neoforged.neoforge.registries.DeferredRegister;
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

	private final DeferredRegister<Feature<?>> REGISTRY = RegUtil.create(Registries.FEATURE);

	public final Supplier<Feature<BooleanFeatureConfig>> SPIRE = REGISTRY.register("spire", SpireFeature::new);

	public final Supplier<Feature<FluidFeatureConfig>> FLUID = REGISTRY.register("fluid", FluidFeature::new);

	public final Supplier<Feature<ClusterConfig>> CLUSTER = REGISTRY.register("cluster", ClusterFeature::new);

    public final Supplier<Feature<NoneFeatureConfiguration>> THUNDER_VINES = REGISTRY.register("thunder_vines", ThunderVinesFeature::new);

}
