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
import tamaized.voidscape.registry.tool.ExtraToolTypes;
import tamaized.voidscape.registry.tool.ModToolTiers;
import tamaized.voidscape.util.ItemAugmentUtil;

@Component
public class CharredToolSet {

	@Autowired
	private ModToolTiers toolTiers;

	@Autowired
	private ModItemProperties itemProperties;

	@Autowired
	private ModAttributes attributes;

	@Autowired
	private ItemAugmentUtil itemAugmentUtil;

	public final DeferredHolder<Item, Item> CHARRED_WARHAMMER;

	public CharredToolSet(@Autowired ExtraToolTypes extraToolTypes) {
		CHARRED_WARHAMMER = extraToolTypes.hammer(
			"charred",
			() -> toolTiers.CHARRED,
			() -> itemProperties.LAVA_IMMUNE.get(),
			AttributeFactory.make(
				() -> AttributeData.make(attributes.VOIDIC_DMG, AttributeModifier.Operation.ADD_VALUE, 3D, EquipmentSlotGroup.MAINHAND),
				() -> AttributeData.make(itemAugmentUtil::fang, attributes.VOIDIC_INFUSION, AttributeModifier.Operation.ADD_MULTIPLIED_BASE, 0.15D, EquipmentSlotGroup.MAINHAND)
			),
			RegUtil.ToolAndArmorHelper.TooltipContext.EMPTY
		);
	}

}
