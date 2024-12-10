package tamaized.voidscape.registry.tool.set;

import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.regutil.AttributeData;
import tamaized.regutil.AttributeFactory;
import tamaized.regutil.RegUtil;
import tamaized.voidscape.registry.ModAttributes;
import tamaized.voidscape.registry.ModItemProperties;
import tamaized.voidscape.registry.tool.ModToolTiers;
import tamaized.voidscape.util.ItemAugmentUtil;

@Component
public class IchorToolSet {

	private final String MATERIAL_NAME = "ichor";

	@Autowired
	private ModToolTiers toolTiers;

	@Autowired
	private ModItemProperties itemProperties;

	@Autowired
	private ModAttributes attributes;

	@Autowired
	private ItemAugmentUtil itemAugmentUtil;

	public final DeferredHolder<Item, Item> ICHOR_SWORD = RegUtil.ToolAndArmorHelper.sword(
		MATERIAL_NAME,
		() -> toolTiers.ICHOR,
		() -> itemProperties.LAVA_IMMUNE.get(),
		AttributeFactory.make(
			() -> AttributeData.make(attributes.VOIDIC_DMG, AttributeModifier.Operation.ADD_VALUE, 4D, EquipmentSlotGroup.MAINHAND),
			() -> AttributeData.make(itemAugmentUtil::fang, attributes.VOIDIC_INFUSION, AttributeModifier.Operation.ADD_MULTIPLIED_BASE, 0.15D, EquipmentSlotGroup.MAINHAND)
		),
		RegUtil.ToolAndArmorHelper.TooltipContext.EMPTY
	);

	public final DeferredHolder<Item, Item> ICHOR_BOW = RegUtil.ToolAndArmorHelper.bow(
		MATERIAL_NAME,
		() -> toolTiers.ICHOR,
		() -> itemProperties.LAVA_IMMUNE.get(),
		AttributeFactory.make(
			() -> AttributeData.make(attributes.VOIDIC_ARROW_DMG, AttributeModifier.Operation.ADD_VALUE, 4D, EquipmentSlotGroup.MAINHAND),
			() -> AttributeData.make(itemAugmentUtil::fang, attributes.VOIDIC_INFUSION, AttributeModifier.Operation.ADD_MULTIPLIED_BASE, 0.15D, EquipmentSlotGroup.MAINHAND)
		),
		RegUtil.ToolAndArmorHelper.TooltipContext.EMPTY
	);

	public final DeferredHolder<Item, Item> ICHOR_XBOW = RegUtil.ToolAndArmorHelper.xbow(
		MATERIAL_NAME,
		() -> toolTiers.ICHOR,
		() -> itemProperties.LAVA_IMMUNE.get(),
		AttributeFactory.make(
			() -> AttributeData.make(attributes.VOIDIC_ARROW_DMG, AttributeModifier.Operation.ADD_VALUE, 4D, EquipmentSlotGroup.MAINHAND),
			() -> AttributeData.make(itemAugmentUtil::fang, attributes.VOIDIC_INFUSION, AttributeModifier.Operation.ADD_MULTIPLIED_BASE, 0.15D, EquipmentSlotGroup.MAINHAND)
		),
		RegUtil.ToolAndArmorHelper.TooltipContext.EMPTY
	);

	public final DeferredHolder<Item, Item> ICHOR_AXE = RegUtil.ToolAndArmorHelper.axe(
		MATERIAL_NAME,
		() -> toolTiers.ICHOR,
		() -> itemProperties.LAVA_IMMUNE.get(),
		AttributeFactory.make(
			() -> AttributeData.make(attributes.VOIDIC_DMG, AttributeModifier.Operation.ADD_VALUE, 5D, EquipmentSlotGroup.MAINHAND),
			() -> AttributeData.make(itemAugmentUtil::fang, attributes.VOIDIC_INFUSION, AttributeModifier.Operation.ADD_MULTIPLIED_BASE, 0.15D, EquipmentSlotGroup.MAINHAND)
		),
		RegUtil.ToolAndArmorHelper.TooltipContext.EMPTY
	);

	public final DeferredHolder<Item, Item> ICHOR_PICKAXE = RegUtil.ToolAndArmorHelper.pickaxe(
		MATERIAL_NAME,
		() -> toolTiers.ICHOR,
		() -> itemProperties.LAVA_IMMUNE.get(),
		AttributeFactory.make(
			() -> AttributeData.make(attributes.VOIDIC_DMG, AttributeModifier.Operation.ADD_VALUE, 3D, EquipmentSlotGroup.MAINHAND)
		),
		RegUtil.ToolAndArmorHelper.TooltipContext.EMPTY
	);

}
