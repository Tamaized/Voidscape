package tamaized.voidscape.registry.armor.set;

import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.regutil.*;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.registry.ModItemComponents;
import tamaized.voidscape.registry.armor.ModArmorMaterials;
import tamaized.voidscape.registry.ModAttributes;
import tamaized.voidscape.registry.ModItemProperties;

import java.util.function.Consumer;

@Component
public class AstralArmorSet {

	public final DeferredHolder<Item, Item> ASTRAL_HELMET;
	public final DeferredHolder<Item, Item> ASTRAL_CHEST;
	public final DeferredHolder<Item, Item> ASTRAL_LEGS;
	public final DeferredHolder<Item, Item> ASTRAL_BOOTS;

	public AstralArmorSet(
		@Autowired ToolAndArmorHelper toolAndArmorHelper,
		@Autowired AttributeFactoryProvider attributeFactoryProvider,
		@Autowired ModAttributes attributes,
		@Autowired ModArmorMaterials armorMaterials,
		@Autowired ModItemProperties itemProperties,
		@Autowired ModItemComponents itemComponents
	) {
		final Consumer<ExtraTooltipContext> TOOLTIP = tooltipContext -> {
			if (tooltipContext.stack().getOrDefault(itemComponents.DRACONIC, false))
				tooltipContext.tooltip().accept(net.minecraft.network.chat.Component
					.translatable(Voidscape.MODID + ".tooltip.draconic")
					.withStyle(ChatFormatting.LIGHT_PURPLE)
				);
		};


		ASTRAL_HELMET = toolAndArmorHelper.helmet(
			"astral",
			armorMaterials.ASTRAL,
			itemProperties.LAVA_IMMUNE,
			attributeFactoryProvider.make(
				() -> AttributeData.make(attributes.VOIDIC_RES, AttributeModifier.Operation.ADD_VALUE, 5D, EquipmentSlotGroup.HEAD),
				() -> AttributeData.make(attributes.VOIDIC_INFUSION_RES, AttributeModifier.Operation.ADD_VALUE, 0.20D, EquipmentSlotGroup.HEAD),
				() -> AttributeData.make(attributes.VOIDIC_PARANOIA_RES, AttributeModifier.Operation.ADD_VALUE, 0.25D, EquipmentSlotGroup.HEAD),
				() -> AttributeData.make(attributes.VOIDIC_VISIBILITY, AttributeModifier.Operation.ADD_VALUE, 0.30D, EquipmentSlotGroup.HEAD),
				() -> AttributeData.make(stack -> stack.getOrDefault(itemComponents.DRACONIC, false), Attributes.MAX_HEALTH, attributes.getDraconicHealthId(EquipmentSlot.HEAD), AttributeModifier.Operation.ADD_VALUE, 5D, EquipmentSlotGroup.HEAD)
			),
			TOOLTIP
		);

		ASTRAL_CHEST = toolAndArmorHelper.chest(
			"astral",
			armorMaterials.ASTRAL,
			itemProperties.LAVA_IMMUNE,
			attributeFactoryProvider.make(
				() -> AttributeData.make(attributes.VOIDIC_RES, AttributeModifier.Operation.ADD_VALUE, 5D, EquipmentSlotGroup.CHEST),
				() -> AttributeData.make(attributes.VOIDIC_INFUSION_RES, AttributeModifier.Operation.ADD_VALUE, 0.20D, EquipmentSlotGroup.CHEST),
				() -> AttributeData.make(attributes.VOIDIC_PARANOIA_RES, AttributeModifier.Operation.ADD_VALUE, 0.25D, EquipmentSlotGroup.CHEST),
				() -> AttributeData.make(stack -> stack.getOrDefault(itemComponents.DRACONIC, false), Attributes.MAX_HEALTH, attributes.getDraconicHealthId(EquipmentSlot.CHEST), AttributeModifier.Operation.ADD_VALUE, 5D, EquipmentSlotGroup.CHEST)
			),
			(stack, tick) -> stack.getOrDefault(itemComponents.ELYTRA, false),
			TOOLTIP
		);

		ASTRAL_LEGS = toolAndArmorHelper.legs(
			"astral",
			armorMaterials.ASTRAL,
			itemProperties.LAVA_IMMUNE,
			attributeFactoryProvider.make(
				() -> AttributeData.make(attributes.VOIDIC_RES, AttributeModifier.Operation.ADD_VALUE, 5D, EquipmentSlotGroup.LEGS),
				() -> AttributeData.make(attributes.VOIDIC_INFUSION_RES, AttributeModifier.Operation.ADD_VALUE, 0.20D, EquipmentSlotGroup.LEGS),
				() -> AttributeData.make(attributes.VOIDIC_PARANOIA_RES, AttributeModifier.Operation.ADD_VALUE, 0.25D, EquipmentSlotGroup.LEGS),
				() -> AttributeData.make(stack -> stack.getOrDefault(itemComponents.DRACONIC, false), Attributes.MAX_HEALTH, attributes.getDraconicHealthId(EquipmentSlot.LEGS), AttributeModifier.Operation.ADD_VALUE, 5D, EquipmentSlotGroup.LEGS)
			),
			TOOLTIP
		);

		ASTRAL_BOOTS = toolAndArmorHelper.boots(
			"astral",
			armorMaterials.ASTRAL,
			itemProperties.LAVA_IMMUNE,
			attributeFactoryProvider.make(
				() -> AttributeData.make(attributes.VOIDIC_RES, AttributeModifier.Operation.ADD_VALUE, 5D, EquipmentSlotGroup.FEET),
				() -> AttributeData.make(attributes.VOIDIC_INFUSION_RES, AttributeModifier.Operation.ADD_VALUE, 0.20D, EquipmentSlotGroup.FEET),
				() -> AttributeData.make(attributes.VOIDIC_PARANOIA_RES, AttributeModifier.Operation.ADD_VALUE, 0.25D, EquipmentSlotGroup.FEET),
				() -> AttributeData.make(stack -> stack.getOrDefault(itemComponents.DRACONIC, false), Attributes.MAX_HEALTH, attributes.getDraconicHealthId(EquipmentSlot.FEET), AttributeModifier.Operation.ADD_VALUE, 5D, EquipmentSlotGroup.FEET)
			),
			TOOLTIP
		);
	}

}
