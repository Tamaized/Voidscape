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
import tamaized.voidscape.registry.tool.ExtraToolTypes;
import tamaized.voidscape.registry.tool.ModToolMaterials;

@Component
public class TitaniteToolSet {

	public final DeferredHolder<Item, Item> TITANITE_HOE;
	public final DeferredHolder<Item, Item> TITANITE_SWORD;
	public final DeferredHolder<Item, Item> TITANITE_BOW;
	public final DeferredHolder<Item, Item> TITANITE_XBOW;
	public final DeferredHolder<Item, Item> TITANITE_AXE;
	public final DeferredHolder<Item, Item> TITANITE_PICKAXE;

	public TitaniteToolSet(
		@Autowired ToolAndArmorHelper toolAndArmorHelper,
		@Autowired AttributeFactoryProvider attributeFactoryProvider,
		@Autowired ModToolMaterials toolTiers,
		@Autowired ModItemProperties itemProperties,
		@Autowired ModAttributes attributes,
		@Autowired ModItemComponents itemComponents,
		@Autowired ExtraToolTypes extraToolTypes
	) {
		final String MATERIAL_NAME = "titanite";

		TITANITE_SWORD = toolAndArmorHelper.sword(
			MATERIAL_NAME,
			toolTiers.TITANITE,
			itemProperties.LAVA_IMMUNE,
			attributeFactoryProvider.make(
				() -> AttributeData.make(attributes.VOIDIC_DMG, AttributeModifier.Operation.ADD_VALUE, 3D, EquipmentSlotGroup.MAINHAND),
				() -> AttributeData.make(stack -> stack.getOrDefault(itemComponents.FANG, false), attributes.VOIDIC_INFUSION, AttributeModifier.Operation.ADD_VALUE, 0.15D, EquipmentSlotGroup.MAINHAND)
			),
			ExtraTooltipContext.EMPTY
		);

		TITANITE_BOW = toolAndArmorHelper.bow(
			MATERIAL_NAME,
			toolTiers.TITANITE,
			itemProperties.LAVA_IMMUNE,
			attributeFactoryProvider.make(
				() -> AttributeData.make(attributes.VOIDIC_ARROW_DMG, AttributeModifier.Operation.ADD_VALUE, 3D, EquipmentSlotGroup.MAINHAND),
				() -> AttributeData.make(stack -> stack.getOrDefault(itemComponents.FANG, false), attributes.VOIDIC_INFUSION, AttributeModifier.Operation.ADD_VALUE, 0.15D, EquipmentSlotGroup.MAINHAND)
			),
			ExtraTooltipContext.EMPTY
		);

		TITANITE_XBOW = toolAndArmorHelper.xbow(
			MATERIAL_NAME,
			toolTiers.TITANITE,
			itemProperties.LAVA_IMMUNE,
			attributeFactoryProvider.make(
				() -> AttributeData.make(attributes.VOIDIC_ARROW_DMG, AttributeModifier.Operation.ADD_VALUE, 3D, EquipmentSlotGroup.MAINHAND),
				() -> AttributeData.make(stack -> stack.getOrDefault(itemComponents.FANG, false), attributes.VOIDIC_INFUSION, AttributeModifier.Operation.ADD_VALUE, 0.15D, EquipmentSlotGroup.MAINHAND)
			),
			ExtraTooltipContext.EMPTY
		);

		TITANITE_AXE = toolAndArmorHelper.axe(
			MATERIAL_NAME,
			toolTiers.TITANITE,
			itemProperties.LAVA_IMMUNE,
			attributeFactoryProvider.make(
				() -> AttributeData.make(attributes.VOIDIC_DMG, AttributeModifier.Operation.ADD_VALUE, 4D, EquipmentSlotGroup.MAINHAND),
				() -> AttributeData.make(stack -> stack.getOrDefault(itemComponents.FANG, false), attributes.VOIDIC_INFUSION, AttributeModifier.Operation.ADD_VALUE, 0.15D, EquipmentSlotGroup.MAINHAND)
			),
			ExtraTooltipContext.EMPTY
		);

		TITANITE_PICKAXE = toolAndArmorHelper.pickaxe(
			MATERIAL_NAME,
			toolTiers.TITANITE,
			itemProperties.LAVA_IMMUNE,
			attributeFactoryProvider.make(
				() -> AttributeData.make(attributes.VOIDIC_DMG, AttributeModifier.Operation.ADD_VALUE, 2D, EquipmentSlotGroup.MAINHAND)
			),
			ExtraTooltipContext.EMPTY
		);

		TITANITE_HOE = extraToolTypes.hoeWithBonemeal(
			MATERIAL_NAME,
			toolTiers.TITANITE,
			itemProperties.LAVA_IMMUNE,
			attributeFactoryProvider.make(
				() -> AttributeData.make(attributes.VOIDIC_DMG, AttributeModifier.Operation.ADD_VALUE, 1D, EquipmentSlotGroup.MAINHAND)
			),
			ExtraTooltipContext.EMPTY
		);
	}

}
