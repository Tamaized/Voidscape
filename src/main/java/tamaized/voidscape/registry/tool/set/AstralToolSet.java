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
import tamaized.voidscape.registry.ModItemComponents;
import tamaized.voidscape.registry.ModItemProperties;
import tamaized.voidscape.registry.tool.ExtraToolTypes;
import tamaized.voidscape.registry.tool.ModToolTiers;

@Component
public class AstralToolSet {

	private final String MATERIAL_NAME = "astral";

	@Autowired
	private ModToolTiers toolTiers;

	@Autowired
	private ModItemProperties itemProperties;

	@Autowired
	private ModAttributes attributes;

	@Autowired
	private ModItemComponents itemComponents;

	public final DeferredHolder<Item, Item> ASTRAL_SWORD = RegUtil.ToolAndArmorHelper.sword(
		MATERIAL_NAME,
		() -> toolTiers.ASTRAL,
		() -> itemProperties.LAVA_IMMUNE.get(),
		AttributeFactory.make(
			() -> AttributeData.make(attributes.VOIDIC_DMG, AttributeModifier.Operation.ADD_VALUE, 5D, EquipmentSlotGroup.MAINHAND),
			() -> AttributeData.make(stack -> stack.getOrDefault(itemComponents.FANG, false), attributes.VOIDIC_INFUSION, AttributeModifier.Operation.ADD_VALUE, 0.15D, EquipmentSlotGroup.MAINHAND)
		),
		RegUtil.ToolAndArmorHelper.TooltipContext.EMPTY
	);

	public final DeferredHolder<Item, Item> ASTRAL_AXE = RegUtil.ToolAndArmorHelper.axe(
		MATERIAL_NAME,
		() -> toolTiers.ASTRAL,
		() -> itemProperties.LAVA_IMMUNE.get(),
		AttributeFactory.make(
			() -> AttributeData.make(attributes.VOIDIC_DMG, AttributeModifier.Operation.ADD_VALUE, 6D, EquipmentSlotGroup.MAINHAND),
			() -> AttributeData.make(stack -> stack.getOrDefault(itemComponents.FANG, false), attributes.VOIDIC_INFUSION, AttributeModifier.Operation.ADD_VALUE, 0.15D, EquipmentSlotGroup.MAINHAND)
		),
		RegUtil.ToolAndArmorHelper.TooltipContext.EMPTY
	);

	public final DeferredHolder<Item, Item> ASTRAL_PICKAXE = RegUtil.ToolAndArmorHelper.pickaxe(
		MATERIAL_NAME,
		() -> toolTiers.ASTRAL,
		() -> itemProperties.LAVA_IMMUNE.get(),
		AttributeFactory.make(
			() -> AttributeData.make(attributes.VOIDIC_DMG, AttributeModifier.Operation.ADD_VALUE, 4D, EquipmentSlotGroup.MAINHAND)
		),
		RegUtil.ToolAndArmorHelper.TooltipContext.EMPTY
	);

	public final DeferredHolder<Item, Item> ASTRAL_SHOVEL;

	public final DeferredHolder<Item, Item> ASTRAL_BOW = RegUtil.ToolAndArmorHelper.bow(
		MATERIAL_NAME,
		() -> toolTiers.ASTRAL,
		() -> itemProperties.LAVA_IMMUNE.get(),
		AttributeFactory.make(
			() -> AttributeData.make(attributes.VOIDIC_ARROW_DMG, AttributeModifier.Operation.ADD_VALUE, 5D, EquipmentSlotGroup.MAINHAND),
			() -> AttributeData.make(stack -> stack.getOrDefault(itemComponents.FANG, false), attributes.VOIDIC_INFUSION, AttributeModifier.Operation.ADD_VALUE, 0.15D, EquipmentSlotGroup.MAINHAND)
		),
		RegUtil.ToolAndArmorHelper.TooltipContext.EMPTY
	);

	public final DeferredHolder<Item, Item> ASTRAL_XBOW = RegUtil.ToolAndArmorHelper.xbow(
		MATERIAL_NAME,
		() -> toolTiers.ASTRAL,
		() -> itemProperties.LAVA_IMMUNE.get(),
		AttributeFactory.make(
			() -> AttributeData.make(attributes.VOIDIC_ARROW_DMG, AttributeModifier.Operation.ADD_VALUE, 5D, EquipmentSlotGroup.MAINHAND),
			() -> AttributeData.make(stack -> stack.getOrDefault(itemComponents.FANG, false), attributes.VOIDIC_INFUSION, AttributeModifier.Operation.ADD_VALUE, 0.15D, EquipmentSlotGroup.MAINHAND)
		),
		RegUtil.ToolAndArmorHelper.TooltipContext.EMPTY
	);

	public AstralToolSet(@Autowired ExtraToolTypes extraToolTypes) {
		ASTRAL_SHOVEL = extraToolTypes.shovelThreeByThree(
			MATERIAL_NAME,
			() -> toolTiers.ASTRAL,
			() -> itemProperties.LAVA_IMMUNE.get(),
			AttributeFactory.make(
				() -> AttributeData.make(attributes.VOIDIC_DMG, AttributeModifier.Operation.ADD_VALUE, 3D, EquipmentSlotGroup.MAINHAND)
			),
			RegUtil.ToolAndArmorHelper.TooltipContext.EMPTY
		);
	}
}
