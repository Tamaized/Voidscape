package tamaized.voidscape.registry.armor.set;

import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.regutil.AttributeData;
import tamaized.regutil.AttributeFactory;
import tamaized.regutil.RegUtil;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.registry.ModArmorMaterials;
import tamaized.voidscape.registry.ModAttributes;
import tamaized.voidscape.registry.ModItemProperties;
import tamaized.voidscape.util.ArmorUtil;

import java.util.function.Consumer;

@Component
public class AstralArmorSet {

	@Autowired
	private static ModAttributes attributes;

	@Autowired
	private static ModArmorMaterials armorMaterials;

	@Autowired
	private static ModItemProperties itemProperties;

	@Autowired
	private static ArmorUtil armorUtil;

	private final Consumer<RegUtil.ToolAndArmorHelper.TooltipContext> TOOLTIP = tooltipContext -> {
		if (armorUtil.draconic(tooltipContext.stack()))
			tooltipContext.tooltip().add(net.minecraft.network.chat.Component.translatable(Voidscape.MODID + ".tooltip.draconic").withStyle(ChatFormatting.LIGHT_PURPLE));
	};

	public final DeferredHolder<Item, Item> ASTRAL_HELMET = RegUtil.ToolAndArmorHelper.helmet(
		armorMaterials.ASTRAL,
		itemProperties.LAVA_IMMUNE.get(),
		AttributeFactory.make(
			AttributeData.make(attributes.VOIDIC_RES, AttributeModifier.Operation.ADD_VALUE, 5D, EquipmentSlotGroup.HEAD),
			AttributeData.make(attributes.VOIDIC_INFUSION_RES, AttributeModifier.Operation.ADD_MULTIPLIED_BASE, 0.20D, EquipmentSlotGroup.HEAD),
			AttributeData.make(attributes.VOIDIC_PARANOIA_RES, AttributeModifier.Operation.ADD_MULTIPLIED_BASE, 0.25D, EquipmentSlotGroup.HEAD),
			AttributeData.make(attributes.VOIDIC_VISIBILITY, AttributeModifier.Operation.ADD_MULTIPLIED_BASE, 0.30D, EquipmentSlotGroup.HEAD),
			AttributeData.make(armorUtil::draconic, Attributes.MAX_HEALTH, attributes.DRACONIC_HEALTH_ID, AttributeModifier.Operation.ADD_VALUE, 5D, EquipmentSlotGroup.HEAD)
		),
		TOOLTIP
	);

	public final DeferredHolder<Item, Item> ASTRAL_CHEST = RegUtil.ToolAndArmorHelper.chest(
		armorMaterials.ASTRAL,
		itemProperties.LAVA_IMMUNE.get(),
		AttributeFactory.make(
			AttributeData.make(attributes.VOIDIC_RES, AttributeModifier.Operation.ADD_VALUE, 5D, EquipmentSlotGroup.CHEST),
			AttributeData.make(attributes.VOIDIC_INFUSION_RES, AttributeModifier.Operation.ADD_MULTIPLIED_BASE, 0.20D, EquipmentSlotGroup.CHEST),
			AttributeData.make(attributes.VOIDIC_PARANOIA_RES, AttributeModifier.Operation.ADD_MULTIPLIED_BASE, 0.25D, EquipmentSlotGroup.CHEST)
		),
		(stack, tick) -> armorUtil.elytra(stack),
		TOOLTIP
	);

	public final DeferredHolder<Item, Item> ASTRAL_LEGS = RegUtil.ToolAndArmorHelper.legs(
		armorMaterials.ASTRAL,
		itemProperties.LAVA_IMMUNE.get(),
		AttributeFactory.make(
			AttributeData.make(attributes.VOIDIC_RES, AttributeModifier.Operation.ADD_VALUE, 5D, EquipmentSlotGroup.LEGS),
			AttributeData.make(attributes.VOIDIC_INFUSION_RES, AttributeModifier.Operation.ADD_MULTIPLIED_BASE, 0.20D, EquipmentSlotGroup.LEGS),
			AttributeData.make(attributes.VOIDIC_PARANOIA_RES, AttributeModifier.Operation.ADD_MULTIPLIED_BASE, 0.25D, EquipmentSlotGroup.LEGS)
		),
		TOOLTIP
	);

	public final DeferredHolder<Item, Item> ASTRAL_BOOTS = RegUtil.ToolAndArmorHelper.boots(
		armorMaterials.ASTRAL,
		itemProperties.LAVA_IMMUNE.get(),
		AttributeFactory.make(
			AttributeData.make(attributes.VOIDIC_RES, AttributeModifier.Operation.ADD_VALUE, 5D, EquipmentSlotGroup.FEET),
			AttributeData.make(attributes.VOIDIC_INFUSION_RES, AttributeModifier.Operation.ADD_MULTIPLIED_BASE, 0.20D, EquipmentSlotGroup.FEET),
			AttributeData.make(attributes.VOIDIC_PARANOIA_RES, AttributeModifier.Operation.ADD_MULTIPLIED_BASE, 0.25D, EquipmentSlotGroup.FEET)
		),
		TOOLTIP
	);

}
