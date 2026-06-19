package tamaized.voidscape.registry;

import com.google.common.collect.ImmutableSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.neoforged.neoforge.registries.DeferredHolder;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.regutil.RegUtil;
import tamaized.voidscape.registry.block.FunctionalBlocks;

@Component
public class ModPOIs {

	@Autowired
	private FunctionalBlocks functionalBlocks;

	public final DeferredHolder<PoiType, PoiType> PORTAL = RegUtil.register(Registries.POINT_OF_INTEREST_TYPE, "portal",
		() -> new PoiType(ImmutableSet.copyOf(functionalBlocks.PORTAL.get().getStateDefinition().getPossibleStates()), 0, 1));

}
