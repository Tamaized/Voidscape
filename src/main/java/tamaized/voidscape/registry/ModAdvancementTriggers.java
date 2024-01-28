package tamaized.voidscape.registry;

import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import tamaized.regutil.RegUtil;
import tamaized.regutil.RegistryClass;
import tamaized.voidscape.advancement.*;

import java.util.function.Supplier;

public class ModAdvancementTriggers implements RegistryClass {

	private static final DeferredRegister<CriterionTrigger<?>> REGISTRY = RegUtil.create(Registries.TRIGGER_TYPE);

	public static final Supplier<GenericAdvancementTrigger> ETHEREAL_ESSENCE_TRIGGER = REGISTRY.register("ethereal_essence", GenericAdvancementTrigger::new);
	public static final Supplier<ItemMatchesAdvancementTrigger> ITEM_USED_ON_NULL_SERVANT_TRIGGER = REGISTRY.register("item_used_on_null_servant", ItemMatchesAdvancementTrigger::new);
	public static final Supplier<GenericAdvancementTrigger> ACTIVATE_PORTAL_TRIGGER = REGISTRY.register("activate_portal", GenericAdvancementTrigger::new);
	public static final Supplier<GenericAdvancementTrigger> INFUSED_TRIGGER = REGISTRY.register("infused", GenericAdvancementTrigger::new);
	public static final Supplier<GenericAdvancementTrigger> HOE_BONEMEAL_TRIGGER = REGISTRY.register("hoe_bonemeal", GenericAdvancementTrigger::new);
	public static final Supplier<GenericAdvancementTrigger> LIQUIFIER_TRIGGER = REGISTRY.register("liquifier", GenericAdvancementTrigger::new);
	public static final Supplier<GenericAdvancementTrigger> DEFUSER_TRIGGER = REGISTRY.register("defuser", GenericAdvancementTrigger::new);
	public static final Supplier<GenericAdvancementTrigger> GERMINATOR_TRIGGER = REGISTRY.register("germinator", GenericAdvancementTrigger::new);
	public static final Supplier<GenericAdvancementTrigger> WELL_TRIGGER = REGISTRY.register("well", GenericAdvancementTrigger::new);
	public static final Supplier<GenericAdvancementTrigger> COOP_TRIGGER = REGISTRY.register("coop", GenericAdvancementTrigger::new);
	public static final Supplier<GenericAdvancementTrigger> INFUSER_TRIGGER = REGISTRY.register("infuser", GenericAdvancementTrigger::new);
	public static final Supplier<GenericAdvancementTrigger> COLLECTOR_TRIGGER = REGISTRY.register("collector", GenericAdvancementTrigger::new);
	public static final Supplier<ItemMatchesAdvancementTrigger> THREE_BY_THREE = REGISTRY.register("three_by_three", ItemMatchesAdvancementTrigger::new);

	@Override
	public void init(IEventBus bus) {

	}

}
