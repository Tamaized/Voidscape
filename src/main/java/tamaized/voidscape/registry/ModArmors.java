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
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;
import tamaized.regutil.RegUtil;
import tamaized.regutil.RegistryClass;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.client.entity.model.ModelArmorCorrupt;
import tamaized.voidscape.client.entity.model.ModelArmorTitanite;

import javax.annotation.Nullable;

public class ModArmors implements RegistryClass {

	static class ArmorMaterial {
		static final RegUtil.ArmorMaterial VOIDIC_CRYSTAL = new 
				RegUtil.ArmorMaterial("voidic_crystal", 39, new int[]{3, 6, 8, 3}, 17, SoundEvents.ARMOR_EQUIP_DIAMOND, 2F, 0.10F, () -> Ingredient.of(ModItems.VOIDIC_CRYSTAL.get()), true, false, false);

		static final RegUtil.ArmorMaterial CORRUPT = new 
				RegUtil.ArmorMaterial("corrupt", 41, new int[]{3, 6, 8, 3}, 19, SoundEvents.ARMOR_EQUIP_NETHERITE, 3F, 0.15F, () -> Ingredient.of(ModItems.TENDRIL.get()), false, true, true) {
			@Override
			@OnlyIn(Dist.CLIENT)
			@SuppressWarnings("unchecked")
			public <A extends HumanoidModel<?>> A getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlot armorSlot, A _default) {
				ModelArmorCorrupt<LivingEntity> model = new ModelArmorCorrupt<>(Minecraft.getInstance().getEntityModels().bakeLayer(
						armorSlot == EquipmentSlot.LEGS ?
								ModEntities.ModelLayerLocations.MODEL_ARMOR_CORRUPT_INNER :
								ModEntities.ModelLayerLocations.MODEL_ARMOR_CORRUPT_OUTER
				));
				model.rightfoot.visible = false;
				model.leftfoot.visible = false;
				model.bodyToLeg.visible = false;
				model.rightleg.visible = false;
				model.leftleg.visible = false;
				model.body.visible = false;
				model.rightarm.visible = false;
				model.leftarm.visible = false;
				model.head.visible = false;
				model.headoverlay.visible = false;
				switch (armorSlot) {
					case FEET:
						model.rightfoot.visible = true;
						model.leftfoot.visible = true;
						break;
					case LEGS:
						model.rightleg.visible = true;
						model.leftleg.visible = true;
						break;
					case CHEST:
						model.bodyToLeg.visible = true;
						model.body.visible = true;
						model.rightarm.visible = true;
						model.leftarm.visible = true;
						float tick = entityLiving.tickCount + Minecraft.getInstance().getDeltaFrameTime();
						float scale = 0.05F;
						float amp = 0.15F;
						float offset = 0.25F;
						model.topLeftTentacle.xRot = Mth.cos(tick * scale) * amp + offset;
						model.topLeftTentacle.yRot = Mth.sin(tick * scale + 0.2F) * amp + offset;
						model.topRightTentacle.xRot = Mth.sin(tick * scale + 0.4F) * amp + offset;
						model.topRightTentacle.yRot = Mth.cos(tick * scale + 0.6F) * amp - offset;
						model.bottomLeftTentacle.xRot = Mth.sin(tick * scale + 0.7F) * amp - offset;
						model.bottomLeftTentacle.yRot = Mth.cos(tick * scale + 0.5F) * amp + offset;
						model.bottomRightTentacle.xRot = Mth.cos(tick * scale + 0.3F) * amp - offset;
						model.bottomRightTentacle.yRot = Mth.sin(tick * scale + 0.1F) * amp - offset;
						break;
					case HEAD:
						model.head.visible = true;
						model.headoverlay.visible = true;
						break;
					default:
				}
				return (A) model;
			}

