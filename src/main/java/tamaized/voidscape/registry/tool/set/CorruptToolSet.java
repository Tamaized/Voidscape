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
import tamaized.voidscape.registry.ModTools;
import tamaized.voidscape.registry.tool.ModToolTiers;
import tamaized.voidscape.util.ItemAugmentUtil;

@Component
public class CorruptToolSet {

	@Autowired
	private ModToolTiers toolTiers;

	@Autowired
	private ModItemProperties itemProperties;

	@Autowired
	private ModAttributes attributes;

	@Autowired
	private ItemAugmentUtil itemAugmentUtil;

	public final DeferredHolder<Item, Item> CORRUPT_SWORD = RegUtil.ToolAndArmorHelper.sword(
		"corrupt",
		() -> toolTiers.CORRUPT,
		() -> itemProperties.LAVA_IMMUNE.get(),
		AttributeFactory.make(
			() -> AttributeData.make(attributes.VOIDIC_DMG, AttributeModifier.Operation.ADD_VALUE, 2D, EquipmentSlotGroup.MAINHAND),
			() -> AttributeData.make(itemAugmentUtil::fang, attributes.VOIDIC_INFUSION, AttributeModifier.Operation.ADD_MULTIPLIED_BASE, 0.15D, EquipmentSlotGroup.MAINHAND)
		),
		RegUtil.ToolAndArmorHelper.TooltipContext.EMPTY
	);

	public final DeferredHolder<Item, Item> CORRUPT_BOW = RegUtil.ToolAndArmorHelper.bow(
		"corrupt",
		() -> toolTiers.CORRUPT,
		() -> itemProperties.LAVA_IMMUNE.get(),
		AttributeFactory.make(
			() -> AttributeData.make(attributes.VOIDIC_ARROW_DMG, AttributeModifier.Operation.ADD_VALUE, 2D, EquipmentSlotGroup.MAINHAND),
			() -> AttributeData.make(itemAugmentUtil::fang, attributes.VOIDIC_INFUSION, AttributeModifier.Operation.ADD_MULTIPLIED_BASE, 0.15D, EquipmentSlotGroup.MAINHAND)
		),
		RegUtil.ToolAndArmorHelper.TooltipContext.EMPTY
	);

	public final DeferredHolder<Item, Item> CORRUPT_XBOW = RegUtil.ToolAndArmorHelper.xbow(
		"corrupt",
		() -> toolTiers.CORRUPT,
		() -> itemProperties.LAVA_IMMUNE.get(),
		AttributeFactory.make(
			() -> AttributeData.make(attributes.VOIDIC_ARROW_DMG, AttributeModifier.Operation.ADD_VALUE, 2D, EquipmentSlotGroup.MAINHAND),
			() -> AttributeData.make(itemAugmentUtil::fang, attributes.VOIDIC_INFUSION, AttributeModifier.Operation.ADD_MULTIPLIED_BASE, 0.15D, EquipmentSlotGroup.MAINHAND)
		),
		RegUtil.ToolAndArmorHelper.TooltipContext.EMPTY
	);

	public final DeferredHolder<Item, Item> CORRUPT_AXE = RegUtil.ToolAndArmorHelper.axe(
		"corrupt",
		() -> toolTiers.CORRUPT,
		() -> itemProperties.LAVA_IMMUNE.get(),
		AttributeFactory.make(
			() -> AttributeData.make(attributes.VOIDIC_DMG, AttributeModifier.Operation.ADD_VALUE, 3D, EquipmentSlotGroup.MAINHAND),
			() -> AttributeData.make(itemAugmentUtil::fang, attributes.VOIDIC_INFUSION, AttributeModifier.Operation.ADD_MULTIPLIED_BASE, 0.15D, EquipmentSlotGroup.MAINHAND)
		),
		RegUtil.ToolAndArmorHelper.TooltipContext.EMPTY
	);

}
