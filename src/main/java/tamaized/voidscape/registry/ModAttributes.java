package tamaized.voidscape.registry;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.beanification.PostConstruct;
import tamaized.regutil.RegUtil;
import tamaized.voidscape.util.NamespaceUtils;

@Component
public class ModAttributes {

	@Autowired
	private NamespaceUtils namespaceUtils;

	private final DeferredRegister<Attribute> REGISTERY = RegUtil.create(Registries.ATTRIBUTE);

	public final Holder<Attribute> VOIDIC_VISIBILITY = REGISTERY.register("voidic_visibility", () -> new RangedAttribute(namespaceUtils.prefixId("voidic_visibility"), 1F, 1F, 2F).setSyncable(true));
	public final Holder<Attribute> VOIDIC_INFUSION = REGISTERY.register("voidic_infusion", () -> new RangedAttribute(namespaceUtils.prefixId("voidic_infusion"), 1F, 1F, 2F));
	public final Holder<Attribute> VOIDIC_INFUSION_RES = REGISTERY.register("voidic_infusion_res", () -> new RangedAttribute(namespaceUtils.prefixId("voidic_infusion_res"), 1F, 1F, 2F).setSyncable(true));
	public final Holder<Attribute> VOIDIC_PARANOIA_RES = REGISTERY.register("voidic_paranoia_res", () -> new RangedAttribute(namespaceUtils.prefixId("voidic_paranoia_res"), 1F, 1F, 2F).setSyncable(true));
	public final Holder<Attribute> VOIDIC_RES = REGISTERY.register("voidic_res", () -> new RangedAttribute(namespaceUtils.prefixId("voidic_res"), 0F, 0F, 2048F));
	public final Holder<Attribute> VOIDIC_DMG = REGISTERY.register("voidic_dmg", () -> new RangedAttribute(namespaceUtils.prefixId("voidic_dmg"), 0F, 0F, 2048F));
	public final Holder<Attribute> VOIDIC_ARROW_DMG = REGISTERY.register("voidic_arrow_dmg", () -> new RangedAttribute(namespaceUtils.prefixId("voidic_arrow_dmg"), 0F, 0F, 2048F));


	public final String DRACONIC_HEALTH_ID = "draconic_health";

	@PostConstruct
	private void setup(IEventBus bus) {
		bus.addListener(EntityAttributeModificationEvent.class, event -> event.getTypes().forEach(e -> {
			event.add(e, VOIDIC_VISIBILITY, 1F);
			event.add(e, VOIDIC_INFUSION, 1F);
			event.add(e, VOIDIC_INFUSION_RES, 1F);
			event.add(e, VOIDIC_PARANOIA_RES, 1F);
			event.add(e, VOIDIC_RES, 0F);
			event.add(e, VOIDIC_DMG, 0F);
			event.add(e, VOIDIC_ARROW_DMG, 0F);
		}));
	}

}
