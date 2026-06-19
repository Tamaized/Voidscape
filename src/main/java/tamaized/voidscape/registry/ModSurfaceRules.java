package tamaized.voidscape.registry;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.Registries;
import tamaized.beanification.Component;
import tamaized.regutil.RegUtil;
import tamaized.voidscape.surfacerule.AirAboveConditionSource;

import java.util.function.Supplier;

@Component
public class ModSurfaceRules {

	public final Supplier<MapCodec<AirAboveConditionSource>> AIR_ABOVE = RegUtil.register(Registries.MATERIAL_CONDITION, "air_above", AirAboveConditionSource.CODEC::codec);

}
