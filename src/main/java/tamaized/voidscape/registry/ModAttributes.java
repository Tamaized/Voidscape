package tamaized.voidscape.registry;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.PercentageAttribute;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.beanification.PostConstruct;
import tamaized.regutil.RegUtil;
import tamaized.voidscape.util.NamespaceUtils;

import java.util.Locale;
import java.util.Objects;

@Component
public class ModAttributes {

	@Autowired
	private NamespaceUtils namespaceUtils;

	@Autowired
	private ModDataAttachments dataAttachments;

	public final Holder<Attribute> VOIDIC_VISIBILITY = RegUtil.register(Registries.ATTRIBUTE, "voidic_visibility",
		() -> new PercentageAttribute(namespaceUtils.prefixId("voidic_visibility"), 0F, 0F, 1F).setSyncable(true));

	public final Holder<Attribute> VOIDIC_INFUSION = RegUtil.register(Registries.ATTRIBUTE, "voidic_infusion",
		() -> new PercentageAttribute(namespaceUtils.prefixId("voidic_infusion"), 0F, 0F, 1F));

	public final Holder<Attribute> VOIDIC_INFUSION_RES = RegUtil.register(Registries.ATTRIBUTE, "voidic_infusion_res",
		() -> new PercentageAttribute(namespaceUtils.prefixId("voidic_infusion_res"), 0F, 0F, 1F).setSyncable(true));

	public final Holder<Attribute> VOIDIC_PARANOIA_RES = RegUtil.register(Registries.ATTRIBUTE, "voidic_paranoia_res",
		() -> new PercentageAttribute(namespaceUtils.prefixId("voidic_paranoia_res"), 0F, 0F, 1F).setSyncable(true));

	public final Holder<Attribute> VOIDIC_RES = RegUtil.register(Registries.ATTRIBUTE, "voidic_res",
		() -> new RangedAttribute(namespaceUtils.prefixId("voidic_res"), 0F, 0F, 2048F));

	public final Holder<Attribute> VOIDIC_DMG = RegUtil.register(Registries.ATTRIBUTE, "voidic_dmg",
		() -> new RangedAttribute(namespaceUtils.prefixId("voidic_dmg"), 0F, 0F, 2048F));

	public final Holder<Attribute> VOIDIC_ARROW_DMG = RegUtil.register(Registries.ATTRIBUTE, "voidic_arrow_dmg",
		() -> new RangedAttribute(namespaceUtils.prefixId("voidic_arrow_dmg"), 0F, 0F, 2048F));

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
		}));
	}

	public String getDraconicHealthId(EquipmentSlot slot) {
		return "draconic_health".concat(slot.getName().toLowerCase(Locale.ROOT));
	}

}
