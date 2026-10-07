package tamaized.voidscape.registry.tool.set;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.BlocksAttacks;
import net.minecraft.world.item.component.TooltipDisplay;
import net.neoforged.neoforge.registries.DeferredHolder;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.regutil.AttributeData;
import tamaized.regutil.AttributeFactoryProvider;
import tamaized.regutil.ExtraTooltipContext;
import tamaized.regutil.ToolAndArmorHelper;
import tamaized.regutil.item.BreakableHelper;
import tamaized.regutil.item.BreakableLootingAxe;
import tamaized.regutil.item.BreakableShield;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.registry.ModAttributes;
import tamaized.voidscape.registry.ModEffects;
import tamaized.voidscape.registry.ModItemComponents;
import tamaized.voidscape.registry.ModItemProperties;
import tamaized.voidscape.registry.tool.ModToolMaterials;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Component
public class EntropicToolSet {

	public final DeferredHolder<Item, Item> ENTROPIC_AXE;
	public final DeferredHolder<Item, Item> ENTROPIC_SHIELD;

	public EntropicToolSet(
		@Autowired BreakableHelper breakableHelper,
		@Autowired ToolAndArmorHelper toolAndArmorHelper,
		@Autowired AttributeFactoryProvider attributeFactoryProvider,
		@Autowired ModToolMaterials toolTiers,
		@Autowired ModItemProperties itemProperties,
		@Autowired ModAttributes attributes,
		@Autowired ModItemComponents itemComponents,
		@Autowired ModEffects effects
		) {
		final String MATERIAL_NAME = "entropic";

		ENTROPIC_AXE = toolAndArmorHelper.gear(
			"axe",
			MATERIAL_NAME,
			attributeFactoryProvider.make(
				() -> AttributeData.make(attributes.VOIDIC_DMG, AttributeModifier.Operation.ADD_VALUE, 9D, EquipmentSlotGroup.MAINHAND),
				() -> AttributeData.make(stack -> stack.getOrDefault(itemComponents.FANG, false), attributes.VOIDIC_INFUSION, AttributeModifier.Operation.ADD_VALUE, 0.15D, EquipmentSlotGroup.MAINHAND)
			),
			(id) -> new BreakableLootingAxe(
				toolTiers.ENTROPIC.get(),
				itemProperties.LAVA_IMMUNE.apply(id),
				ExtraTooltipContext.EMPTY
			) {
				@Override
				public void postHurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
					super.postHurtEnemy(stack, target, attacker);
					if (breakableHelper.isBroken(stack))
						return;
					target.addEffect(new MobEffectInstance(effects.ICHOR, 20 * 5));
					target.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 20 * 5, 4));
				}

				@Override
				public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<net.minecraft.network.chat.Component> builder, TooltipFlag tooltipFlag) {
					super.appendHoverText(stack, context, display, builder, tooltipFlag);
					builder.accept(net.minecraft.network.chat.Component.empty());
					builder.accept(net.minecraft.network.chat.Component.translatable(Voidscape.MODID + ".tooltip.entropic_axe").withStyle(
						ChatFormatting.DARK_RED,
						ChatFormatting.ITALIC
					));
					builder.accept(net.minecraft.network.chat.Component.empty());
					builder.accept(net.minecraft.network.chat.Component.translatable(Voidscape.MODID + ".tooltip.entropic_axe_effects").withStyle(
						ChatFormatting.YELLOW
					));
				}
			}
		);

		ENTROPIC_SHIELD = toolAndArmorHelper.shield(
			MATERIAL_NAME,
			toolTiers.ENTROPIC,
			itemProperties.LAVA_IMMUNE,
			attributeFactoryProvider.make(
				() -> AttributeData.make(attributes.VOIDIC_RES, AttributeModifier.Operation.ADD_VALUE, 4D, EquipmentSlotGroup.OFFHAND)
			),
			context -> {
				context.tooltip().accept(net.minecraft.network.chat.Component.empty());
				context.tooltip().accept(net.minecraft.network.chat.Component
					.translatable(Voidscape.MODID + ".tooltip.maddening")
					.withStyle(ChatFormatting.DARK_RED)
					.withStyle(ChatFormatting.ITALIC)
				);
			}
		);

	}
}
