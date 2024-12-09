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
import tamaized.voidscape.registry.armor.ModArmorMaterials;
import tamaized.voidscape.registry.ModAttributes;
import tamaized.voidscape.registry.ModItemProperties;
import tamaized.voidscape.util.ItemAugmentUtil;

@Component
public class TitaniteArmorSet {

	@Autowired
	private ModAttributes attributes;

	@Autowired
	private ModArmorMaterials armorMaterials;

	@Autowired
	private ModItemProperties itemProperties;

	@Autowired
	private ItemAugmentUtil itemAugmentUtil;

	public final DeferredHolder<Item, Item> TITANITE_HELMET = RegUtil.ToolAndArmorHelper.helmet(
		"titanite",
		() -> armorMaterials.TITANITE,
		() -> itemProperties.LAVA_IMMUNE.get(),
		AttributeFactory.make(
			() -> AttributeData.make(attributes.VOIDIC_RES, AttributeModifier.Operation.ADD_VALUE, 3D, EquipmentSlotGroup.HEAD),
			() -> AttributeData.make(attributes.VOIDIC_INFUSION_RES, AttributeModifier.Operation.ADD_MULTIPLIED_BASE, 0.15D, EquipmentSlotGroup.HEAD),
			() -> AttributeData.make(attributes.VOIDIC_PARANOIA_RES, AttributeModifier.Operation.ADD_MULTIPLIED_BASE, 0.25D, EquipmentSlotGroup.HEAD),
			() -> AttributeData.make(attributes.VOIDIC_VISIBILITY, AttributeModifier.Operation.ADD_MULTIPLIED_BASE, 0.20D, EquipmentSlotGroup.HEAD)
		),
		RegUtil.ToolAndArmorHelper.TooltipContext.EMPTY
	);

	public final DeferredHolder<Item, Item> TITANITE_CHEST = RegUtil.ToolAndArmorHelper.chest(
		"titanite",
		() -> armorMaterials.TITANITE,
		() -> itemProperties.LAVA_IMMUNE.get(),
		AttributeFactory.make(
			() -> AttributeData.make(attributes.VOIDIC_RES, AttributeModifier.Operation.ADD_VALUE, 3D, EquipmentSlotGroup.CHEST),
			() -> AttributeData.make(attributes.VOIDIC_INFUSION_RES, AttributeModifier.Operation.ADD_MULTIPLIED_BASE, 0.15D, EquipmentSlotGroup.CHEST),
			() -> AttributeData.make(attributes.VOIDIC_PARANOIA_RES, AttributeModifier.Operation.ADD_MULTIPLIED_BASE, 0.25D, EquipmentSlotGroup.CHEST)
		),
		(stack, tick) -> itemAugmentUtil.elytra(stack),
		RegUtil.ToolAndArmorHelper.TooltipContext.EMPTY
	);

	public final DeferredHolder<Item, Item> TITANITE_LEGS = RegUtil.ToolAndArmorHelper.legs(
		"titanite",
		() -> armorMaterials.TITANITE,
		() -> itemProperties.LAVA_IMMUNE.get(),
		AttributeFactory.make(
			() -> AttributeData.make(attributes.VOIDIC_RES, AttributeModifier.Operation.ADD_VALUE, 3D, EquipmentSlotGroup.LEGS),
			() -> AttributeData.make(attributes.VOIDIC_INFUSION_RES, AttributeModifier.Operation.ADD_MULTIPLIED_BASE, 0.15D, EquipmentSlotGroup.LEGS),
			() -> AttributeData.make(attributes.VOIDIC_PARANOIA_RES, AttributeModifier.Operation.ADD_MULTIPLIED_BASE, 0.25D, EquipmentSlotGroup.LEGS)
		),
		RegUtil.ToolAndArmorHelper.TooltipContext.EMPTY
	);

	public final DeferredHolder<Item, Item> TITANITE_BOOTS = RegUtil.ToolAndArmorHelper.boots(
		"titanite",
		() -> armorMaterials.TITANITE,
		() -> itemProperties.LAVA_IMMUNE.get(),
		AttributeFactory.make(
			() -> AttributeData.make(attributes.VOIDIC_RES, AttributeModifier.Operation.ADD_VALUE, 3D, EquipmentSlotGroup.FEET),
			() -> AttributeData.make(attributes.VOIDIC_INFUSION_RES, AttributeModifier.Operation.ADD_MULTIPLIED_BASE, 0.15D, EquipmentSlotGroup.FEET),
			() -> AttributeData.make(attributes.VOIDIC_PARANOIA_RES, AttributeModifier.Operation.ADD_MULTIPLIED_BASE, 0.25D, EquipmentSlotGroup.FEET)
		),
		RegUtil.ToolAndArmorHelper.TooltipContext.EMPTY
	);

}
