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
import tamaized.voidscape.registry.*;
import tamaized.voidscape.util.ArmorUtil;

@Component
public class VoidicCrystalArmorSet {

	@Autowired
	private ModAttributes attributes;

	@Autowired
	private ModArmorMaterials armorMaterials;

	@Autowired
	private ModItemProperties itemProperties;

	@Autowired
	private ArmorUtil armorUtil;

	public final DeferredHolder<Item, Item> VOIDIC_CRYSTAL_HELMET = RegUtil.ToolAndArmorHelper.helmet(
		"voidic_crystal",
		() -> armorMaterials.VOIDIC_CRYSTAL,
		() -> itemProperties.LAVA_IMMUNE.get(),
		AttributeFactory.make(
			() -> AttributeData.make(attributes.VOIDIC_RES, AttributeModifier.Operation.ADD_VALUE, 1D, EquipmentSlotGroup.HEAD),
			() -> AttributeData.make(attributes.VOIDIC_INFUSION_RES, AttributeModifier.Operation.ADD_MULTIPLIED_BASE, 0.05D, EquipmentSlotGroup.HEAD)
		),
		RegUtil.ToolAndArmorHelper.TooltipContext.EMPTY
	);

	public final DeferredHolder<Item, Item> VOIDIC_CRYSTAL_CHEST = RegUtil.ToolAndArmorHelper.chest(
		"voidic_crystal",
		() -> armorMaterials.VOIDIC_CRYSTAL,
		() -> itemProperties.LAVA_IMMUNE.get(),
		AttributeFactory.make(
			() -> AttributeData.make(attributes.VOIDIC_RES, AttributeModifier.Operation.ADD_VALUE, 1D, EquipmentSlotGroup.CHEST),
			() -> AttributeData.make(attributes.VOIDIC_INFUSION_RES, AttributeModifier.Operation.ADD_MULTIPLIED_BASE, 0.05D, EquipmentSlotGroup.CHEST)
		),
		(stack, tick) -> armorUtil.elytra(stack),
		RegUtil.ToolAndArmorHelper.TooltipContext.EMPTY
	);

	public final DeferredHolder<Item, Item> VOIDIC_CRYSTAL_LEGS = RegUtil.ToolAndArmorHelper.legs(
		"voidic_crystal",
		() -> armorMaterials.VOIDIC_CRYSTAL,
		() -> itemProperties.LAVA_IMMUNE.get(),
		AttributeFactory.make(
			() -> AttributeData.make(attributes.VOIDIC_RES, AttributeModifier.Operation.ADD_VALUE, 1D, EquipmentSlotGroup.LEGS),
			() -> AttributeData.make(attributes.VOIDIC_INFUSION_RES, AttributeModifier.Operation.ADD_MULTIPLIED_BASE, 0.05D, EquipmentSlotGroup.LEGS)
		),
		RegUtil.ToolAndArmorHelper.TooltipContext.EMPTY
	);

	public final DeferredHolder<Item, Item> VOIDIC_CRYSTAL_BOOTS = RegUtil.ToolAndArmorHelper.boots(
		"voidic_crystal",
		() -> armorMaterials.VOIDIC_CRYSTAL,
		() -> itemProperties.LAVA_IMMUNE.get(),
		AttributeFactory.make(
			() -> AttributeData.make(attributes.VOIDIC_RES, AttributeModifier.Operation.ADD_VALUE, 1D, EquipmentSlotGroup.FEET),
			() -> AttributeData.make(attributes.VOIDIC_INFUSION_RES, AttributeModifier.Operation.ADD_MULTIPLIED_BASE, 0.05D, EquipmentSlotGroup.FEET)
		),
		RegUtil.ToolAndArmorHelper.TooltipContext.EMPTY
	);

}
