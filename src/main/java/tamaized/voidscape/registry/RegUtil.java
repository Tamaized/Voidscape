package tamaized.voidscape.registry;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.AxeItem;
import net.minecraft.item.BowItem;
import net.minecraft.item.HoeItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ShovelItem;
import net.minecraft.item.SwordItem;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.LazyValue;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import tamaized.voidscape.Voidscape;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

public class RegUtil {

	private static final UUID[] ARMOR_MODIFIER_UUID_PER_SLOT = new UUID[]{UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"), UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"), UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"), UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150")};

	public static final ItemGroup CREATIVE_TAB = new ItemGroup(Voidscape.MODID.concat(".item_group")) {
		@Override
		public ItemStack makeIcon() {
			return new ItemStack(ModItems.VOIDIC_CRYSTAL.get());
		}
	};
	private static final List<DeferredRegister> REGISTERS = new ArrayList<>();

	public static void register(IEventBus bus) {
		ModAttributes.classload();
		ModArmors.classload();
		ModBlocks.classload();
		ModEffects.classload();
		ModItems.classload();
		ModParticles.classload();
		ModSounds.classload();
		ModTools.classload();
		ModEntities.classload();
		ModBiomes.classload();
		for (DeferredRegister register : REGISTERS)
			register.register(bus);
	}

	static <R extends IForgeRegistryEntry<R>> DeferredRegister<R> create(IForgeRegistry<R> type) {
		DeferredRegister<R> def = DeferredRegister.create(type, Voidscape.MODID);
		REGISTERS.add(def);
		return def;
	}

	static Function<Integer, Multimap<Attribute, AttributeModifier>> makeAttributeFactory(AttributeData... data) {
		return (slot) -> {
			ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
			for (AttributeData attribute : data) {
				ModAttribute a = attribute.attribute.get();
				builder.put(a, new AttributeModifier(slot == null ? a.id : ARMOR_MODIFIER_UUID_PER_SLOT[slot], a.type, attribute.value, attribute.op));
			}
			return builder.build();
		};
	}

	enum ItemProps {
		VOIDIC_CRYSTAL(() -> new Item.Properties().tab(RegUtil.CREATIVE_TAB).fireResistant());

		private final Supplier<Item.Properties> properties;

		ItemProps(Supplier<Item.Properties> properties) {
			this.properties = properties;
		}

		Item.Properties get() {
			return properties.get();
		}
	}

	enum ItemTier implements IItemTier {
		VOIDIC_CRYSTAL(5, 2538, 9.5F, 5F, 17, () -> {
			return Ingredient.of(ModItems.VOIDIC_CRYSTAL.get());
		});

		private final int harvestLevel;
		private final int maxUses;
		private final float efficiency;
		private final float attackDamage;
		private final int enchantability;
		private final LazyValue<Ingredient> repairMaterial;

		ItemTier(int harvestLevelIn, int maxUsesIn, float efficiencyIn, float attackDamageIn, int enchantabilityIn, Supplier<Ingredient> repairMaterialIn) {
			this.harvestLevel = harvestLevelIn;
			this.maxUses = maxUsesIn;
			this.efficiency = efficiencyIn;
			this.attackDamage = attackDamageIn;
			this.enchantability = enchantabilityIn;
			this.repairMaterial = new LazyValue<>(repairMaterialIn);
		}

		@Override
		public int getUses() {
			return this.maxUses;
		}

		@Override
		public float getSpeed() {
			return this.efficiency;
		}

		@Override
		public float getAttackDamageBonus() {
			return this.attackDamage;
		}

		@Override
		public int getLevel() {
			return this.harvestLevel;
		}

		@Override
		public int getEnchantmentValue() {
			return this.enchantability;
		}

		@Override
		public Ingredient getRepairIngredient() {
			return this.repairMaterial.get();
		}
	}

	enum ArmorMaterial implements IArmorMaterial {
		VOIDIC_CRYSTAL(ModItems.VOIDIC_CRYSTAL.getId().toString(), 39, new int[]{3, 6, 8, 3}, 17, SoundEvents.ARMOR_EQUIP_DIAMOND, 4F, 0.2F, () -> {
			return Ingredient.of(ModItems.VOIDIC_CRYSTAL.get());
		}, true);

		private static final int[] MAX_DAMAGE_ARRAY = new int[]{13, 15, 16, 11};
		private final String name;
		private final int maxDamageFactor;
		private final int[] damageReductionAmountArray;
		private final int enchantability;
		private final SoundEvent soundEvent;
		private final float toughness;
		private final float knockbackResistance;
		private final LazyValue<Ingredient> repairMaterial;
		private final boolean fullbright;

		ArmorMaterial(String name, int maxDamageFactor, int[] damageReductionAmountArray, int enchantability, SoundEvent soundEvent, float toughness, float knockbackResistance, Supplier<Ingredient> repairMaterial, boolean fullbright) {
			this.name = name;
			this.maxDamageFactor = maxDamageFactor;
			this.damageReductionAmountArray = damageReductionAmountArray;
			this.enchantability = enchantability;
			this.soundEvent = soundEvent;
			this.toughness = toughness;
			this.knockbackResistance = knockbackResistance;
			this.repairMaterial = new LazyValue<>(repairMaterial);
			this.fullbright = fullbright;
		}

		@Override
		public int getDurabilityForSlot(EquipmentSlotType slotIn) {
			return MAX_DAMAGE_ARRAY[slotIn.getIndex()] * this.maxDamageFactor;
		}

		@Override
		public int getDefenseForSlot(EquipmentSlotType slotIn) {
			return this.damageReductionAmountArray[slotIn.getIndex()];
		}

		@Override
		public int getEnchantmentValue() {
			return this.enchantability;
		}

		@Override
		public SoundEvent getEquipSound() {
			return this.soundEvent;
		}

		@Override
		public Ingredient getRepairIngredient() {
			return this.repairMaterial.get();
		}

		@Override
		@OnlyIn(Dist.CLIENT)
		public String getName() {
			return this.name;
		}

		@Override
		public float getToughness() {
			return this.toughness;
		}

		@Override
		public float getKnockbackResistance() {
			return this.knockbackResistance;
		}
	}

	static class ToolAndArmorHelper {

		static RegistryObject<Item> sword(ItemTier tier, Item.Properties properties, Function<Integer, Multimap<Attribute, AttributeModifier>> factory) {
			return ModItems.REGISTRY.register(tier.name().toLowerCase(Locale.US).concat("_sword"), () -> new SwordItem(tier, 3, -2.4F, properties) {
				@Override
				public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlotType equipmentSlot) {
					ImmutableMultimap.Builder<Attribute, AttributeModifier> map = ImmutableMultimap.builder();
					map.putAll(super.getDefaultAttributeModifiers(equipmentSlot));
					if (equipmentSlot == EquipmentSlotType.MAINHAND)
						map.putAll(factory.apply(null));
					return map.build();
				}
			});
		}

		static RegistryObject<Item> bow(ItemTier tier, Item.Properties properties, Function<Integer, Multimap<Attribute, AttributeModifier>> factory) {
			return ModItems.REGISTRY.register(tier.name().toLowerCase(Locale.US).concat("_bow"), () -> new BowItem(properties.defaultDurability(tier.getUses())) {
				@Override
				public int getEnchantmentValue() {
					return tier.getEnchantmentValue();
				}

				@Override
				public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
					return tier.getRepairIngredient().test(repair) || super.isValidRepairItem(toRepair, repair);
				}

				@Override
				public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack) {
					ImmutableMultimap.Builder<Attribute, AttributeModifier> map = ImmutableMultimap.builder();
					map.putAll(super.getAttributeModifiers(slot, stack));
					if (slot == EquipmentSlotType.MAINHAND)
						map.putAll(factory.apply(null));
					return map.build();
				}
			});
		}
		static RegistryObject<Item> axe(ItemTier tier, Item.Properties properties, Function<Integer, Multimap<Attribute, AttributeModifier>> factory) {
			return ModItems.REGISTRY.register(tier.name().toLowerCase(Locale.US).concat("_axe"), () -> new AxeItem(tier, 5F, -3.0F, properties) {
				@Override
				public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlotType equipmentSlot) {
					ImmutableMultimap.Builder<Attribute, AttributeModifier> map = ImmutableMultimap.builder();
					map.putAll(super.getDefaultAttributeModifiers(equipmentSlot));
					if (equipmentSlot == EquipmentSlotType.MAINHAND)
						map.putAll(factory.apply(null));
					return map.build();
				}
			});
		}

		static RegistryObject<Item> pickaxe(ItemTier tier, Item.Properties properties, Function<Integer, Multimap<Attribute, AttributeModifier>> factory) {
			return ModItems.REGISTRY.register(tier.name().toLowerCase(Locale.US).concat("_pickaxe"), () -> new PickaxeItem(tier, 1, -2.8F, properties) {
				@Override
				public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlotType equipmentSlot) {
					ImmutableMultimap.Builder<Attribute, AttributeModifier> map = ImmutableMultimap.builder();
					map.putAll(super.getDefaultAttributeModifiers(equipmentSlot));
					if (equipmentSlot == EquipmentSlotType.MAINHAND)
						map.putAll(factory.apply(null));
					return map.build();
				}
			});
		}

		static RegistryObject<Item> shovel(ItemTier tier, Item.Properties properties, Function<Integer, Multimap<Attribute, AttributeModifier>> factory) {
			return ModItems.REGISTRY.register(tier.name().toLowerCase(Locale.US).concat("_shovel"), () -> new ShovelItem(tier, 1.5F, -3.0F, properties) {
				@Override
				public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlotType equipmentSlot) {
					ImmutableMultimap.Builder<Attribute, AttributeModifier> map = ImmutableMultimap.builder();
					map.putAll(super.getDefaultAttributeModifiers(equipmentSlot));
					if (equipmentSlot == EquipmentSlotType.MAINHAND)
						map.putAll(factory.apply(null));
					return map.build();
				}
			});
		}

		static RegistryObject<Item> hoe(ItemTier tier, Item.Properties properties, Function<Integer, Multimap<Attribute, AttributeModifier>> factory) {
			return ModItems.REGISTRY.register(tier.name().toLowerCase(Locale.US).concat("_hoe"), () -> new HoeItem(tier, -3, 0.0F, properties) {
				@Override
				public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlotType equipmentSlot) {
					ImmutableMultimap.Builder<Attribute, AttributeModifier> map = ImmutableMultimap.builder();
					map.putAll(super.getDefaultAttributeModifiers(equipmentSlot));
					if (equipmentSlot == EquipmentSlotType.MAINHAND)
						map.putAll(factory.apply(null));
					return map.build();
				}
			});
		}

		static RegistryObject<Item> helmet(ArmorMaterial tier, Item.Properties properties, Function<Integer, Multimap<Attribute, AttributeModifier>> factory) {
			return ModItems.REGISTRY.register(tier.name().toLowerCase(Locale.US).concat("_helmet"), armorFactory(tier, EquipmentSlotType.HEAD, properties, factory));
		}

		static RegistryObject<Item> chest(ArmorMaterial tier, Item.Properties properties, Function<Integer, Multimap<Attribute, AttributeModifier>> factory) {
			return ModItems.REGISTRY.register(tier.name().toLowerCase(Locale.US).concat("_chest"), armorFactory(tier, EquipmentSlotType.CHEST, properties, factory));
		}

		static RegistryObject<Item> legs(ArmorMaterial tier, Item.Properties properties, Function<Integer, Multimap<Attribute, AttributeModifier>> factory) {
			return ModItems.REGISTRY.register(tier.name().toLowerCase(Locale.US).concat("_legs"), armorFactory(tier, EquipmentSlotType.LEGS, properties, factory));
		}

		static RegistryObject<Item> boots(ArmorMaterial tier, Item.Properties properties, Function<Integer, Multimap<Attribute, AttributeModifier>> factory) {
			return ModItems.REGISTRY.register(tier.name().toLowerCase(Locale.US).concat("_boots"), armorFactory(tier, EquipmentSlotType.FEET, properties, factory));
		}

		@SuppressWarnings("unchecked")
		private static Supplier<ArmorItem> armorFactory(ArmorMaterial tier, EquipmentSlotType slot, Item.Properties properties, Function<Integer, Multimap<Attribute, AttributeModifier>> factory) {
			return () -> new ArmorItem(tier, slot, properties) {

				@Override
				public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlotType equipmentSlot) {
					ImmutableMultimap.Builder<Attribute, AttributeModifier> map = ImmutableMultimap.builder();
					map.putAll(super.getDefaultAttributeModifiers(equipmentSlot));
					if (equipmentSlot == slot)
						map.putAll(factory.apply(equipmentSlot.getIndex()));
					return map.build();
				}

				@Override
				@OnlyIn(Dist.CLIENT)
				public <A extends BipedModel<?>> A getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlotType armorSlot, A _default) {
					return tier.fullbright ? (A) new BipedModel(slot == EquipmentSlotType.LEGS ? 0.5F : 1.0F) {
						@Override
						public void renderToBuffer(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
							super.renderToBuffer(matrixStackIn, bufferIn, 0xF000F0, packedOverlayIn, red, green, blue, alpha);
						}
					} : super.getArmorModel(entityLiving, itemStack, armorSlot, _default);
				}

			};
		}

	}

	static class ModAttribute extends Attribute {
		final UUID id;
		final String type;

		ModAttribute(String name, float defaultValue, UUID id, String type) {
			super(name, defaultValue);
			this.id = id;
			this.type = type;
		}
	}

	static class AttributeData {
		final Supplier<ModAttribute> attribute;
		final AttributeModifier.Operation op;
		final double value;

		private AttributeData(Supplier<ModAttribute> attribute, AttributeModifier.Operation op, double value) {
			this.attribute = attribute;
			this.op = op;
			this.value = value;
		}

		static AttributeData make(Supplier<ModAttribute> attribute, AttributeModifier.Operation op, double value) {
			return new AttributeData(attribute, op, value);
		}
	}

}
