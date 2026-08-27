package tamaized.voidscape.registry.fluid;

import net.minecraft.sounds.SoundEvents;
import net.neoforged.neoforge.common.SoundActions;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import tamaized.beanification.Component;
import tamaized.regutil.RegUtil;

@Component
public class ModFluidTypes {

	public final DeferredHolder<FluidType, FluidType> VOIDIC = RegUtil.register(NeoForgeRegistries.Keys.FLUID_TYPES, "voidic", () -> new FluidType(
		FluidType.Properties.create()
			.sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL_LAVA)
			.sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY_LAVA)
			.lightLevel(1)
			.density(4000)
			.viscosity(4000)));

}
