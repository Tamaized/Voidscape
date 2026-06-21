package tamaized.voidscape.registry;

import net.minecraft.core.registries.Registries;
import tamaized.beanification.Component;
import tamaized.regutil.RegUtil;
import tamaized.voidscape.advancement.*;

import java.util.function.Supplier;

@Component
public class ModAdvancementTriggers {

	public final Supplier<GenericAdvancementTrigger> ETHEREAL_ESSENCE_TRIGGER = RegUtil.register(Registries.TRIGGER_TYPE, "ethereal_essence", GenericAdvancementTrigger::new);
	public final Supplier<GenericAdvancementTrigger> ETHEREAL_SPIDER_EGGS_TRIGGER = RegUtil.register(Registries.TRIGGER_TYPE, "ethereal_spider_eggs", GenericAdvancementTrigger::new);
	public final Supplier<ItemMatchesAdvancementTrigger> ITEM_USED_ON_NULL_SERVANT_TRIGGER = RegUtil.register(Registries.TRIGGER_TYPE, "item_used_on_null_servant", ItemMatchesAdvancementTrigger::new);
	public final Supplier<GenericAdvancementTrigger> ACTIVATE_PORTAL_TRIGGER = RegUtil.register(Registries.TRIGGER_TYPE, "activate_portal", GenericAdvancementTrigger::new);
	public final Supplier<GenericAdvancementTrigger> INFUSED_TRIGGER = RegUtil.register(Registries.TRIGGER_TYPE, "infused", GenericAdvancementTrigger::new);
	public final Supplier<GenericAdvancementTrigger> HOE_BONEMEAL_TRIGGER = RegUtil.register(Registries.TRIGGER_TYPE, "hoe_bonemeal", GenericAdvancementTrigger::new);
	public final Supplier<GenericAdvancementTrigger> LIQUIFIER_TRIGGER = RegUtil.register(Registries.TRIGGER_TYPE, "liquifier", GenericAdvancementTrigger::new);
	public final Supplier<GenericAdvancementTrigger> DEFUSER_TRIGGER = RegUtil.register(Registries.TRIGGER_TYPE, "defuser", GenericAdvancementTrigger::new);
	public final Supplier<GenericAdvancementTrigger> GERMINATOR_TRIGGER = RegUtil.register(Registries.TRIGGER_TYPE, "germinator", GenericAdvancementTrigger::new);
	public final Supplier<GenericAdvancementTrigger> WELL_TRIGGER = RegUtil.register(Registries.TRIGGER_TYPE, "well", GenericAdvancementTrigger::new);
	public final Supplier<GenericAdvancementTrigger> COOP_TRIGGER = RegUtil.register(Registries.TRIGGER_TYPE, "coop", GenericAdvancementTrigger::new);
	public final Supplier<GenericAdvancementTrigger> HATCHERY_TRIGGER = RegUtil.register(Registries.TRIGGER_TYPE, "hatchery", GenericAdvancementTrigger::new);
	public final Supplier<GenericAdvancementTrigger> INFUSER_TRIGGER = RegUtil.register(Registries.TRIGGER_TYPE, "infuser", GenericAdvancementTrigger::new);
	public final Supplier<GenericAdvancementTrigger> COLLECTOR_TRIGGER = RegUtil.register(Registries.TRIGGER_TYPE, "collector", GenericAdvancementTrigger::new);
	public final Supplier<ItemMatchesAdvancementTrigger> THREE_BY_THREE = RegUtil.register(Registries.TRIGGER_TYPE, "three_by_three", ItemMatchesAdvancementTrigger::new);

}
