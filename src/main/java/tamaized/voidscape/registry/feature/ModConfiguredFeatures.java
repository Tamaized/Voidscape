package tamaized.voidscape.registry.feature;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.neoforged.neoforge.registries.DeferredRegister;
import tamaized.beanification.Component;
import tamaized.regutil.RegUtil;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.features.ClusterFeature;
import tamaized.voidscape.features.FluidFeature;
import tamaized.voidscape.features.SpireFeature;
import tamaized.voidscape.features.ThunderVinesFeature;
import tamaized.voidscape.features.config.BooleanFeatureConfig;
import tamaized.voidscape.features.config.ClusterConfig;
import tamaized.voidscape.features.config.FluidFeatureConfig;

import java.util.function.Supplier;

@Component
public class ModConfiguredFeatures {

	public final ResourceKey<ConfiguredFeature<?, ?>> THUNDER_FUNGUS = create("thunder_fungus");

	public final ResourceKey<ConfiguredFeature<?, ?>> THUNDER_FOREST_VEGETATION_BONEMEAL = create("thunder_forest_vegetation_bonemeal");

	private ResourceKey<ConfiguredFeature<?, ?>> create(String name) {
		return ResourceKey.create(Registries.CONFIGURED_FEATURE, Identifier.fromNamespaceAndPath(Voidscape.MODID, name));
	}

}
