package tamaized.voidscape.registry.tool.set;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.regutil.AttributeData;
import tamaized.regutil.AttributeFactory;
import tamaized.regutil.RegUtil;
import tamaized.voidscape.item.QuiverItem;
import tamaized.voidscape.registry.ModAttributes;
import tamaized.voidscape.registry.ModItemComponents;
import tamaized.voidscape.registry.ModItemProperties;
import tamaized.voidscape.registry.tool.ExtraToolTypes;
import tamaized.voidscape.registry.tool.ModToolMaterials;

@Component
public class CharredToolSet {

	private final String MATERIAL_NAME = "charred";

	@Autowired
	private ModToolMaterials toolTiers;

	@Autowired
	private ModItemProperties itemProperties;

	@Autowired
	private ModAttributes attributes;

	@Autowired
	private ModItemComponents itemComponents;

	private final DeferredRegister<Item> REGISTRY = RegUtil.create(Registries.ITEM);

	public final DeferredHolder<Item, Item> CHARRED_WARHAMMER;

	public final DeferredHolder<Item, Item> CHARRED_QUIVER = REGISTRY.register("charred_quiver", () -> new QuiverItem(
		itemProperties.LAVA_IMMUNE.get()
	));

	public CharredToolSet(@Autowired ExtraToolTypes extraToolTypes) {
		CHARRED_WARHAMMER = extraToolTypes.hammer(
			MATERIAL_NAME,
			() -> toolTiers.CHARRED,
			() -> itemProperties.LAVA_IMMUNE.get(),
			AttributeFactory.make(
				() -> AttributeData.make(attributes.VOIDIC_DMG, AttributeModifier.Operation.ADD_VALUE, 3D, EquipmentSlotGroup.MAINHAND),
				() -> AttributeData.make(stack -> stack.getOrDefault(itemComponents.FANG, false), attributes.VOIDIC_INFUSION, AttributeModifier.Operation.ADD_VALUE, 0.15D, EquipmentSlotGroup.MAINHAND)
			),
			RegUtil.ToolAndArmorHelper.TooltipContext.EMPTY
		);
	}

}