			@Override
			public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, @Nullable String type) {
				return Voidscape.MODID.concat(":textures/models/armor/corrupt" + (type == null ? "" : "_overlay") + ".png");
			}
		};

		static final RegUtil.ArmorMaterial TITANITE = new 
				RegUtil.ArmorMaterial("titanite", 43, new int[]{3, 6, 8, 3}, 21, SoundEvents.ARMOR_EQUIP_DIAMOND, 4F, 0.20F, () -> Ingredient.of(ModItems.TITANITE_SHARD.get()), false, true, true) {
			@Override
			@OnlyIn(Dist.CLIENT)
			@SuppressWarnings("unchecked")
			public <A extends HumanoidModel<?>> A getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlot armorSlot, A _default) {
				ModelArmorTitanite<LivingEntity> model = new ModelArmorTitanite<>(Minecraft.getInstance().getEntityModels().
						bakeLayer(ModEntities.ModelLayerLocations.MODEL_ARMOR_TITANITE));
				model.head.visible = false;
				model.headoverlay.visible = false;
				model.body.visible = false;
				model.leftarm.visible = false;
				model.rightarm.visible = false;
				model.leftleg.visible = false;
				model.rightleg.visible = false;
				model.leftfoot.visible = false;
				model.rightfoot.visible = false;
				switch (armorSlot) {
					case FEET -> {
						model.rightfoot.visible = true;
						model.leftfoot.visible = true;
					}
					case LEGS -> {
						model.rightleg.visible = true;
						model.leftleg.visible = true;
					}
					case CHEST -> {
						model.body.visible = true;
						model.rightarm.visible = true;
						model.leftarm.visible = true;
					}
					case HEAD -> {
						model.head.visible = true;
						model.headoverlay.visible = true;
					}
				}
				return (A) model;
			}

			@Override
			public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, @Nullable String type) {
				return Voidscape.MODID.concat(":textures/models/armor/titanite" + (type == null ? "" : "_overlay") + ".png");
			}
		};

		static final RegUtil.ArmorMaterial ICHOR = new 
				RegUtil.ArmorMaterial("ichor", 45, new int[]{3, 6, 8, 3}, 23, SoundEvents.ARMOR_EQUIP_DIAMOND, 5F, 0.25F, () -> Ingredient.of(ModItems.ICHOR_CRYSTAL.get()), false, true, true) {
			@Override
			@OnlyIn(Dist.CLIENT)
			@SuppressWarnings("unchecked")
			public <A extends HumanoidModel<?>> A getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlot armorSlot, A _default) {
				ModelArmorTitanite<LivingEntity> model = new ModelArmorTitanite<>(Minecraft.getInstance().getEntityModels().
						bakeLayer(ModEntities.ModelLayerLocations.MODEL_ARMOR_ICHOR));
				model.head.visible = false;
				model.headoverlay.visible = false;
				model.body.visible = false;
				model.leftarm.visible = false;
				model.rightarm.visible = false;
				model.leftleg.visible = false;
				model.rightleg.visible = false;
				model.leftfoot.visible = false;
				model.rightfoot.visible = false;
				switch (armorSlot) {
					case FEET -> {
						model.rightfoot.visible = true;
						model.leftfoot.visible = true;
					}
					case LEGS -> {
						model.rightleg.visible = true;
						model.leftleg.visible = true;
					}
					case CHEST -> {
						model.body.visible = true;
						model.rightarm.visible = true;
						model.leftarm.visible = true;
					}
					case HEAD -> {
						model.head.visible = true;
						model.headoverlay.visible = true;
					}
				}
				return (A) model;
			}

			@Override
			public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, @Nullable String type) {
				return Voidscape.MODID.concat(":textures/models/armor/ichor" + (type == null ? "" : "_overlay") + ".png");
			}
		};
	}

	public static final RegistryObject<Item> VOIDIC_CRYSTAL_HELMET = RegUtil.ToolAndArmorHelper.
			helmet(ArmorMaterial.VOIDIC_CRYSTAL, ModItems.ItemProps.LAVA_IMMUNE.properties().get(), RegUtil.makeAttributeFactory(
					RegUtil.AttributeData.make(ModAttributes.VOIDIC_RES, AttributeModifier.Operation.ADDITION, 1D),
					RegUtil.AttributeData.make(ModAttributes.VOIDIC_INFUSION_RES, AttributeModifier.Operation.MULTIPLY_BASE, 0.05D)), tooltip -> {});
	public static final RegistryObject<Item> VOIDIC_CRYSTAL_CHEST = RegUtil.ToolAndArmorHelper.
			chest(ArmorMaterial.VOIDIC_CRYSTAL, ModItems.ItemProps.LAVA_IMMUNE.properties().get(), RegUtil.makeAttributeFactory(
					RegUtil.AttributeData.make(ModAttributes.VOIDIC_RES, AttributeModifier.Operation.ADDITION, 1D), 
					RegUtil.AttributeData.make(ModAttributes.VOIDIC_INFUSION_RES, AttributeModifier.Operation.MULTIPLY_BASE, 0.05D)), (stack, tick) -> ModArmors.elytra(stack), tooltip -> {});
	public static final RegistryObject<Item> VOIDIC_CRYSTAL_LEGS = RegUtil.ToolAndArmorHelper.
			legs(ArmorMaterial.VOIDIC_CRYSTAL, ModItems.ItemProps.LAVA_IMMUNE.properties().get(), RegUtil.makeAttributeFactory(
					RegUtil.AttributeData.make(ModAttributes.VOIDIC_RES, AttributeModifier.Operation.ADDITION, 1D), 
					RegUtil.AttributeData.make(ModAttributes.VOIDIC_INFUSION_RES, AttributeModifier.Operation.MULTIPLY_BASE, 0.05D)), tooltip -> {});
	public static final RegistryObject<Item> VOIDIC_CRYSTAL_BOOTS = RegUtil.ToolAndArmorHelper.
			boots(ArmorMaterial.VOIDIC_CRYSTAL, ModItems.ItemProps.LAVA_IMMUNE.properties().get(), RegUtil.makeAttributeFactory(
					RegUtil.AttributeData.make(ModAttributes.VOIDIC_RES, AttributeModifier.Operation.ADDITION, 1D), 
					RegUtil.AttributeData.make(ModAttributes.VOIDIC_INFUSION_RES, AttributeModifier.Operation.MULTIPLY_BASE, 0.05D)), tooltip -> {});

	public static final RegistryObject<Item> CORRUPT_HELMET = RegUtil.ToolAndArmorHelper.
			helmet(ArmorMaterial.CORRUPT, ModItems.ItemProps.LAVA_IMMUNE.properties().get(), RegUtil.makeAttributeFactory(
					RegUtil.AttributeData.make(ModAttributes.VOIDIC_RES, AttributeModifier.Operation.ADDITION, 2D), 
					RegUtil.AttributeData.make(ModAttributes.VOIDIC_INFUSION_RES, AttributeModifier.Operation.MULTIPLY_BASE, 0.1D), 
					RegUtil.AttributeData.make(ModAttributes.VOIDIC_PARANOIA_RES, AttributeModifier.Operation.MULTIPLY_BASE, 0.25D), 
					RegUtil.AttributeData.make(ModAttributes.VOIDIC_VISIBILITY, AttributeModifier.Operation.MULTIPLY_BASE, 0.15D)), tooltip -> {});
	public static final RegistryObject<Item> CORRUPT_CHEST = RegUtil.ToolAndArmorHelper.
			chest(ArmorMaterial.CORRUPT, ModItems.ItemProps.LAVA_IMMUNE.properties().get(), RegUtil.makeAttributeFactory(
					RegUtil.AttributeData.make(ModAttributes.VOIDIC_RES, AttributeModifier.Operation.ADDITION, 2D), 
					RegUtil.AttributeData.make(ModAttributes.VOIDIC_INFUSION_RES, AttributeModifier.Operation.MULTIPLY_BASE, 0.1D), 
					RegUtil.AttributeData.make(ModAttributes.VOIDIC_PARANOIA_RES, AttributeModifier.Operation.MULTIPLY_BASE, 0.25D)), (stack, tick) -> true, tooltip -> {});
	public static final RegistryObject<Item> CORRUPT_LEGS = RegUtil.ToolAndArmorHelper.
			legs(ArmorMaterial.CORRUPT, ModItems.ItemProps.LAVA_IMMUNE.properties().get(), RegUtil.makeAttributeFactory(
					RegUtil.AttributeData.make(ModAttributes.VOIDIC_RES, AttributeModifier.Operation.ADDITION, 2D), 
					RegUtil.AttributeData.make(ModAttributes.VOIDIC_INFUSION_RES, AttributeModifier.Operation.MULTIPLY_BASE, 0.1D), 
					RegUtil.AttributeData.make(ModAttributes.VOIDIC_PARANOIA_RES, AttributeModifier.Operation.MULTIPLY_BASE, 0.25D)), tooltip -> {});
	public static final RegistryObject<Item> CORRUPT_BOOTS = RegUtil.ToolAndArmorHelper.
			boots(ArmorMaterial.CORRUPT, ModItems.ItemProps.LAVA_IMMUNE.properties().get(), RegUtil.makeAttributeFactory(
					RegUtil.AttributeData.make(ModAttributes.VOIDIC_RES, AttributeModifier.Operation.ADDITION, 2D), 
					RegUtil.AttributeData.make(ModAttributes.VOIDIC_INFUSION_RES, AttributeModifier.Operation.MULTIPLY_BASE, 0.1D), 
					RegUtil.AttributeData.make(ModAttributes.VOIDIC_PARANOIA_RES, AttributeModifier.Operation.MULTIPLY_BASE, 0.25D)), tooltip -> {});

	public static final RegistryObject<Item> TITANITE_HELMET = RegUtil.ToolAndArmorHelper.
			helmet(ArmorMaterial.TITANITE, ModItems.ItemProps.LAVA_IMMUNE.properties().get(), RegUtil.makeAttributeFactory(
					RegUtil.AttributeData.make(ModAttributes.VOIDIC_RES, AttributeModifier.Operation.ADDITION, 3D), 
					RegUtil.AttributeData.make(ModAttributes.VOIDIC_INFUSION_RES, AttributeModifier.Operation.MULTIPLY_BASE, 0.15D), 
					RegUtil.AttributeData.make(ModAttributes.VOIDIC_PARANOIA_RES, AttributeModifier.Operation.MULTIPLY_BASE, 0.25D), 
					RegUtil.AttributeData.make(ModAttributes.VOIDIC_VISIBILITY, AttributeModifier.Operation.MULTIPLY_BASE, 0.20D)), tooltip -> {});
	public static final RegistryObject<Item> TITANITE_CHEST = RegUtil.ToolAndArmorHelper.
			chest(ArmorMaterial.TITANITE, ModItems.ItemProps.LAVA_IMMUNE.properties().get(), RegUtil.makeAttributeFactory(
					RegUtil.AttributeData.make(ModAttributes.VOIDIC_RES, AttributeModifier.Operation.ADDITION, 3D), 
					RegUtil.AttributeData.make(ModAttributes.VOIDIC_INFUSION_RES, AttributeModifier.Operation.MULTIPLY_BASE, 0.15D), 
					RegUtil.AttributeData.make(ModAttributes.VOIDIC_PARANOIA_RES, AttributeModifier.Operation.MULTIPLY_BASE, 0.25D)),
					(stack, tick) -> ModArmors.elytra(stack), tooltip -> {});
	public static final RegistryObject<Item> TITANITE_LEGS = RegUtil.ToolAndArmorHelper.
			legs(ArmorMaterial.TITANITE, ModItems.ItemProps.LAVA_IMMUNE.properties().get(), RegUtil.makeAttributeFactory(
					RegUtil.AttributeData.make(ModAttributes.VOIDIC_RES, AttributeModifier.Operation.ADDITION, 3D), 
					RegUtil.AttributeData.make(ModAttributes.VOIDIC_INFUSION_RES, AttributeModifier.Operation.MULTIPLY_BASE, 0.15D), 
					RegUtil.AttributeData.make(ModAttributes.VOIDIC_PARANOIA_RES, AttributeModifier.Operation.MULTIPLY_BASE, 0.25D)), tooltip -> {});
	public static final RegistryObject<Item> TITANITE_BOOTS = RegUtil.ToolAndArmorHelper.
			boots(ArmorMaterial.TITANITE, ModItems.ItemProps.LAVA_IMMUNE.properties().get(), RegUtil.makeAttributeFactory(
					RegUtil.AttributeData.make(ModAttributes.VOIDIC_RES, AttributeModifier.Operation.ADDITION, 3D), 
					RegUtil.AttributeData.make(ModAttributes.VOIDIC_INFUSION_RES, AttributeModifier.Operation.MULTIPLY_BASE, 0.15D), 
					RegUtil.AttributeData.make(ModAttributes.VOIDIC_PARANOIA_RES, AttributeModifier.Operation.MULTIPLY_BASE, 0.25D)), tooltip -> {});

	public static final RegistryObject<Item> ICHOR_HELMET = RegUtil.ToolAndArmorHelper.
			helmet(ArmorMaterial.ICHOR, ModItems.ItemProps.LAVA_IMMUNE.properties().get(), RegUtil.makeAttributeFactory(
							RegUtil.AttributeData.make(ModAttributes.VOIDIC_RES, AttributeModifier.Operation.ADDITION, 4D), 
							RegUtil.AttributeData.make(ModAttributes.VOIDIC_INFUSION_RES, AttributeModifier.Operation.MULTIPLY_BASE, 0.17D), 
							RegUtil.AttributeData.make(ModAttributes.VOIDIC_PARANOIA_RES, AttributeModifier.Operation.MULTIPLY_BASE, 0.25D), 
							RegUtil.AttributeData.make(ModAttributes.VOIDIC_VISIBILITY, AttributeModifier.Operation.MULTIPLY_BASE, 0.25D)), tooltip -> {});
	public static final RegistryObject<Item> ICHOR_CHEST = RegUtil.ToolAndArmorHelper.
			chest(ArmorMaterial.ICHOR, ModItems.ItemProps.LAVA_IMMUNE.properties().get(), RegUtil.makeAttributeFactory(
							RegUtil.AttributeData.make(ModAttributes.VOIDIC_RES, AttributeModifier.Operation.ADDITION, 4D), 
							RegUtil.AttributeData.make(ModAttributes.VOIDIC_INFUSION_RES, AttributeModifier.Operation.MULTIPLY_BASE, 0.17D), 
							RegUtil.AttributeData.make(ModAttributes.VOIDIC_PARANOIA_RES, AttributeModifier.Operation.MULTIPLY_BASE, 0.25D)),
					(stack, tick) -> ModArmors.elytra(stack), tooltip -> {});
	public static final RegistryObject<Item> ICHOR_LEGS = RegUtil.ToolAndArmorHelper.
			legs(ArmorMaterial.ICHOR, ModItems.ItemProps.LAVA_IMMUNE.properties().get(), RegUtil.makeAttributeFactory(
							RegUtil.AttributeData.make(ModAttributes.VOIDIC_RES, AttributeModifier.Operation.ADDITION, 4D), 
							RegUtil.AttributeData.make(ModAttributes.VOIDIC_INFUSION_RES, AttributeModifier.Operation.MULTIPLY_BASE, 0.17D), 
							RegUtil.AttributeData.make(ModAttributes.VOIDIC_PARANOIA_RES, AttributeModifier.Operation.MULTIPLY_BASE, 0.25D)), tooltip -> {});
	public static final RegistryObject<Item> ICHOR_BOOTS = RegUtil.ToolAndArmorHelper.
			boots(ArmorMaterial.ICHOR, ModItems.ItemProps.LAVA_IMMUNE.properties().get(), RegUtil.makeAttributeFactory(
							RegUtil.AttributeData.make(ModAttributes.VOIDIC_RES, AttributeModifier.Operation.ADDITION, 4D),
							RegUtil.AttributeData.make(ModAttributes.VOIDIC_INFUSION_RES, AttributeModifier.Operation.MULTIPLY_BASE, 0.17D),
							RegUtil.AttributeData.make(ModAttributes.VOIDIC_PARANOIA_RES, AttributeModifier.Operation.MULTIPLY_BASE, 0.25D)), tooltip -> {});

	@Override
	public void init(IEventBus bus) {

	}

	public static boolean elytra(ItemStack stack) {
		if (stack.isEmpty())
			return false;
		if (!stack.is(VOIDIC_CRYSTAL_CHEST.get()) && !stack.is(TITANITE_CHEST.get()) && !stack.is(ICHOR_CHEST.get()))
			return false; // Quick fail for performance, no nbt polling needed
		CompoundTag nbt = stack.getTagElement(Voidscape.MODID);
		return nbt != null && nbt.getBoolean("elytra");
	}

}
