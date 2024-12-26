package tamaized.voidscape.registry;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.neoforged.neoforge.registries.DeferredRegister;
import tamaized.beanification.Component;
import tamaized.regutil.RegUtil;
import tamaized.voidscape.surfacerule.AirAboveConditionSource;

import java.util.function.Supplier;

@Component
public class ModSurfaceRules {

	private final DeferredRegister<MapCodec<? extends SurfaceRules.ConditionSource>> REGISTRY = RegUtil.create(Registries.MATERIAL_CONDITION);

	public final Supplier<MapCodec<AirAboveConditionSource>> AIR_ABOVE = REGISTRY.register("air_above", AirAboveConditionSource.CODEC::codec);

}
