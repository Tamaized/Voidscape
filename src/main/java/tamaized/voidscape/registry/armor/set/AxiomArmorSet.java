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
import tamaized.regutil.AttributeData;
import tamaized.regutil.AttributeFactoryProvider;
import tamaized.regutil.ExtraTooltipContext;
import tamaized.regutil.ToolAndArmorHelper;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.registry.ModAttributes;
import tamaized.voidscape.registry.ModItemComponents;
import tamaized.voidscape.registry.ModItemProperties;
import tamaized.voidscape.registry.armor.ModArmorMaterials;

import java.util.function.Consumer;

@Component
public class AxiomArmorSet {

	public final DeferredHolder<Item, Item> AXIOM_CHEST;
	public final DeferredHolder<Item, Item> AXIOM_LEGS;
	public final DeferredHolder<Item, Item> AXIOM_BOOTS;

	public AxiomArmorSet(
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
			tooltipContext.tooltip().accept(net.minecraft.network.chat.Component.empty());
			tooltipContext.tooltip().accept(net.minecraft.network.chat.Component
				.translatable(Voidscape.MODID + ".tooltip.axiomatic")
				.withStyle(ChatFormatting.AQUA)
				.withStyle(ChatFormatting.ITALIC)
			);
		};

		AXIOM_CHEST = toolAndArmorHelper.chest(
			"axiom",
			armorMaterials.AXIOM,
			itemProperties.LAVA_IMMUNE,
			attributeFactoryProvider.make(
				() -> AttributeData.make(attributes.AXIOMATIC, AttributeModifier.Operation.ADD_VALUE, 0.25D, EquipmentSlotGroup.CHEST),
				() -> AttributeData.make(attributes.VOIDIC_RES, AttributeModifier.Operation.ADD_VALUE, 6D, EquipmentSlotGroup.CHEST),
				() -> AttributeData.make(attributes.VOIDIC_INFUSION_RES, AttributeModifier.Operation.ADD_VALUE, 0.225D, EquipmentSlotGroup.CHEST),
				() -> AttributeData.make(attributes.VOIDIC_PARANOIA_RES, AttributeModifier.Operation.ADD_VALUE, 0.25D, EquipmentSlotGroup.CHEST),
				() -> AttributeData.make(stack -> stack.getOrDefault(itemComponents.DRACONIC, false), Attributes.MAX_HEALTH, attributes.getDraconicHealthId(EquipmentSlot.CHEST), AttributeModifier.Operation.ADD_VALUE, 6D, EquipmentSlotGroup.CHEST)
			),
			(stack, _) -> stack.getOrDefault(itemComponents.ELYTRA, false),
			TOOLTIP
		);

		AXIOM_LEGS = toolAndArmorHelper.legs(
			"axiom",
			armorMaterials.AXIOM,
			itemProperties.LAVA_IMMUNE,
			attributeFactoryProvider.make(
				() -> AttributeData.make(attributes.AXIOMATIC, AttributeModifier.Operation.ADD_VALUE, 0.25D, EquipmentSlotGroup.LEGS),
				() -> AttributeData.make(attributes.VOIDIC_RES, AttributeModifier.Operation.ADD_VALUE, 6D, EquipmentSlotGroup.LEGS),
				() -> AttributeData.make(attributes.VOIDIC_INFUSION_RES, AttributeModifier.Operation.ADD_VALUE, 0.225D, EquipmentSlotGroup.LEGS),
				() -> AttributeData.make(attributes.VOIDIC_PARANOIA_RES, AttributeModifier.Operation.ADD_VALUE, 0.25D, EquipmentSlotGroup.LEGS),
				() -> AttributeData.make(stack -> stack.getOrDefault(itemComponents.DRACONIC, false), Attributes.MAX_HEALTH, attributes.getDraconicHealthId(EquipmentSlot.LEGS), AttributeModifier.Operation.ADD_VALUE, 6D, EquipmentSlotGroup.LEGS)
			),
			TOOLTIP
		);

		AXIOM_BOOTS = toolAndArmorHelper.boots(
			"axiom",
			armorMaterials.AXIOM,
			itemProperties.LAVA_IMMUNE,
			attributeFactoryProvider.make(
				() -> AttributeData.make(attributes.AXIOMATIC, AttributeModifier.Operation.ADD_VALUE, 0.25D, EquipmentSlotGroup.FEET),
				() -> AttributeData.make(attributes.VOIDIC_RES, AttributeModifier.Operation.ADD_VALUE, 6D, EquipmentSlotGroup.FEET),
				() -> AttributeData.make(attributes.VOIDIC_INFUSION_RES, AttributeModifier.Operation.ADD_VALUE, 0.225D, EquipmentSlotGroup.FEET),
				() -> AttributeData.make(attributes.VOIDIC_PARANOIA_RES, AttributeModifier.Operation.ADD_VALUE, 0.25D, EquipmentSlotGroup.FEET),
				() -> AttributeData.make(attributes.VOIDIC_VISIBILITY, AttributeModifier.Operation.ADD_VALUE, 0.35D, EquipmentSlotGroup.FEET),
				() -> AttributeData.make(stack -> stack.getOrDefault(itemComponents.DRACONIC, false), Attributes.MAX_HEALTH, attributes.getDraconicHealthId(EquipmentSlot.FEET), AttributeModifier.Operation.ADD_VALUE, 6D, EquipmentSlotGroup.FEET)
			),
			TOOLTIP
		);

	}

}
