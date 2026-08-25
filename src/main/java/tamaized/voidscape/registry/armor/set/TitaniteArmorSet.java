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
public class TitaniteArmorSet {

	public final DeferredHolder<Item, Item> TITANITE_HELMET;
	public final DeferredHolder<Item, Item> TITANITE_CHEST;
	public final DeferredHolder<Item, Item> TITANITE_LEGS;
	public final DeferredHolder<Item, Item> TITANITE_BOOTS;

	public TitaniteArmorSet(
		@Autowired ToolAndArmorHelper toolAndArmorHelper,
		@Autowired AttributeFactoryProvider attributeFactoryProvider,
		@Autowired ModAttributes attributes,
		@Autowired ModArmorMaterials armorMaterials,
		@Autowired ModItemProperties itemProperties,
		@Autowired ModItemComponents itemComponents
	) {


		TITANITE_HELMET = toolAndArmorHelper.helmet(
			"titanite",
			armorMaterials.TITANITE,
			itemProperties.LAVA_IMMUNE,
			attributeFactoryProvider.make(
				() -> AttributeData.make(attributes.VOIDIC_RES, AttributeModifier.Operation.ADD_VALUE, 3D, EquipmentSlotGroup.HEAD),
				() -> AttributeData.make(attributes.VOIDIC_INFUSION_RES, AttributeModifier.Operation.ADD_VALUE, 0.15D, EquipmentSlotGroup.HEAD),
				() -> AttributeData.make(attributes.VOIDIC_PARANOIA_RES, AttributeModifier.Operation.ADD_VALUE, 0.25D, EquipmentSlotGroup.HEAD),
				() -> AttributeData.make(attributes.VOIDIC_VISIBILITY, AttributeModifier.Operation.ADD_VALUE, 0.20D, EquipmentSlotGroup.HEAD)
			),
			ExtraTooltipContext.EMPTY
		);

		TITANITE_CHEST = toolAndArmorHelper.chest(
			"titanite",
			armorMaterials.TITANITE,
			itemProperties.LAVA_IMMUNE,
			attributeFactoryProvider.make(
				() -> AttributeData.make(attributes.VOIDIC_RES, AttributeModifier.Operation.ADD_VALUE, 3D, EquipmentSlotGroup.CHEST),
				() -> AttributeData.make(attributes.VOIDIC_INFUSION_RES, AttributeModifier.Operation.ADD_VALUE, 0.15D, EquipmentSlotGroup.CHEST),
				() -> AttributeData.make(attributes.VOIDIC_PARANOIA_RES, AttributeModifier.Operation.ADD_VALUE, 0.25D, EquipmentSlotGroup.CHEST)
			),
			(stack, _) -> stack.getOrDefault(itemComponents.ELYTRA, false),
			ExtraTooltipContext.EMPTY
		);

		TITANITE_LEGS = toolAndArmorHelper.legs(
			"titanite",
			armorMaterials.TITANITE,
			itemProperties.LAVA_IMMUNE,
			attributeFactoryProvider.make(
				() -> AttributeData.make(attributes.VOIDIC_RES, AttributeModifier.Operation.ADD_VALUE, 3D, EquipmentSlotGroup.LEGS),
				() -> AttributeData.make(attributes.VOIDIC_INFUSION_RES, AttributeModifier.Operation.ADD_VALUE, 0.15D, EquipmentSlotGroup.LEGS),
				() -> AttributeData.make(attributes.VOIDIC_PARANOIA_RES, AttributeModifier.Operation.ADD_VALUE, 0.25D, EquipmentSlotGroup.LEGS)
			),
			ExtraTooltipContext.EMPTY
		);

		TITANITE_BOOTS = toolAndArmorHelper.boots(
			"titanite",
			armorMaterials.TITANITE,
			itemProperties.LAVA_IMMUNE,
			attributeFactoryProvider.make(
				() -> AttributeData.make(attributes.VOIDIC_RES, AttributeModifier.Operation.ADD_VALUE, 3D, EquipmentSlotGroup.FEET),
				() -> AttributeData.make(attributes.VOIDIC_INFUSION_RES, AttributeModifier.Operation.ADD_VALUE, 0.15D, EquipmentSlotGroup.FEET),
				() -> AttributeData.make(attributes.VOIDIC_PARANOIA_RES, AttributeModifier.Operation.ADD_VALUE, 0.25D, EquipmentSlotGroup.FEET)
			),
			ExtraTooltipContext.EMPTY
		);
	}

}
