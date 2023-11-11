package tamaized.voidscape.registry;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import tamaized.regutil.RegistryClass;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.advancement.*;

public class ModAdvancementTriggers implements RegistryClass {

	public static final EtherealEssenceTrigger ETHEREAL_ESSENCE_TRIGGER = CriteriaTriggers.register(new ResourceLocation(Voidscape.MODID, "ethereal_essence").toString(), new EtherealEssenceTrigger());
	public static final ItemUsedOnNullServantTrigger ITEM_USED_ON_NULL_SERVANT_TRIGGER = CriteriaTriggers.register(new ResourceLocation(Voidscape.MODID, "item_used_on_null_servant").toString(), new ItemUsedOnNullServantTrigger());
	public static final ActivatePortalTrigger ACTIVATE_PORTAL_TRIGGER = CriteriaTriggers.register(new ResourceLocation(Voidscape.MODID, "activate_portal").toString(), new ActivatePortalTrigger());
	public static final InfusedTrigger INFUSED_TRIGGER = CriteriaTriggers.register(new ResourceLocation(Voidscape.MODID, "infused").toString(), new InfusedTrigger());
	public static final HoeBonemealTrigger HOE_BONEMEAL_TRIGGER = CriteriaTriggers.register(new ResourceLocation(Voidscape.MODID, "hoe_bonemeal").toString(), new HoeBonemealTrigger());
	public static final LiquifierTrigger LIQUIFIER_TRIGGER = CriteriaTriggers.register(new ResourceLocation(Voidscape.MODID, "liquifier").toString(), new LiquifierTrigger());
	public static final DefuserTrigger DEFUSER_TRIGGER = CriteriaTriggers.register(new ResourceLocation(Voidscape.MODID, "defuser").toString(), new DefuserTrigger());
	public static final ThreeByThreeTrigger THREE_BY_THREE = CriteriaTriggers.register(new ResourceLocation(Voidscape.MODID, "three_by_three").toString(), new ThreeByThreeTrigger());

	@Override
	public void init(IEventBus bus) {

	}

}
