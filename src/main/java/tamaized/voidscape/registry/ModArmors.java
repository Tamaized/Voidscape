package tamaized.voidscape.registry;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import tamaized.beanification.Autowired;
import tamaized.regutil.*;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.client.entity.model.ModelArmorCorrupt;
import tamaized.voidscape.client.entity.model.ModelArmorTitanite;

import javax.annotation.Nullable;

@tamaized.beanification.Component
public class ModArmors {

	public final DeferredHolder<Item, Item> CORRUPT_HELMET = RegUtil.ToolAndArmorHelper.
			helmet(ArmorMaterial.CORRUPT, ModItems.ItemProps.LAVA_IMMUNE.properties().get(), RegUtil.makeAttributeFactory(
					RegUtil.AttributeData.make(ModAttributes.VOIDIC_RES, AttributeModifier.Operation.ADDITION, 2D),
					RegUtil.AttributeData.make(ModAttributes.VOIDIC_INFUSION_RES, AttributeModifier.Operation.MULTIPLY_BASE, 0.1D),
					RegUtil.AttributeData.make(ModAttributes.VOIDIC_PARANOIA_RES, AttributeModifier.Operation.MULTIPLY_BASE, 0.25D),
					RegUtil.AttributeData.make(ModAttributes.VOIDIC_VISIBILITY, AttributeModifier.Operation.MULTIPLY_BASE, 0.15D)), tooltip -> {});
	public final DeferredHolder<Item, Item> CORRUPT_CHEST = RegUtil.ToolAndArmorHelper.
			chest(ArmorMaterial.CORRUPT, ModItems.ItemProps.LAVA_IMMUNE.properties().get(), RegUtil.makeAttributeFactory(
					RegUtil.AttributeData.make(ModAttributes.VOIDIC_RES, AttributeModifier.Operation.ADDITION, 2D),
					RegUtil.AttributeData.make(ModAttributes.VOIDIC_INFUSION_RES, AttributeModifier.Operation.MULTIPLY_BASE, 0.1D),
					RegUtil.AttributeData.make(ModAttributes.VOIDIC_PARANOIA_RES, AttributeModifier.Operation.MULTIPLY_BASE, 0.25D)), (stack, tick) -> true, tooltip -> {});
	public final DeferredHolder<Item, Item> CORRUPT_LEGS = RegUtil.ToolAndArmorHelper.
			legs(ArmorMaterial.CORRUPT, ModItems.ItemProps.LAVA_IMMUNE.properties().get(), RegUtil.makeAttributeFactory(
					RegUtil.AttributeData.make(ModAttributes.VOIDIC_RES, AttributeModifier.Operation.ADDITION, 2D),
					RegUtil.AttributeData.make(ModAttributes.VOIDIC_INFUSION_RES, AttributeModifier.Operation.MULTIPLY_BASE, 0.1D),
					RegUtil.AttributeData.make(ModAttributes.VOIDIC_PARANOIA_RES, AttributeModifier.Operation.MULTIPLY_BASE, 0.25D)), tooltip -> {});
	public final DeferredHolder<Item, Item> CORRUPT_BOOTS = RegUtil.ToolAndArmorHelper.
			boots(ArmorMaterial.CORRUPT, ModItems.ItemProps.LAVA_IMMUNE.properties().get(), RegUtil.makeAttributeFactory(
					RegUtil.AttributeData.make(ModAttributes.VOIDIC_RES, AttributeModifier.Operation.ADDITION, 2D),
					RegUtil.AttributeData.make(ModAttributes.VOIDIC_INFUSION_RES, AttributeModifier.Operation.MULTIPLY_BASE, 0.1D),
					RegUtil.AttributeData.make(ModAttributes.VOIDIC_PARANOIA_RES, AttributeModifier.Operation.MULTIPLY_BASE, 0.25D)), tooltip -> {});

