package tamaized.voidscape.registry.tool.set;

import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.regutil.*;
import tamaized.voidscape.registry.ModAttributes;
import tamaized.voidscape.registry.ModItemComponents;
import tamaized.voidscape.registry.ModItemProperties;
import tamaized.voidscape.registry.tool.ModToolMaterials;

@Component
public class VoidicCrystalToolSet {

	public final DeferredHolder<Item, Item> VOIDIC_CRYSTAL_SWORD;
	public final DeferredHolder<Item, Item> VOIDIC_CRYSTAL_BOW;
	public final DeferredHolder<Item, Item> VOIDIC_CRYSTAL_XBOW;
	public final DeferredHolder<Item, Item> VOIDIC_CRYSTAL_SHIELD;
	public final DeferredHolder<Item, Item> VOIDIC_CRYSTAL_AXE;
	public final DeferredHolder<Item, Item> VOIDIC_CRYSTAL_PICKAXE;

	public VoidicCrystalToolSet(
		@Autowired ToolAndArmorHelper toolAndArmorHelper,
		@Autowired AttributeFactoryProvider attributeFactoryProvider,
		@Autowired ModToolMaterials toolTiers,
		@Autowired ModItemProperties itemProperties,
		@Autowired ModAttributes attributes,
		@Autowired ModItemComponents itemComponents
	) {
		final String MATERIAL_NAME = "voidic_crystal";

		VOIDIC_CRYSTAL_SWORD = toolAndArmorHelper.sword(
			MATERIAL_NAME,
			toolTiers.VOIDIC_CRYSTAL,
			itemProperties.LAVA_IMMUNE,
			attributeFactoryProvider.make(
				() -> AttributeData.make(attributes.VOIDIC_DMG, AttributeModifier.Operation.ADD_VALUE, 1D, EquipmentSlotGroup.MAINHAND),
				() -> AttributeData.make(stack -> stack.getOrDefault(itemComponents.FANG, false), attributes.VOIDIC_INFUSION, AttributeModifier.Operation.ADD_VALUE, 0.15D, EquipmentSlotGroup.MAINHAND)
			),
			ExtraTooltipContext.EMPTY
		);

		VOIDIC_CRYSTAL_BOW = toolAndArmorHelper.bow(
			MATERIAL_NAME,
			toolTiers.VOIDIC_CRYSTAL,
			itemProperties.LAVA_IMMUNE,
			attributeFactoryProvider.make(
				() -> AttributeData.make(attributes.VOIDIC_ARROW_DMG, AttributeModifier.Operation.ADD_VALUE, 1D, EquipmentSlotGroup.MAINHAND),
				() -> AttributeData.make(stack -> stack.getOrDefault(itemComponents.FANG, false), attributes.VOIDIC_INFUSION, AttributeModifier.Operation.ADD_VALUE, 0.15D, EquipmentSlotGroup.MAINHAND)
			),
			ExtraTooltipContext.EMPTY
		);

		VOIDIC_CRYSTAL_XBOW = toolAndArmorHelper.xbow(
			MATERIAL_NAME,
			toolTiers.VOIDIC_CRYSTAL,
			itemProperties.LAVA_IMMUNE,
			attributeFactoryProvider.make(
				() -> AttributeData.make(attributes.VOIDIC_ARROW_DMG, AttributeModifier.Operation.ADD_VALUE, 1D, EquipmentSlotGroup.MAINHAND),
				() -> AttributeData.make(stack -> stack.getOrDefault(itemComponents.FANG, false), attributes.VOIDIC_INFUSION, AttributeModifier.Operation.ADD_VALUE, 0.15D, EquipmentSlotGroup.MAINHAND)
			),
			ExtraTooltipContext.EMPTY
		);

		VOIDIC_CRYSTAL_SHIELD = toolAndArmorHelper.shield(
			MATERIAL_NAME,
			toolTiers.VOIDIC_CRYSTAL,
			itemProperties.LAVA_IMMUNE,
			attributeFactoryProvider.make(
				() -> AttributeData.make(attributes.VOIDIC_RES, AttributeModifier.Operation.ADD_VALUE, 1D, EquipmentSlotGroup.OFFHAND)
			),
			ExtraTooltipContext.EMPTY
		);

		VOIDIC_CRYSTAL_AXE = toolAndArmorHelper.axe(
			MATERIAL_NAME,
			toolTiers.VOIDIC_CRYSTAL,
			itemProperties.LAVA_IMMUNE,
			attributeFactoryProvider.make(
				() -> AttributeData.make(attributes.VOIDIC_DMG, AttributeModifier.Operation.ADD_VALUE, 2D, EquipmentSlotGroup.MAINHAND),
				() -> AttributeData.make(stack -> stack.getOrDefault(itemComponents.FANG, false), attributes.VOIDIC_INFUSION, AttributeModifier.Operation.ADD_VALUE, 0.15D, EquipmentSlotGroup.MAINHAND)
			),
			ExtraTooltipContext.EMPTY
		);

		VOIDIC_CRYSTAL_PICKAXE = toolAndArmorHelper.pickaxe(
			MATERIAL_NAME,
			toolTiers.VOIDIC_CRYSTAL,
			itemProperties.LAVA_IMMUNE,
			attributeFactoryProvider.make(
				() -> AttributeData.make(attributes.VOIDIC_DMG, AttributeModifier.Operation.ADD_VALUE, 1D, EquipmentSlotGroup.MAINHAND)
			),
			ExtraTooltipContext.EMPTY
		);
	}

}
