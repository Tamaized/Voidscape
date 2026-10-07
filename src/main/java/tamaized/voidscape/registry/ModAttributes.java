package tamaized.voidscape.registry;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.PercentageAttribute;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import tamaized.beanification.Component;
import tamaized.beanification.PostConstruct;
import tamaized.regutil.RegUtil;

import java.util.Locale;

@Component
public class ModAttributes {

	public final Holder<Attribute> VOIDIC_VISIBILITY = RegUtil.register(Registries.ATTRIBUTE, "voidic_visibility",
		(id) -> new PercentageAttribute(id.toLanguageKey(), 0F, 0F, 1F).setSyncable(true));

	public final Holder<Attribute> VOIDIC_INFUSION = RegUtil.register(Registries.ATTRIBUTE, "voidic_infusion",
		(id) -> new PercentageAttribute(id.toLanguageKey(), 0F, 0F, 1F));

	public final Holder<Attribute> VOIDIC_INFUSION_RES = RegUtil.register(Registries.ATTRIBUTE, "voidic_infusion_res",
		(id) -> new PercentageAttribute(id.toLanguageKey(), 0F, 0F, 1F).setSyncable(true));

	public final Holder<Attribute> VOIDIC_PARANOIA_RES = RegUtil.register(Registries.ATTRIBUTE, "voidic_paranoia_res",
		(id) -> new PercentageAttribute(id.toLanguageKey(), 0F, 0F, 1F).setSyncable(true));

	public final Holder<Attribute> VOIDIC_RES = RegUtil.register(Registries.ATTRIBUTE, "voidic_res",
		(id) -> new RangedAttribute(id.toLanguageKey(), 0F, 0F, 2048F));

	public final Holder<Attribute> VOIDIC_DMG = RegUtil.register(Registries.ATTRIBUTE, "voidic_dmg",
		(id) -> new RangedAttribute(id.toLanguageKey(), 0F, 0F, 2048F));

	public final Holder<Attribute> VOIDIC_ARROW_DMG = RegUtil.register(Registries.ATTRIBUTE, "voidic_arrow_dmg",
		(id) -> new RangedAttribute(id.toLanguageKey(), 0F, 0F, 2048F));

	public final Holder<Attribute> SHROUDED = RegUtil.register(Registries.ATTRIBUTE, "shrouded",
		(id) -> new PercentageAttribute(id.toLanguageKey(), 0F, 0F, 1F).setSyncable(true));

	public final Holder<Attribute> MADDENING = RegUtil.register(Registries.ATTRIBUTE, "maddening",
		(id) -> new PercentageAttribute(id.toLanguageKey(), 0F, 0F, 1F).setSyncable(true));

	@PostConstruct
	private void setup(IEventBus bus) {
		bus.addListener(EntityAttributeModificationEvent.class, event -> event.getTypes().forEach(e -> {
			event.add(e, VOIDIC_VISIBILITY);
			event.add(e, VOIDIC_INFUSION);
			event.add(e, VOIDIC_INFUSION_RES);
			event.add(e, VOIDIC_PARANOIA_RES);
			event.add(e, VOIDIC_RES);
			event.add(e, VOIDIC_DMG);
			event.add(e, VOIDIC_ARROW_DMG);
			event.add(e, SHROUDED);
			event.add(e, MADDENING);
		}));
	}

	public String getDraconicHealthId(EquipmentSlot slot) {
		return "draconic_health".concat(slot.getName().toLowerCase(Locale.ROOT));
	}

}
