package tamaized.voidscape.registry.feature;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.neoforged.neoforge.registries.DeferredRegister;
import tamaized.beanification.Component;
import tamaized.regutil.RegUtil;
import tamaized.voidscape.features.placements.AirAbovePlacementMod;

import java.util.function.Supplier;

@Component
public class ModFeaturePlacements {

	private final DeferredRegister<PlacementModifierType<?>> REGISTRY = RegUtil.create(Registries.PLACEMENT_MODIFIER_TYPE);

	public final Supplier<PlacementModifierType<AirAbovePlacementMod>> AIR_ABOVE = REGISTRY.register("air_above", () -> () -> AirAbovePlacementMod.CODEC);

	public final Supplier<PlacementModifierType<AirAbovePlacementMod>> NOT_AIR_BELOW = REGISTRY.register("not_air_below", () -> () -> NotAirBelowPlacementMod.CODEC);

}
