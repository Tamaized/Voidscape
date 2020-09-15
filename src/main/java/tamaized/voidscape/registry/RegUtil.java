package tamaized.voidscape.registry;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.AxeItem;
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
import java.util.function.Supplier;

public class RegUtil {

	public static final ItemGroup CREATIVE_TAB = new ItemGroup(Voidscape.MODID.concat(".item_group")) {
		@Override
		public ItemStack createIcon() {
			return new ItemStack(ModItems.VOIDIC_CRYSTAL.get());
		}
	};
	private static final List<DeferredRegister> REGISTERS = new ArrayList<>();

	public static void register(IEventBus bus) {
		ModArmors.classload();
		ModBlocks.classload();
		ModItems.classload();
		ModSounds.classload();
		ModTools.classload();
		for (DeferredRegister register : REGISTERS)
			register.register(bus);
	}

	static <R extends IForgeRegistryEntry<R>> DeferredRegister<R> create(IForgeRegistry<R> type) {
		DeferredRegister<R> def = DeferredRegister.create(type, Voidscape.MODID);
		REGISTERS.add(def);
		return def;
	}

	enum ItemProps {
		VOIDIC_CRYSTAL(new Item.Properties().group(RegUtil.CREATIVE_TAB).func_234689_a_());

		private final Item.Properties properties;

		ItemProps(Item.Properties properties) {
			this.properties = properties;
		}

		Item.Properties get() {
			return properties;
		}
	}

	enum ItemTier implements IItemTier {
		VOIDIC_CRYSTAL(5, 2538, 9.5F, 4.5F, 17, () -> {
			return Ingredient.fromItems(ModItems.VOIDIC_CRYSTAL.get());
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
		public int getMaxUses() {
			return this.maxUses;
		}

		@Override
		public float getEfficiency() {
			return this.efficiency;
		}

		@Override
		public float getAttackDamage() {
			return this.attackDamage;
		}

		@Override
		public int getHarvestLevel() {
			return this.harvestLevel;
		}

		@Override
		public int getEnchantability() {
			return this.enchantability;
		}

		@Override
		public Ingredient getRepairMaterial() {
			return this.repairMaterial.getValue();
		}
	}

	enum ArmorMaterial implements IArmorMaterial {
		VOIDIC_CRYSTAL(ModItems.VOIDIC_CRYSTAL.getId().toString(), 39, new int[]{3, 6, 8, 3}, 17, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 3.5F, 0.2F, () -> {
			return Ingredient.fromItems(ModItems.VOIDIC_CRYSTAL.get());
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
		public int getDurability(EquipmentSlotType slotIn) {
			return MAX_DAMAGE_ARRAY[slotIn.getIndex()] * this.maxDamageFactor;
		}

		@Override
		public int getDamageReductionAmount(EquipmentSlotType slotIn) {
			return this.damageReductionAmountArray[slotIn.getIndex()];
		}

		@Override
		public int getEnchantability() {
			return this.enchantability;
		}

		@Override
		public SoundEvent getSoundEvent() {
			return this.soundEvent;
		}

		@Override
		public Ingredient getRepairMaterial() {
			return this.repairMaterial.getValue();
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
		public float func_230304_f_() {
			return this.knockbackResistance;
		}
	}

	static class ToolAndArmorHelper {

		static RegistryObject<Item> sword(ItemTier tier, Item.Properties properties) {
			return ModItems.REGISTRY.register(tier.name().toLowerCase(Locale.US).concat("_sword"), () -> new SwordItem(tier, 3, -2.4F, properties));
		}

		static RegistryObject<Item> axe(ItemTier tier, Item.Properties properties) {
			return ModItems.REGISTRY.register(tier.name().toLowerCase(Locale.US).concat("_axe"), () -> new AxeItem(tier, 5F, -3.0F, properties));
		}

		static RegistryObject<Item> pickaxe(ItemTier tier, Item.Properties properties) {
			return ModItems.REGISTRY.register(tier.name().toLowerCase(Locale.US).concat("_pickaxe"), () -> new PickaxeItem(tier, 1, -2.8F, properties));
		}

		static RegistryObject<Item> shovel(ItemTier tier, Item.Properties properties) {
			return ModItems.REGISTRY.register(tier.name().toLowerCase(Locale.US).concat("_shovel"), () -> new ShovelItem(tier, 1.5F, -3.0F, properties));
		}

		static RegistryObject<Item> hoe(ItemTier tier, Item.Properties properties) {
			return ModItems.REGISTRY.register(tier.name().toLowerCase(Locale.US).concat("_hoe"), () -> new HoeItem(tier, -3, 0.0F, properties));
		}

		static RegistryObject<Item> helmet(ArmorMaterial tier, Item.Properties properties) {
			return ModItems.REGISTRY.register(tier.name().toLowerCase(Locale.US).concat("_helmet"), armorFactory(tier, EquipmentSlotType.HEAD, properties));
		}

		static RegistryObject<Item> chest(ArmorMaterial tier, Item.Properties properties) {
			return ModItems.REGISTRY.register(tier.name().toLowerCase(Locale.US).concat("_chest"), armorFactory(tier, EquipmentSlotType.CHEST, properties));
		}

		static RegistryObject<Item> legs(ArmorMaterial tier, Item.Properties properties) {
			return ModItems.REGISTRY.register(tier.name().toLowerCase(Locale.US).concat("_legs"), armorFactory(tier, EquipmentSlotType.LEGS, properties));
		}

		static RegistryObject<Item> boots(ArmorMaterial tier, Item.Properties properties) {
			return ModItems.REGISTRY.register(tier.name().toLowerCase(Locale.US).concat("_boots"), armorFactory(tier, EquipmentSlotType.FEET, properties));
		}

		@SuppressWarnings("unchecked")
		private static Supplier<ArmorItem> armorFactory(ArmorMaterial tier, EquipmentSlotType slot, Item.Properties properties) {
			return tier.fullbright ? () -> new ArmorItem(tier, slot, properties) {
				@Override
				public <A extends BipedModel<?>> A getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlotType armorSlot, A _default) {
					return (A) new BipedModel(slot == EquipmentSlotType.LEGS ? 0.5F : 1.0F) {
						@Override
						public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
							super.render(matrixStackIn, bufferIn, 0xF000F0, packedOverlayIn, red, green, blue, alpha);
						}
					};
				}
			} : () -> new ArmorItem(tier, slot, properties);
		}

	}

}