	public final DeferredHolder<Item, Item> TITANITE_HELMET = RegUtil.ToolAndArmorHelper.
			helmet(ArmorMaterial.TITANITE, ModItems.ItemProps.LAVA_IMMUNE.properties().get(), RegUtil.makeAttributeFactory(
					RegUtil.AttributeData.make(ModAttributes.VOIDIC_RES, AttributeModifier.Operation.ADDITION, 3D),
					RegUtil.AttributeData.make(ModAttributes.VOIDIC_INFUSION_RES, AttributeModifier.Operation.MULTIPLY_BASE, 0.15D),
					RegUtil.AttributeData.make(ModAttributes.VOIDIC_PARANOIA_RES, AttributeModifier.Operation.MULTIPLY_BASE, 0.25D),
					RegUtil.AttributeData.make(ModAttributes.VOIDIC_VISIBILITY, AttributeModifier.Operation.MULTIPLY_BASE, 0.20D)), tooltip -> {});
	public final DeferredHolder<Item, Item> TITANITE_CHEST = RegUtil.ToolAndArmorHelper.
			chest(ArmorMaterial.TITANITE, ModItems.ItemProps.LAVA_IMMUNE.properties().get(), RegUtil.makeAttributeFactory(
					RegUtil.AttributeData.make(ModAttributes.VOIDIC_RES, AttributeModifier.Operation.ADDITION, 3D),
					RegUtil.AttributeData.make(ModAttributes.VOIDIC_INFUSION_RES, AttributeModifier.Operation.MULTIPLY_BASE, 0.15D),
					RegUtil.AttributeData.make(ModAttributes.VOIDIC_PARANOIA_RES, AttributeModifier.Operation.MULTIPLY_BASE, 0.25D)),
					(stack, tick) -> ModArmors.elytra(stack), tooltip -> {});
	public final DeferredHolder<Item, Item> TITANITE_LEGS = RegUtil.ToolAndArmorHelper.
			legs(ArmorMaterial.TITANITE, ModItems.ItemProps.LAVA_IMMUNE.properties().get(), RegUtil.makeAttributeFactory(
					RegUtil.AttributeData.make(ModAttributes.VOIDIC_RES, AttributeModifier.Operation.ADDITION, 3D),
					RegUtil.AttributeData.make(ModAttributes.VOIDIC_INFUSION_RES, AttributeModifier.Operation.MULTIPLY_BASE, 0.15D),
					RegUtil.AttributeData.make(ModAttributes.VOIDIC_PARANOIA_RES, AttributeModifier.Operation.MULTIPLY_BASE, 0.25D)), tooltip -> {});
	public final DeferredHolder<Item, Item> TITANITE_BOOTS = RegUtil.ToolAndArmorHelper.
			boots(ArmorMaterial.TITANITE, ModItems.ItemProps.LAVA_IMMUNE.properties().get(), RegUtil.makeAttributeFactory(
					RegUtil.AttributeData.make(ModAttributes.VOIDIC_RES, AttributeModifier.Operation.ADDITION, 3D),
					RegUtil.AttributeData.make(ModAttributes.VOIDIC_INFUSION_RES, AttributeModifier.Operation.MULTIPLY_BASE, 0.15D),
					RegUtil.AttributeData.make(ModAttributes.VOIDIC_PARANOIA_RES, AttributeModifier.Operation.MULTIPLY_BASE, 0.25D)), tooltip -> {});

