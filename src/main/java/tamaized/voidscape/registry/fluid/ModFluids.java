package tamaized.voidscape.registry.fluid;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.material.FlowingFluid;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.regutil.RegUtil;
import tamaized.voidscape.fluid.VoidicFluid;

import java.util.function.Supplier;

@Component
public class ModFluids {

	@Autowired
	private ModFluidProperties fluidProperties;

	public final Supplier<FlowingFluid> VOIDIC_SOURCE = RegUtil.register(Registries.FLUID, "voidic_source", () -> new VoidicFluid.Source(fluidProperties.VOIDIC.get()));
	public final Supplier<FlowingFluid> VOIDIC_FLOWING = RegUtil.register(Registries.FLUID, "voidic_flowing", () -> new VoidicFluid.Flowing(fluidProperties.VOIDIC.get()));

}
