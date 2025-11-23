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

@Component
public class CorruptArmorSet {

	@Autowired
	private ModAttributes attributes;

	@Autowired
	private ModArmorMaterials armorMaterials;

	@Autowired
	private ModItemProperties itemProperties;

	public final DeferredHolder<Item, Item> CORRUPT_HELMET = RegUtil.ToolAndArmorHelper.helmet(
		"corrupt",
		() -> armorMaterials.CORRUPT,
		() -> itemProperties.LAVA_IMMUNE.get(),
		AttributeFactory.make(
			() -> AttributeData.make(attributes.VOIDIC_RES, AttributeModifier.Operation.ADD_VALUE, 2D, EquipmentSlotGroup.HEAD),
			() -> AttributeData.make(attributes.VOIDIC_INFUSION_RES, AttributeModifier.Operation.ADD_VALUE, 0.10D, EquipmentSlotGroup.HEAD),
			() -> AttributeData.make(attributes.VOIDIC_PARANOIA_RES, AttributeModifier.Operation.ADD_VALUE, 0.25D, EquipmentSlotGroup.HEAD),
			() -> AttributeData.make(attributes.VOIDIC_VISIBILITY, AttributeModifier.Operation.ADD_VALUE, 0.15D, EquipmentSlotGroup.HEAD)
		),
		RegUtil.ToolAndArmorHelper.TooltipContext.EMPTY
	);

	public final DeferredHolder<Item, Item> CORRUPT_CHEST = RegUtil.ToolAndArmorHelper.chest(
		"corrupt",
		() -> armorMaterials.CORRUPT,
		() -> itemProperties.LAVA_IMMUNE.get(),
		AttributeFactory.make(
			() -> AttributeData.make(attributes.VOIDIC_RES, AttributeModifier.Operation.ADD_VALUE, 2D, EquipmentSlotGroup.CHEST),
			() -> AttributeData.make(attributes.VOIDIC_INFUSION_RES, AttributeModifier.Operation.ADD_VALUE, 0.10D, EquipmentSlotGroup.CHEST),
			() -> AttributeData.make(attributes.VOIDIC_PARANOIA_RES, AttributeModifier.Operation.ADD_VALUE, 0.25D, EquipmentSlotGroup.CHEST)
		),
		(stack, tick) -> true,
		RegUtil.ToolAndArmorHelper.TooltipContext.EMPTY
	);

	public final DeferredHolder<Item, Item> CORRUPT_LEGS = RegUtil.ToolAndArmorHelper.legs(
		"corrupt",
		() -> armorMaterials.CORRUPT,
		() -> itemProperties.LAVA_IMMUNE.get(),
		AttributeFactory.make(
			() -> AttributeData.make(attributes.VOIDIC_RES, AttributeModifier.Operation.ADD_VALUE, 2D, EquipmentSlotGroup.LEGS),
			() -> AttributeData.make(attributes.VOIDIC_INFUSION_RES, AttributeModifier.Operation.ADD_VALUE, 0.10D, EquipmentSlotGroup.LEGS),
			() -> AttributeData.make(attributes.VOIDIC_PARANOIA_RES, AttributeModifier.Operation.ADD_VALUE, 0.25D, EquipmentSlotGroup.LEGS)
		),
		RegUtil.ToolAndArmorHelper.TooltipContext.EMPTY
	);

	public final DeferredHolder<Item, Item> CORRUPT_BOOTS = RegUtil.ToolAndArmorHelper.boots(
		"corrupt",
		() -> armorMaterials.CORRUPT,
		() -> itemProperties.LAVA_IMMUNE.get(),
		AttributeFactory.make(
			() -> AttributeData.make(attributes.VOIDIC_RES, AttributeModifier.Operation.ADD_VALUE, 2D, EquipmentSlotGroup.FEET),
			() -> AttributeData.make(attributes.VOIDIC_INFUSION_RES, AttributeModifier.Operation.ADD_VALUE, 0.10D, EquipmentSlotGroup.FEET),
			() -> AttributeData.make(attributes.VOIDIC_PARANOIA_RES, AttributeModifier.Operation.ADD_VALUE, 0.25D, EquipmentSlotGroup.FEET)
		),
		RegUtil.ToolAndArmorHelper.TooltipContext.EMPTY
	);

}
