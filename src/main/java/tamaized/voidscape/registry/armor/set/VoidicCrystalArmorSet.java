package tamaized.voidscape.registry.armor.set;

import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.regutil.*;
import tamaized.voidscape.registry.*;
import tamaized.voidscape.registry.armor.ModArmorMaterials;

@Component
public class VoidicCrystalArmorSet {

	public final DeferredHolder<Item, Item> VOIDIC_CRYSTAL_HELMET;
	public final DeferredHolder<Item, Item> VOIDIC_CRYSTAL_CHEST;
	public final DeferredHolder<Item, Item> VOIDIC_CRYSTAL_LEGS;
	public final DeferredHolder<Item, Item> VOIDIC_CRYSTAL_BOOTS;

	public VoidicCrystalArmorSet(
		@Autowired ToolAndArmorHelper toolAndArmorHelper,
		@Autowired AttributeFactoryProvider attributeFactoryProvider,
		@Autowired ModAttributes attributes,
		@Autowired ModArmorMaterials armorMaterials,
		@Autowired ModItemProperties itemProperties,
		@Autowired ModItemComponents itemComponents
	) {
		VOIDIC_CRYSTAL_HELMET = toolAndArmorHelper.helmet(
			"voidic_crystal",
			armorMaterials.VOIDIC_CRYSTAL,
			itemProperties.LAVA_IMMUNE,
			attributeFactoryProvider.make(
				() -> AttributeData.make(attributes.VOIDIC_RES, AttributeModifier.Operation.ADD_VALUE, 1D, EquipmentSlotGroup.HEAD),
				() -> AttributeData.make(attributes.VOIDIC_INFUSION_RES, AttributeModifier.Operation.ADD_VALUE, 0.05D, EquipmentSlotGroup.HEAD)
			),
			ExtraTooltipContext.EMPTY
		);

		VOIDIC_CRYSTAL_CHEST = toolAndArmorHelper.chest(
			"voidic_crystal",
			armorMaterials.VOIDIC_CRYSTAL,
			itemProperties.LAVA_IMMUNE,
			attributeFactoryProvider.make(
				() -> AttributeData.make(attributes.VOIDIC_RES, AttributeModifier.Operation.ADD_VALUE, 1D, EquipmentSlotGroup.CHEST),
				() -> AttributeData.make(attributes.VOIDIC_INFUSION_RES, AttributeModifier.Operation.ADD_VALUE, 0.05D, EquipmentSlotGroup.CHEST)
			),
			(stack, _) -> stack.getOrDefault(itemComponents.ELYTRA, false),
			ExtraTooltipContext.EMPTY
		);

		VOIDIC_CRYSTAL_LEGS = toolAndArmorHelper.legs(
			"voidic_crystal",
			armorMaterials.VOIDIC_CRYSTAL,
			itemProperties.LAVA_IMMUNE,
			attributeFactoryProvider.make(
				() -> AttributeData.make(attributes.VOIDIC_RES, AttributeModifier.Operation.ADD_VALUE, 1D, EquipmentSlotGroup.LEGS),
				() -> AttributeData.make(attributes.VOIDIC_INFUSION_RES, AttributeModifier.Operation.ADD_VALUE, 0.05D, EquipmentSlotGroup.LEGS)
			),
			ExtraTooltipContext.EMPTY
		);

		VOIDIC_CRYSTAL_BOOTS = toolAndArmorHelper.boots(
			"voidic_crystal",
			armorMaterials.VOIDIC_CRYSTAL,
			itemProperties.LAVA_IMMUNE,
			attributeFactoryProvider.make(
				() -> AttributeData.make(attributes.VOIDIC_RES, AttributeModifier.Operation.ADD_VALUE, 1D, EquipmentSlotGroup.FEET),
				() -> AttributeData.make(attributes.VOIDIC_INFUSION_RES, AttributeModifier.Operation.ADD_VALUE, 0.05D, EquipmentSlotGroup.FEET)
			),
			ExtraTooltipContext.EMPTY
		);
	}

}
