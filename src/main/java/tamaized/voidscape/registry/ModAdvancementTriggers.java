package tamaized.voidscape.registry;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import tamaized.regutil.RegistryClass;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.advancement.*;

public class ModAdvancementTriggers implements RegistryClass {

	public static final GenericAdvancementTrigger ETHEREAL_ESSENCE_TRIGGER = CriteriaTriggers
			.register(new ResourceLocation(Voidscape.MODID, "ethereal_essence").toString(), new GenericAdvancementTrigger());

	public static final ItemMatchesAdvancementTrigger ITEM_USED_ON_NULL_SERVANT_TRIGGER = CriteriaTriggers
			.register(new ResourceLocation(Voidscape.MODID, "item_used_on_null_servant").toString(), new ItemMatchesAdvancementTrigger());

	public static final GenericAdvancementTrigger ACTIVATE_PORTAL_TRIGGER = CriteriaTriggers
			.register(new ResourceLocation(Voidscape.MODID, "activate_portal").toString(), new GenericAdvancementTrigger());

	public static final GenericAdvancementTrigger INFUSED_TRIGGER = CriteriaTriggers
			.register(new ResourceLocation(Voidscape.MODID, "infused").toString(), new GenericAdvancementTrigger());

	public static final GenericAdvancementTrigger HOE_BONEMEAL_TRIGGER = CriteriaTriggers
			.register(new ResourceLocation(Voidscape.MODID, "hoe_bonemeal").toString(), new GenericAdvancementTrigger());

	public static final GenericAdvancementTrigger LIQUIFIER_TRIGGER = CriteriaTriggers
			.register(new ResourceLocation(Voidscape.MODID, "liquifier").toString(), new GenericAdvancementTrigger());

	public static final GenericAdvancementTrigger DEFUSER_TRIGGER = CriteriaTriggers
			.register(new ResourceLocation(Voidscape.MODID, "defuser").toString(), new GenericAdvancementTrigger());

	public static final ItemMatchesAdvancementTrigger THREE_BY_THREE = CriteriaTriggers
			.register(new ResourceLocation(Voidscape.MODID, "three_by_three").toString(), new ItemMatchesAdvancementTrigger());

	@Override
	public void init(IEventBus bus) {

	}

}
