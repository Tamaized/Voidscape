package tamaized.voidscape.registry;

import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import tamaized.beanification.Component;
import tamaized.regutil.RegUtil;
import tamaized.regutil.RegistryClass;
import tamaized.voidscape.advancement.*;

import java.util.function.Supplier;

@Component
public class ModAdvancementTriggers {

	private final DeferredRegister<CriterionTrigger<?>> REGISTRY = RegUtil.create(Registries.TRIGGER_TYPE);

	public final Supplier<GenericAdvancementTrigger> ETHEREAL_ESSENCE_TRIGGER = REGISTRY.register("ethereal_essence", GenericAdvancementTrigger::new);
	public final Supplier<GenericAdvancementTrigger> ETHEREAL_SPIDER_EGGS_TRIGGER = REGISTRY.register("ethereal_spider_eggs", GenericAdvancementTrigger::new);
	public final Supplier<ItemMatchesAdvancementTrigger> ITEM_USED_ON_NULL_SERVANT_TRIGGER = REGISTRY.register("item_used_on_null_servant", ItemMatchesAdvancementTrigger::new);
	public final Supplier<GenericAdvancementTrigger> ACTIVATE_PORTAL_TRIGGER = REGISTRY.register("activate_portal", GenericAdvancementTrigger::new);
	public final Supplier<GenericAdvancementTrigger> INFUSED_TRIGGER = REGISTRY.register("infused", GenericAdvancementTrigger::new);
	public final Supplier<GenericAdvancementTrigger> HOE_BONEMEAL_TRIGGER = REGISTRY.register("hoe_bonemeal", GenericAdvancementTrigger::new);
	public final Supplier<GenericAdvancementTrigger> LIQUIFIER_TRIGGER = REGISTRY.register("liquifier", GenericAdvancementTrigger::new);
	public final Supplier<GenericAdvancementTrigger> DEFUSER_TRIGGER = REGISTRY.register("defuser", GenericAdvancementTrigger::new);
	public final Supplier<GenericAdvancementTrigger> GERMINATOR_TRIGGER = REGISTRY.register("germinator", GenericAdvancementTrigger::new);
	public final Supplier<GenericAdvancementTrigger> WELL_TRIGGER = REGISTRY.register("well", GenericAdvancementTrigger::new);
	public final Supplier<GenericAdvancementTrigger> COOP_TRIGGER = REGISTRY.register("coop", GenericAdvancementTrigger::new);
	public final Supplier<GenericAdvancementTrigger> HATCHERY_TRIGGER = REGISTRY.register("hatchery", GenericAdvancementTrigger::new);
	public final Supplier<GenericAdvancementTrigger> INFUSER_TRIGGER = REGISTRY.register("infuser", GenericAdvancementTrigger::new);
	public final Supplier<GenericAdvancementTrigger> COLLECTOR_TRIGGER = REGISTRY.register("collector", GenericAdvancementTrigger::new);
	public final Supplier<ItemMatchesAdvancementTrigger> THREE_BY_THREE = REGISTRY.register("three_by_three", ItemMatchesAdvancementTrigger::new);

}
