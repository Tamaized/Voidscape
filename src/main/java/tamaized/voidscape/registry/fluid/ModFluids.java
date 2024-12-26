package tamaized.voidscape.registry.fluid;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.registries.DeferredRegister;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.regutil.RegUtil;
import tamaized.voidscape.fluid.VoidicFluid;

import java.util.function.Supplier;

@Component
public class ModFluids {

	private final DeferredRegister<Fluid> REGISTERY = RegUtil.create(Registries.FLUID);

	@Autowired
	private ModFluidProperties fluidProperties;

	public final Supplier<FlowingFluid> VOIDIC_SOURCE = REGISTERY.register("voidic_source", () -> new VoidicFluid.Source(fluidProperties.VOIDIC.get()));
	public final Supplier<FlowingFluid> VOIDIC_FLOWING = REGISTERY.register("voidic_flowing", () -> new VoidicFluid.Flowing(fluidProperties.VOIDIC.get()));

}
