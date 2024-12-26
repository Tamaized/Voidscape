package tamaized.voidscape.registry.fluid;

import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;

import java.util.function.Supplier;

@Component
public class ModFluidProperties {

	@Autowired
	private ModFluids fluids;

	@Autowired
	private ModFluidTypes fluidTypes;

	@Autowired
	private ModFluidBuckets fluidBuckets;

	public final Supplier<BaseFlowingFluid.Properties> VOIDIC = () -> new BaseFlowingFluid.Properties(
		fluidTypes.VOIDIC,
		fluids.VOIDIC_SOURCE,
		fluids.VOIDIC_FLOWING
	).bucket(fluidBuckets.VOIDIC);

}
