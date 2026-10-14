package tamaized.voidscape.registry.tool.set;

import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.registries.DeferredHolder;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.regutil.AttributeData;
import tamaized.regutil.AttributeFactoryProvider;
import tamaized.regutil.ToolAndArmorHelper;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.registry.ModAttributes;
import tamaized.voidscape.registry.ModItemComponents;
import tamaized.voidscape.registry.ModItemProperties;
import tamaized.voidscape.registry.item.AxiomaticCrossbow;
import tamaized.voidscape.registry.item.MaterialItems;
import tamaized.voidscape.registry.tool.ModToolMaterials;

@Component
public class AxiomToolSet {

	public final DeferredHolder<Item, Item> AXIOM_XBOW;

	public AxiomToolSet(
		@Autowired ToolAndArmorHelper toolAndArmorHelper,
		@Autowired AttributeFactoryProvider attributeFactoryProvider,
		@Autowired ModToolMaterials toolTiers,
		@Autowired ModItemProperties itemProperties,
		@Autowired ModAttributes attributes,
		@Autowired ModItemComponents itemComponents,
		@Autowired MaterialItems materialItems
	) {
		final String MATERIAL_NAME = "axiom";

		AXIOM_XBOW = toolAndArmorHelper.registerBow(Items.CROSSBOW, toolAndArmorHelper.gear(
			"xbow",
			MATERIAL_NAME,
			attributeFactoryProvider.make(
				() -> AttributeData.make(attributes.VOIDIC_ARROW_DMG, AttributeModifier.Operation.ADD_VALUE, 7D, EquipmentSlotGroup.MAINHAND),
				() -> AttributeData.make(stack -> stack.getOrDefault(itemComponents.FANG, false), attributes.VOIDIC_INFUSION, AttributeModifier.Operation.ADD_VALUE, 0.15D, EquipmentSlotGroup.MAINHAND)
			),
			(id) -> new AxiomaticCrossbow(
				itemProperties.LAVA_IMMUNE.apply(id)
					.durability(toolTiers.AXIOM.get().durability())
					.repairable(toolTiers.AXIOM.get().repairItems()),
				context -> {
					context.tooltip().accept(net.minecraft.network.chat.Component.empty());
					context.tooltip().accept(net.minecraft.network.chat.Component.translatable(Voidscape.MODID + ".tooltip.axiomatic_crossbow").withStyle(
						ChatFormatting.AQUA,
						ChatFormatting.ITALIC
					));
				},
				materialItems.AXIOMATIC_GEAR
			)
		));

	}
}
