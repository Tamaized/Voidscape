package tamaized.voidscape.registry;

import com.google.common.collect.ImmutableSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import tamaized.regutil.RegUtil;
import tamaized.regutil.RegistryClass;

public class ModPOIs implements RegistryClass {

	@Override
	public void init(IEventBus bus) {

	}

	private static final DeferredRegister<PoiType> REGISTERY = RegUtil.create(Registries.POINT_OF_INTEREST_TYPE);

	public static final DeferredHolder<PoiType, PoiType> PORTAL = REGISTERY.register("portal", () -> new PoiType(ImmutableSet.copyOf(ModBlocks.PORTAL.get().getStateDefinition().getPossibleStates()), 0, 1));

}
