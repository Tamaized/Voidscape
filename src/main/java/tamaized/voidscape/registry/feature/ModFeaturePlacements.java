package tamaized.voidscape.registry.feature;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.neoforged.neoforge.registries.DeferredRegister;
import tamaized.beanification.Component;
import tamaized.regutil.RegUtil;
import tamaized.voidscape.features.placements.*;

import java.util.function.Supplier;

@Component
public class ModFeaturePlacements {

	private final DeferredRegister<PlacementModifierType<?>> REGISTRY = RegUtil.create(Registries.PLACEMENT_MODIFIER_TYPE);

	public final Supplier<PlacementModifierType<RandomYPlacementMod>> RANDOM_Y = REGISTRY.register("random_y", () -> () -> RandomYPlacementMod.CODEC);

	public final Supplier<PlacementModifierType<SeekDownPlacementMod>> SEEK_DOWN = REGISTRY.register("seek", () -> () -> SeekDownPlacementMod.CODEC);

	public final Supplier<PlacementModifierType<UnderBlockPlacementMod>> UNDER_BLOCK = REGISTRY.register("under_block", () -> () -> UnderBlockPlacementMod.CODEC);

}
