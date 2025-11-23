package tamaized.voidscape.registry.armor.set;

import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.regutil.AttributeData;
import tamaized.regutil.AttributeFactory;
import tamaized.regutil.RegUtil;
import tamaized.voidscape.registry.ModItemComponents;
import tamaized.voidscape.registry.armor.ModArmorMaterials;
import tamaized.voidscape.registry.ModAttributes;
import tamaized.voidscape.registry.ModItemProperties;

@Component
public class IchorArmorSet {

	@Autowired
	private ModAttributes attributes;

	@Autowired
	private ModArmorMaterials armorMaterials;

	@Autowired
	private ModItemProperties itemProperties;

	@Autowired
	private ModItemComponents itemComponents;

	public final DeferredHolder<Item, Item> ICHOR_HELMET = RegUtil.ToolAndArmorHelper.helmet(
		"ichor",
		() -> armorMaterials.ICHOR,
		() -> itemProperties.LAVA_IMMUNE.get(),
		AttributeFactory.make(
			() -> AttributeData.make(attributes.VOIDIC_RES, AttributeModifier.Operation.ADD_VALUE, 4D, EquipmentSlotGroup.HEAD),
			() -> AttributeData.make(attributes.VOIDIC_INFUSION_RES, AttributeModifier.Operation.ADD_VALUE, 0.17D, EquipmentSlotGroup.HEAD),
			() -> AttributeData.make(attributes.VOIDIC_PARANOIA_RES, AttributeModifier.Operation.ADD_VALUE, 0.25D, EquipmentSlotGroup.HEAD),
			() -> AttributeData.make(attributes.VOIDIC_VISIBILITY, AttributeModifier.Operation.ADD_VALUE, 0.25D, EquipmentSlotGroup.HEAD)
		),
		RegUtil.ToolAndArmorHelper.TooltipContext.EMPTY
	);

	public final DeferredHolder<Item, Item> ICHOR_CHEST = RegUtil.ToolAndArmorHelper.chest(
		"ichor",
		() -> armorMaterials.ICHOR,
		() -> itemProperties.LAVA_IMMUNE.get(),
		AttributeFactory.make(
			() -> AttributeData.make(attributes.VOIDIC_RES, AttributeModifier.Operation.ADD_VALUE, 4D, EquipmentSlotGroup.CHEST),
			() -> AttributeData.make(attributes.VOIDIC_INFUSION_RES, AttributeModifier.Operation.ADD_VALUE, 0.17D, EquipmentSlotGroup.CHEST),
			() -> AttributeData.make(attributes.VOIDIC_PARANOIA_RES, AttributeModifier.Operation.ADD_VALUE, 0.25D, EquipmentSlotGroup.CHEST)
		),
		(stack, tick) -> stack.getOrDefault(itemComponents.ELYTRA, false),
		RegUtil.ToolAndArmorHelper.TooltipContext.EMPTY
	);

	public final DeferredHolder<Item, Item> ICHOR_LEGS = RegUtil.ToolAndArmorHelper.legs(
		"ichor",
		() -> armorMaterials.ICHOR,
		() -> itemProperties.LAVA_IMMUNE.get(),
		AttributeFactory.make(
			() -> AttributeData.make(attributes.VOIDIC_RES, AttributeModifier.Operation.ADD_VALUE, 4D, EquipmentSlotGroup.LEGS),
			() -> AttributeData.make(attributes.VOIDIC_INFUSION_RES, AttributeModifier.Operation.ADD_VALUE, 0.17D, EquipmentSlotGroup.LEGS),
			() -> AttributeData.make(attributes.VOIDIC_PARANOIA_RES, AttributeModifier.Operation.ADD_VALUE, 0.25D, EquipmentSlotGroup.LEGS)
		),
		RegUtil.ToolAndArmorHelper.TooltipContext.EMPTY
	);

	public final DeferredHolder<Item, Item> ICHOR_BOOTS = RegUtil.ToolAndArmorHelper.boots(
		"ichor",
		() -> armorMaterials.ICHOR,
		() -> itemProperties.LAVA_IMMUNE.get(),
		AttributeFactory.make(
			() -> AttributeData.make(attributes.VOIDIC_RES, AttributeModifier.Operation.ADD_VALUE, 4D, EquipmentSlotGroup.FEET),
			() -> AttributeData.make(attributes.VOIDIC_INFUSION_RES, AttributeModifier.Operation.ADD_VALUE, 0.17D, EquipmentSlotGroup.FEET),
			() -> AttributeData.make(attributes.VOIDIC_PARANOIA_RES, AttributeModifier.Operation.ADD_VALUE, 0.25D, EquipmentSlotGroup.FEET)
		),
		RegUtil.ToolAndArmorHelper.TooltipContext.EMPTY
	);

}
