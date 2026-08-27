package tamaized.voidscape.registry.armor.set;

import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.regutil.*;
import tamaized.voidscape.registry.ModItemComponents;
import tamaized.voidscape.registry.armor.ModArmorMaterials;
import tamaized.voidscape.registry.ModAttributes;
import tamaized.voidscape.registry.ModItemProperties;

@Component
public class IchorArmorSet {

	public final DeferredHolder<Item, Item> ICHOR_HELMET;
	public final DeferredHolder<Item, Item> ICHOR_CHEST;
	public final DeferredHolder<Item, Item> ICHOR_LEGS;
	public final DeferredHolder<Item, Item> ICHOR_BOOTS;

	public IchorArmorSet(
		@Autowired ToolAndArmorHelper toolAndArmorHelper,
		@Autowired AttributeFactoryProvider attributeFactoryProvider,
		@Autowired ModAttributes attributes,
		@Autowired ModArmorMaterials armorMaterials,
		@Autowired ModItemProperties itemProperties,
		@Autowired ModItemComponents itemComponents
	) {
		ICHOR_HELMET = toolAndArmorHelper.helmet(
			"ichor",
			armorMaterials.ICHOR,
			itemProperties.LAVA_IMMUNE,
			attributeFactoryProvider.make(
				() -> AttributeData.make(attributes.VOIDIC_RES, AttributeModifier.Operation.ADD_VALUE, 4D, EquipmentSlotGroup.HEAD),
				() -> AttributeData.make(attributes.VOIDIC_INFUSION_RES, AttributeModifier.Operation.ADD_VALUE, 0.17D, EquipmentSlotGroup.HEAD),
				() -> AttributeData.make(attributes.VOIDIC_PARANOIA_RES, AttributeModifier.Operation.ADD_VALUE, 0.25D, EquipmentSlotGroup.HEAD),
				() -> AttributeData.make(attributes.VOIDIC_VISIBILITY, AttributeModifier.Operation.ADD_VALUE, 0.17D, EquipmentSlotGroup.HEAD)
			),
			ExtraTooltipContext.EMPTY
		);

		ICHOR_CHEST = toolAndArmorHelper.chest(
			"ichor",
			armorMaterials.ICHOR,
			itemProperties.LAVA_IMMUNE,
			attributeFactoryProvider.make(
				() -> AttributeData.make(attributes.VOIDIC_RES, AttributeModifier.Operation.ADD_VALUE, 4D, EquipmentSlotGroup.CHEST),
				() -> AttributeData.make(attributes.VOIDIC_INFUSION_RES, AttributeModifier.Operation.ADD_VALUE, 0.17D, EquipmentSlotGroup.CHEST),
				() -> AttributeData.make(attributes.VOIDIC_PARANOIA_RES, AttributeModifier.Operation.ADD_VALUE, 0.25D, EquipmentSlotGroup.CHEST)
			),
			(stack, _) -> stack.getOrDefault(itemComponents.ELYTRA, false),
			ExtraTooltipContext.EMPTY
		);

		ICHOR_LEGS = toolAndArmorHelper.legs(
			"ichor",
			armorMaterials.ICHOR,
			itemProperties.LAVA_IMMUNE,
			attributeFactoryProvider.make(
				() -> AttributeData.make(attributes.VOIDIC_RES, AttributeModifier.Operation.ADD_VALUE, 4D, EquipmentSlotGroup.LEGS),
				() -> AttributeData.make(attributes.VOIDIC_INFUSION_RES, AttributeModifier.Operation.ADD_VALUE, 0.17D, EquipmentSlotGroup.LEGS),
				() -> AttributeData.make(attributes.VOIDIC_PARANOIA_RES, AttributeModifier.Operation.ADD_VALUE, 0.25D, EquipmentSlotGroup.LEGS)
			),
			ExtraTooltipContext.EMPTY
		);

		ICHOR_BOOTS = toolAndArmorHelper.boots(
			"ichor",
			armorMaterials.ICHOR,
			itemProperties.LAVA_IMMUNE,
			attributeFactoryProvider.make(
				() -> AttributeData.make(attributes.VOIDIC_RES, AttributeModifier.Operation.ADD_VALUE, 4D, EquipmentSlotGroup.FEET),
				() -> AttributeData.make(attributes.VOIDIC_INFUSION_RES, AttributeModifier.Operation.ADD_VALUE, 0.17D, EquipmentSlotGroup.FEET),
				() -> AttributeData.make(attributes.VOIDIC_PARANOIA_RES, AttributeModifier.Operation.ADD_VALUE, 0.25D, EquipmentSlotGroup.FEET)
			),
			ExtraTooltipContext.EMPTY
		);
	}

}
