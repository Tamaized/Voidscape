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
public class ShroudArmorSet {

	public final DeferredHolder<Item, Item> SHROUD_HELMET;
	public final DeferredHolder<Item, Item> SHROUD_CHEST;
	public final DeferredHolder<Item, Item> SHROUD_LEGS;

	public ShroudArmorSet(
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


		SHROUD_HELMET = toolAndArmorHelper.helmet(
			"shroud",
			armorMaterials.SHROUD,
			itemProperties.LAVA_IMMUNE,
			attributeFactoryProvider.make(
				() -> AttributeData.make(attributes.SHROUDED, AttributeModifier.Operation.ADD_VALUE, 0.25D, EquipmentSlotGroup.HEAD),
				() -> AttributeData.make(attributes.VOIDIC_RES, AttributeModifier.Operation.ADD_VALUE, 7D, EquipmentSlotGroup.HEAD),
				() -> AttributeData.make(attributes.VOIDIC_INFUSION_RES, AttributeModifier.Operation.ADD_VALUE, 0.225D, EquipmentSlotGroup.HEAD),
				() -> AttributeData.make(attributes.VOIDIC_PARANOIA_RES, AttributeModifier.Operation.ADD_VALUE, 0.25D, EquipmentSlotGroup.HEAD),
				() -> AttributeData.make(attributes.VOIDIC_VISIBILITY, AttributeModifier.Operation.ADD_VALUE, 0.35D, EquipmentSlotGroup.HEAD),
				() -> AttributeData.make(stack -> stack.getOrDefault(itemComponents.DRACONIC, false), Attributes.MAX_HEALTH, attributes.getDraconicHealthId(EquipmentSlot.HEAD), AttributeModifier.Operation.ADD_VALUE, 7D, EquipmentSlotGroup.HEAD)
			),
			TOOLTIP
		);

		SHROUD_CHEST = toolAndArmorHelper.chest(
			"shroud",
			armorMaterials.SHROUD,
			itemProperties.LAVA_IMMUNE,
			attributeFactoryProvider.make(
				() -> AttributeData.make(attributes.SHROUDED, AttributeModifier.Operation.ADD_VALUE, 0.25D, EquipmentSlotGroup.CHEST),
				() -> AttributeData.make(attributes.VOIDIC_RES, AttributeModifier.Operation.ADD_VALUE, 7D, EquipmentSlotGroup.CHEST),
				() -> AttributeData.make(attributes.VOIDIC_INFUSION_RES, AttributeModifier.Operation.ADD_VALUE, 0.225D, EquipmentSlotGroup.CHEST),
				() -> AttributeData.make(attributes.VOIDIC_PARANOIA_RES, AttributeModifier.Operation.ADD_VALUE, 0.25D, EquipmentSlotGroup.CHEST),
				() -> AttributeData.make(stack -> stack.getOrDefault(itemComponents.DRACONIC, false), Attributes.MAX_HEALTH, attributes.getDraconicHealthId(EquipmentSlot.CHEST), AttributeModifier.Operation.ADD_VALUE, 7D, EquipmentSlotGroup.CHEST)
			),
			(stack, tick) -> stack.getOrDefault(itemComponents.ELYTRA, false),
			TOOLTIP
		);

		SHROUD_LEGS = toolAndArmorHelper.legs(
			"shroud",
			armorMaterials.SHROUD,
			itemProperties.LAVA_IMMUNE,
			attributeFactoryProvider.make(
				() -> AttributeData.make(attributes.SHROUDED, AttributeModifier.Operation.ADD_VALUE, 0.25D, EquipmentSlotGroup.LEGS),
				() -> AttributeData.make(attributes.VOIDIC_RES, AttributeModifier.Operation.ADD_VALUE, 7D, EquipmentSlotGroup.LEGS),
				() -> AttributeData.make(attributes.VOIDIC_INFUSION_RES, AttributeModifier.Operation.ADD_VALUE, 0.225D, EquipmentSlotGroup.LEGS),
				() -> AttributeData.make(attributes.VOIDIC_PARANOIA_RES, AttributeModifier.Operation.ADD_VALUE, 0.25D, EquipmentSlotGroup.LEGS),
				() -> AttributeData.make(stack -> stack.getOrDefault(itemComponents.DRACONIC, false), Attributes.MAX_HEALTH, attributes.getDraconicHealthId(EquipmentSlot.LEGS), AttributeModifier.Operation.ADD_VALUE, 7D, EquipmentSlotGroup.LEGS)
			),
			TOOLTIP
		);

	}

}