	public final DeferredHolder<Item, Item> ICHOR_HELMET = RegUtil.ToolAndArmorHelper.
			helmet(ArmorMaterial.ICHOR, ModItems.ItemProps.LAVA_IMMUNE.properties().get(), RegUtil.makeAttributeFactory(
							RegUtil.AttributeData.make(ModAttributes.VOIDIC_RES, AttributeModifier.Operation.ADDITION, 4D),
							RegUtil.AttributeData.make(ModAttributes.VOIDIC_INFUSION_RES, AttributeModifier.Operation.MULTIPLY_BASE, 0.17D),
							RegUtil.AttributeData.make(ModAttributes.VOIDIC_PARANOIA_RES, AttributeModifier.Operation.MULTIPLY_BASE, 0.25D),
							RegUtil.AttributeData.make(ModAttributes.VOIDIC_VISIBILITY, AttributeModifier.Operation.MULTIPLY_BASE, 0.25D)), tooltip -> {});
	public final DeferredHolder<Item, Item> ICHOR_CHEST = RegUtil.ToolAndArmorHelper.
			chest(ArmorMaterial.ICHOR, ModItems.ItemProps.LAVA_IMMUNE.properties().get(), RegUtil.makeAttributeFactory(
							RegUtil.AttributeData.make(ModAttributes.VOIDIC_RES, AttributeModifier.Operation.ADDITION, 4D),
							RegUtil.AttributeData.make(ModAttributes.VOIDIC_INFUSION_RES, AttributeModifier.Operation.MULTIPLY_BASE, 0.17D),
							RegUtil.AttributeData.make(ModAttributes.VOIDIC_PARANOIA_RES, AttributeModifier.Operation.MULTIPLY_BASE, 0.25D)),
					(stack, tick) -> ModArmors.elytra(stack), tooltip -> {});
	public final DeferredHolder<Item, Item> ICHOR_LEGS = RegUtil.ToolAndArmorHelper.
			legs(ArmorMaterial.ICHOR, ModItems.ItemProps.LAVA_IMMUNE.properties().get(), RegUtil.makeAttributeFactory(
							RegUtil.AttributeData.make(ModAttributes.VOIDIC_RES, AttributeModifier.Operation.ADDITION, 4D),
							RegUtil.AttributeData.make(ModAttributes.VOIDIC_INFUSION_RES, AttributeModifier.Operation.MULTIPLY_BASE, 0.17D),
							RegUtil.AttributeData.make(ModAttributes.VOIDIC_PARANOIA_RES, AttributeModifier.Operation.MULTIPLY_BASE, 0.25D)), tooltip -> {});
	public final DeferredHolder<Item, Item> ICHOR_BOOTS = RegUtil.ToolAndArmorHelper.
			boots(ArmorMaterial.ICHOR, ModItems.ItemProps.LAVA_IMMUNE.properties().get(), RegUtil.makeAttributeFactory(
							RegUtil.AttributeData.make(ModAttributes.VOIDIC_RES, AttributeModifier.Operation.ADDITION, 4D),
							RegUtil.AttributeData.make(ModAttributes.VOIDIC_INFUSION_RES, AttributeModifier.Operation.MULTIPLY_BASE, 0.17D),
							RegUtil.AttributeData.make(ModAttributes.VOIDIC_PARANOIA_RES, AttributeModifier.Operation.MULTIPLY_BASE, 0.25D)), tooltip -> {});

