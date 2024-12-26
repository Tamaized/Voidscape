package tamaized.voidscape.registry;

import com.google.common.collect.ImmutableSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.regutil.RegUtil;
import tamaized.voidscape.registry.block.FunctionalBlocks;

@Component
public class ModPOIs {

	@Autowired
	private FunctionalBlocks functionalBlocks;

	private final DeferredRegister<PoiType> REGISTERY = RegUtil.create(Registries.POINT_OF_INTEREST_TYPE);

	public final DeferredHolder<PoiType, PoiType> PORTAL = REGISTERY.register("portal", () -> new PoiType(ImmutableSet.copyOf(functionalBlocks.PORTAL.get().getStateDefinition().getPossibleStates()), 0, 1));

}
