package tamaized.voidscape.registry;

import com.google.common.collect.ImmutableSet;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import tamaized.regutil.RegUtil;
import tamaized.regutil.RegistryClass;
import tamaized.voidscape.Voidscape;

import java.util.UUID;

public class ModPOIs implements RegistryClass {

	@Override
	public void init(IEventBus bus) {

	}

	private static final DeferredRegister<PoiType> REGISTERY = RegUtil.create(ForgeRegistries.POI_TYPES);

	public static final RegistryObject<PoiType> PORTAL = REGISTERY.register("portal", () -> new PoiType(ImmutableSet.copyOf(ModBlocks.PORTAL.get().getStateDefinition().getPossibleStates()), 0, 1));

}