	public final DeferredHolder<Item, Item> ASTRAL_HELMET = RegUtil.ToolAndArmorHelper.
			helmet(ArmorMaterial.ASTRAL, ModItems.ItemProps.LAVA_IMMUNE.properties().get(), RegUtil.makeAttributeFactory(
					RegUtil.AttributeData.make(ModAttributes.VOIDIC_RES, AttributeModifier.Operation.ADDITION, 5D),
					RegUtil.AttributeData.make(ModAttributes.VOIDIC_INFUSION_RES, AttributeModifier.Operation.MULTIPLY_BASE, 0.20D),
					RegUtil.AttributeData.make(ModAttributes.VOIDIC_PARANOIA_RES, AttributeModifier.Operation.MULTIPLY_BASE, 0.25D),
					RegUtil.AttributeData.make(ModAttributes.VOIDIC_VISIBILITY, AttributeModifier.Operation.MULTIPLY_BASE, 0.30D),
					RegUtil.AttributeData.make(ModArmors::draconic, () -> Attributes.MAX_HEALTH, AttributeModifier.Operation.ADDITION, 5D, ModAttributes.DRACONIC_HEALTH.id(), ModAttributes.DRACONIC_HEALTH.type())), tooltip -> {
				if (draconic(tooltip.stack()))
					tooltip.tooltip().add(Component.translatable(Voidscape.MODID + ".tooltip.draconic").withStyle(ChatFormatting.LIGHT_PURPLE));
			});
	public final DeferredHolder<Item, Item> ASTRAL_CHEST = RegUtil.ToolAndArmorHelper.
			chest(ArmorMaterial.ASTRAL, ModItems.ItemProps.LAVA_IMMUNE.properties().get(), RegUtil.makeAttributeFactory(
							RegUtil.AttributeData.make(ModAttributes.VOIDIC_RES, AttributeModifier.Operation.ADDITION, 5D),
							RegUtil.AttributeData.make(ModAttributes.VOIDIC_INFUSION_RES, AttributeModifier.Operation.MULTIPLY_BASE, 0.20D),
							RegUtil.AttributeData.make(ModAttributes.VOIDIC_PARANOIA_RES, AttributeModifier.Operation.MULTIPLY_BASE, 0.25D),
							RegUtil.AttributeData.make(ModArmors::draconic, () -> Attributes.MAX_HEALTH, AttributeModifier.Operation.ADDITION, 5D, ModAttributes.DRACONIC_HEALTH.id(), ModAttributes.DRACONIC_HEALTH.type())),
					(stack, tick) -> ModArmors.elytra(stack), tooltip -> {
						if (draconic(tooltip.stack()))
							tooltip.tooltip().add(Component.translatable(Voidscape.MODID + ".tooltip.draconic").withStyle(ChatFormatting.LIGHT_PURPLE));
					});
	public final DeferredHolder<Item, Item> ASTRAL_LEGS = RegUtil.ToolAndArmorHelper.
			legs(ArmorMaterial.ASTRAL, ModItems.ItemProps.LAVA_IMMUNE.properties().get(), RegUtil.makeAttributeFactory(
					RegUtil.AttributeData.make(ModAttributes.VOIDIC_RES, AttributeModifier.Operation.ADDITION, 5D),
					RegUtil.AttributeData.make(ModAttributes.VOIDIC_INFUSION_RES, AttributeModifier.Operation.MULTIPLY_BASE, 0.20D),
					RegUtil.AttributeData.make(ModAttributes.VOIDIC_PARANOIA_RES, AttributeModifier.Operation.MULTIPLY_BASE, 0.25D),
					RegUtil.AttributeData.make(ModArmors::draconic, () -> Attributes.MAX_HEALTH, AttributeModifier.Operation.ADDITION, 5D, ModAttributes.DRACONIC_HEALTH.id(), ModAttributes.DRACONIC_HEALTH.type())), tooltip -> {
				if (draconic(tooltip.stack()))
					tooltip.tooltip().add(Component.translatable(Voidscape.MODID + ".tooltip.draconic").withStyle(ChatFormatting.LIGHT_PURPLE));
			});
	public final DeferredHolder<Item, Item> ASTRAL_BOOTS = RegUtil.ToolAndArmorHelper.
			boots(ArmorMaterial.ASTRAL, ModItems.ItemProps.LAVA_IMMUNE.properties().get(), RegUtil.makeAttributeFactory(
					RegUtil.AttributeData.make(ModAttributes.VOIDIC_RES, AttributeModifier.Operation.ADDITION, 5D),
					RegUtil.AttributeData.make(ModAttributes.VOIDIC_INFUSION_RES, AttributeModifier.Operation.MULTIPLY_BASE, 0.20D),
					RegUtil.AttributeData.make(ModAttributes.VOIDIC_PARANOIA_RES, AttributeModifier.Operation.MULTIPLY_BASE, 0.25D),
					RegUtil.AttributeData.make(ModArmors::draconic, () -> Attributes.MAX_HEALTH, AttributeModifier.Operation.ADDITION, 5D, ModAttributes.DRACONIC_HEALTH.id(), ModAttributes.DRACONIC_HEALTH.type())), tooltip -> {
				if (draconic(tooltip.stack()))
					tooltip.tooltip().add(Component.translatable(Voidscape.MODID + ".tooltip.draconic").withStyle(ChatFormatting.LIGHT_PURPLE));
			});

}
