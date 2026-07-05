package tamaized.voidscape.registry.feature;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import tamaized.beanification.Component;
import tamaized.regutil.RegUtil;
import tamaized.voidscape.features.placements.*;

import java.util.function.Supplier;

@Component
public class ModFeaturePlacements {


	public final Supplier<PlacementModifierType<RandomYPlacementMod>> RANDOM_Y = RegUtil.register(Registries.PLACEMENT_MODIFIER_TYPE, "random_y",
		() -> () -> RandomYPlacementMod.CODEC);

	public final Supplier<PlacementModifierType<SeekDownPlacementMod>> SEEK_DOWN = RegUtil.register(Registries.PLACEMENT_MODIFIER_TYPE, "seek",
		() -> () -> SeekDownPlacementMod.CODEC);

	public final Supplier<PlacementModifierType<UnderBlockPlacementMod>> UNDER_BLOCK = RegUtil.register(Registries.PLACEMENT_MODIFIER_TYPE, "under_block",
		() -> () -> UnderBlockPlacementMod.CODEC);

}
