package tamaized.voidscape.registry;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.UpgradeRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.StructurePieceType;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.IItemRenderProperties;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.client.entity.model.ModelArmorCorrupt;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class RegUtil {

	public static final CreativeModeTab CREATIVE_TAB = new CreativeModeTab(Voidscape.MODID.concat(".item_group")) {
		@Override
		public ItemStack makeIcon() {
			return new ItemStack(ModItems.VOIDIC_CRYSTAL.get());
		}
	};
	private static final UUID[] ARMOR_MODIFIER_UUID_PER_SLOT = new UUID[]{UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"), UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"), UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"), UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150"), UUID.fromString("86fda400-8542-4d95-b275-c6393de5b887")};
	private static final List<DeferredRegister<?>> REGISTERS = new ArrayList<>();
	private static final List<Runnable> CONFIGURED_FEATURES = new ArrayList<>();
	private static final Map<Item, List<RegistryObject<Item>>> BOWS = new HashMap<>() {{
		put(Items.BOW, new ArrayList<>());
		put(Items.CROSSBOW, new ArrayList<>());
	}};

	public static boolean isMyBow(ItemStack stack, Item check) {
		List<RegistryObject<Item>> list = BOWS.get(check);
		if (list == null)
			return false;
		for (RegistryObject<Item> o : list) {
			if (stack.is(o.get()))
				return true;
		}
		return false;
	}

	static <FC extends FeatureConfiguration, F extends Feature<FC>> LazyLoadedValue<ConfiguredFeature<?, ?>> registerConfiguredFeature(RegistryObject<F> feature, FC inst, UnaryOperator<ConfiguredFeature<?, ?>> config) {
		LazyLoadedValue<ConfiguredFeature<?, ?>> val = new LazyLoadedValue<>(() -> config.apply(new ConfiguredFeature<>(feature.get(), inst)));
		CONFIGURED_FEATURES.add(() -> Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, feature.getId(), val.get()));
		return val;
	}

	public static StructurePieceType registerStructurePiece(String name, StructurePieceType piece) {
		return Registry.register(Registry.STRUCTURE_PIECE, new ResourceLocation(Voidscape.MODID, name.toLowerCase(Locale.ROOT)), piece);
	}

	public static void register(IEventBus bus) {
		ModAttributes.classload();
		ModArmors.classload();
		ModBlocks.classload();
		ModDataSerializers.classload();
		ModEffects.classload();
		ModItems.classload();
		ModParticles.classload();
		ModSounds.classload();
		ModTools.classload();
		ModEntities.classload();
		ModBiomes.classload();
		ModSurfaceBuilders.classload();
		ModFeatures.classload();
		ModStructures.classload(bus);
		class FixedUpgradeRecipe extends UpgradeRecipe {
			public FixedUpgradeRecipe(ResourceLocation p_44523_, Ingredient p_44524_, Ingredient p_44525_, ItemStack p_44526_) {
				super(p_44523_, p_44524_, p_44525_, p_44526_);
			}

			@Override
			public ItemStack assemble(Container p_44531_) {
				ItemStack itemstack = getResultItem().copy();
				CompoundTag compoundtag = p_44531_.getItem(0).getTag();
				if (compoundtag != null)
					itemstack.getOrCreateTag().merge(compoundtag.copy());
				return itemstack;
			}
		}
		create(ForgeRegistries.RECIPE_SERIALIZERS).register("smithing", () -> new UpgradeRecipe.Serializer() {
			@Override
			public UpgradeRecipe fromJson(ResourceLocation p_44562_, JsonObject p_44563_) {
				Ingredient ingredient = Ingredient.fromJson(GsonHelper.getAsJsonObject(p_44563_, "base"));
				Ingredient ingredient1 = Ingredient.fromJson(GsonHelper.getAsJsonObject(p_44563_, "addition"));
				ItemStack itemstack = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(p_44563_, "result"));
				return new FixedUpgradeRecipe(p_44562_, ingredient, ingredient1, itemstack);
			}

			@Override
			public UpgradeRecipe fromNetwork(ResourceLocation p_44565_, FriendlyByteBuf p_44566_) {
				Ingredient ingredient = Ingredient.fromNetwork(p_44566_);
				Ingredient ingredient1 = Ingredient.fromNetwork(p_44566_);
				ItemStack itemstack = p_44566_.readItem();
				return new FixedUpgradeRecipe(p_44565_, ingredient, ingredient1, itemstack);
			}
		});
		for (DeferredRegister<?> register : REGISTERS)
			register.register(bus);
		bus.addGenericListener(Feature.class, (Consumer<RegistryEvent.Register<Feature<?>>>) event -> CONFIGURED_FEATURES.forEach(Runnable::run));
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
		DEFAULT(() -> new Item.Properties().tab(RegUtil.CREATIVE_TAB)),

		VOIDIC_CRYSTAL(() -> DEFAULT.get().fireResistant());

		private final Supplier<Item.Properties> properties;

		ItemProps(Supplier<Item.Properties> properties) {
			this.properties = properties;
		}

		Item.Properties get() {
			return properties.get();
		}
	}

	enum ItemTier implements Tier {
		VOIDIC_CRYSTAL(5, 2538, 9.5F, 5F, 17, () -> {
			return Ingredient.of(ModItems.VOIDIC_CRYSTAL.get());
		}),

		CORRUPT(6, 3041, 10.0F, 6F, 19, () -> {
			return Ingredient.of(ModItems.VOIDIC_CRYSTAL.get());
		});

		private final int harvestLevel;
		private final int maxUses;
		private final float efficiency;
		private final float attackDamage;
		private final int enchantability;
		private final LazyLoadedValue<Ingredient> repairMaterial;

		ItemTier(int harvestLevelIn, int maxUsesIn, float efficiencyIn, float attackDamageIn, int enchantabilityIn, Supplier<Ingredient> repairMaterialIn) {
			this.harvestLevel = harvestLevelIn;
			this.maxUses = maxUsesIn;
			this.efficiency = efficiencyIn;
			this.attackDamage = attackDamageIn;
			this.enchantability = enchantabilityIn;
			this.repairMaterial = new LazyLoadedValue<>(repairMaterialIn);
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

	enum ArmorMaterial implements net.minecraft.world.item.ArmorMaterial { // KB Resist max = 0.25 (0.25 * 4 = 1 = 100%)
		VOIDIC_CRYSTAL(ModItems.VOIDIC_CRYSTAL.getId().toString(), 39, new int[]{3, 6, 8, 3}, 17, SoundEvents.ARMOR_EQUIP_DIAMOND, 4F, 0.20F, () -> {
			return Ingredient.of(ModItems.VOIDIC_CRYSTAL.get());
		}, true),

		CORRUPT("corrupt", 41, new int[]{3, 6, 8, 3}, 19, SoundEvents.ARMOR_EQUIP_NETHERITE, 5F, 0.21F, () -> {
			return Ingredient.of(ModItems.VOIDIC_CRYSTAL.get());
		}, true) {
			@Override
			@OnlyIn(Dist.CLIENT)
			@SuppressWarnings("unchecked")
			public <A extends HumanoidModel<?>> A getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlot armorSlot, A _default) {
				ModelArmorCorrupt<LivingEntity> model = new ModelArmorCorrupt<>(Minecraft.getInstance().getEntityModels().
						bakeLayer(armorSlot == EquipmentSlot.LEGS ? ModEntities.ModelLayerLocations.MODEL_ARMOR_INSANE_INNER : ModEntities.ModelLayerLocations.MODEL_ARMOR_INSANE_OUTER));
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
						model.bodyToLeg.visible = true;
						model.rightleg.visible = true;
						model.leftleg.visible = true;
						break;
					case CHEST:
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
			public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
				return Voidscape.MODID.concat(":textures/models/armor/corrupt.png");
			}
		};

		private static final int[] MAX_DAMAGE_ARRAY = new int[]{13, 15, 16, 11};
		private final String name;
		private final int maxDamageFactor;
		private final int[] damageReductionAmountArray;
		private final int enchantability;
		private final SoundEvent soundEvent;
		private final float toughness;
		private final float knockbackResistance;
		private final LazyLoadedValue<Ingredient> repairMaterial;
		private final boolean fullbright;

		ArmorMaterial(String name, int maxDamageFactor, int[] damageReductionAmountArray, int enchantability, SoundEvent soundEvent, float toughness, float knockbackResistance, Supplier<Ingredient> repairMaterial, boolean fullbright) {
			this.name = name;
			this.maxDamageFactor = maxDamageFactor;
			this.damageReductionAmountArray = damageReductionAmountArray;
			this.enchantability = enchantability;
			this.soundEvent = soundEvent;
			this.toughness = toughness;
			this.knockbackResistance = knockbackResistance;
			this.repairMaterial = new LazyLoadedValue<>(repairMaterial);
			this.fullbright = fullbright;
		}

		@Nullable
		@OnlyIn(Dist.CLIENT)
		public <A extends HumanoidModel<?>> A getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlot armorSlot, A _default) {
			return null;
		}

		@Nullable
		@OnlyIn(Dist.CLIENT)
		public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
			return null;
		}

		@Override
		public int getDurabilityForSlot(EquipmentSlot slotIn) {
			return MAX_DAMAGE_ARRAY[slotIn.getIndex()] * this.maxDamageFactor;
		}

		@Override
		public int getDefenseForSlot(EquipmentSlot slotIn) {
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

	public static class ToolAndArmorHelper {

		public static boolean isBroken(ItemStack stack) {
			return stack.isDamageableItem() && stack.getDamageValue() >= stack.getMaxDamage() - 1;
		}

		static RegistryObject<Item> sword(ItemTier tier, Item.Properties properties, Function<Integer, Multimap<Attribute, AttributeModifier>> factory) {
			return ModItems.REGISTRY.register(tier.name().toLowerCase(Locale.US).concat("_sword"), () -> new SwordItem(tier, 3, -2.4F, properties) {

				@Override
				@OnlyIn(Dist.CLIENT)
				public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
					if (isBroken(stack))
						tooltip.add(new TranslatableComponent(Voidscape.MODID + ".tooltip.broken").withStyle(ChatFormatting.DARK_RED));
					super.appendHoverText(stack, worldIn, tooltip, flagIn);
				}

				@Override
				public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
					int remaining = (stack.getMaxDamage() - 1) - stack.getDamageValue();
					if (amount >= remaining)
						onBroken.accept(entity);
					return Math.min(remaining, amount);
				}

				@Override
				public float getDestroySpeed(ItemStack stack, BlockState state) {
					return isBroken(stack) ? 0 : super.getDestroySpeed(stack, state);
				}

				@Override
				public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
					return !isBroken(stack) && super.hurtEnemy(stack, target, attacker);
				}

				@Override
				public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
					ImmutableMultimap.Builder<Attribute, AttributeModifier> map = ImmutableMultimap.builder();
					if (!isBroken(stack)) {
						map.putAll(super.getDefaultAttributeModifiers(slot));
						if (slot == EquipmentSlot.MAINHAND)
							map.putAll(factory.apply(null));
					}
					return map.build();
				}
			});
		}

		static RegistryObject<Item> shield(ItemTier tier, Item.Properties properties, Function<Integer, Multimap<Attribute, AttributeModifier>> factory) {
			return ModItems.REGISTRY.register(tier.name().toLowerCase(Locale.US).concat("_shield"), () -> new ShieldItem(properties.defaultDurability(tier.getUses())) {

				@Override
				@OnlyIn(Dist.CLIENT)
				public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
					if (isBroken(stack))
						tooltip.add(new TranslatableComponent(Voidscape.MODID + ".tooltip.broken").withStyle(ChatFormatting.DARK_RED));
					super.appendHoverText(stack, worldIn, tooltip, flagIn);
				}

				@Override
				public boolean isShield(ItemStack stack, @Nullable LivingEntity entity) {
					return true;
				}

				@Override
				public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
					int remaining = (stack.getMaxDamage() - 1) - stack.getDamageValue();
					int dmg = Math.min(amount, 6);
					if (dmg >= remaining) {
						onBroken.accept(entity);
						entity.stopUsingItem();
					}
					return Math.min(remaining, dmg);
				}

				@Override
				public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
					final ItemStack stack = playerIn.getItemInHand(handIn);
					return isBroken(stack) ? InteractionResultHolder.fail(stack) : super.use(worldIn, playerIn, handIn);
				}

				@Override
				public int getEnchantmentValue() {
					return tier.getEnchantmentValue();
				}

				@Override
				public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
					return tier.getRepairIngredient().test(repair);
				}

				@Override
				public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
					ImmutableMultimap.Builder<Attribute, AttributeModifier> map = ImmutableMultimap.builder();
					if (!isBroken(stack)) {
						map.putAll(super.getAttributeModifiers(slot, stack));
						if (slot == EquipmentSlot.OFFHAND)
							map.putAll(factory.apply(4));
					}
					return map.build();
				}
			});
		}

		private static RegistryObject<Item> registerBow(Item item, RegistryObject<Item> o) {
			if (BOWS.containsKey(item))
				BOWS.get(item).add(o);
			return o;
		}

		static RegistryObject<Item> bow(ItemTier tier, Item.Properties properties, Function<Integer, Multimap<Attribute, AttributeModifier>> factory) {
			return registerBow(Items.BOW, ModItems.REGISTRY.register(tier.name().toLowerCase(Locale.US).concat("_bow"), () -> new BowItem(properties.defaultDurability(tier.getUses())) {

				@Override
				@OnlyIn(Dist.CLIENT)
				public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
					if (isBroken(stack))
						tooltip.add(new TranslatableComponent(Voidscape.MODID + ".tooltip.broken").withStyle(ChatFormatting.DARK_RED));
					super.appendHoverText(stack, worldIn, tooltip, flagIn);
				}

				@Override
				public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
					int remaining = (stack.getMaxDamage() - 1) - stack.getDamageValue();
					if (amount >= remaining)
						onBroken.accept(entity);
					return Math.min(remaining, amount);
				}

				@Override
				public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
					final ItemStack stack = playerIn.getItemInHand(handIn);
					return isBroken(stack) ? InteractionResultHolder.fail(stack) : super.use(worldIn, playerIn, handIn);
				}

				@Override
				public int getEnchantmentValue() {
					return tier.getEnchantmentValue();
				}

				@Override
				public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
					return tier.getRepairIngredient().test(repair) || super.isValidRepairItem(toRepair, repair);
				}

				@Override
				public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
					ImmutableMultimap.Builder<Attribute, AttributeModifier> map = ImmutableMultimap.builder();
					if (!isBroken(stack)) {
						map.putAll(super.getAttributeModifiers(slot, stack));
						if (slot == EquipmentSlot.MAINHAND)
							map.putAll(factory.apply(null));
					}
					return map.build();
				}
			}));
		}

		static RegistryObject<Item> xbow(ItemTier tier, Item.Properties properties, Function<Integer, Multimap<Attribute, AttributeModifier>> factory) {
			return registerBow(Items.CROSSBOW, ModItems.REGISTRY.register(tier.name().toLowerCase(Locale.US).concat("_xbow"), () -> new CrossbowItem(properties.defaultDurability(tier.getUses())) {

				@Override
				@OnlyIn(Dist.CLIENT)
				public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
					if (isBroken(stack))
						tooltip.add(new TranslatableComponent(Voidscape.MODID + ".tooltip.broken").withStyle(ChatFormatting.DARK_RED));
					super.appendHoverText(stack, worldIn, tooltip, flagIn);
				}

				@Override
				public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
					int remaining = (stack.getMaxDamage() - 1) - stack.getDamageValue();
					if (amount >= remaining)
						onBroken.accept(entity);
					return Math.min(remaining, amount);
				}

				@Override
				public boolean useOnRelease(ItemStack stack) {
					return stack.getItem() instanceof CrossbowItem;
				}

				@Override
				public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
					ItemStack itemstack = playerIn.getItemInHand(handIn);
					if (isBroken(itemstack))
						return InteractionResultHolder.fail(itemstack);
					if (isCharged(itemstack)) {
						performShooting(worldIn, playerIn, handIn, itemstack, itemstack.getItem() instanceof CrossbowItem && containsChargedProjectile(itemstack, Items.FIREWORK_ROCKET) ? 1.6F : 3.15F, 1.0F);
						setCharged(itemstack, false);
						return InteractionResultHolder.consume(itemstack);
					}
					return super.use(worldIn, playerIn, handIn);
				}

				@Override
				public int getEnchantmentValue() {
					return tier.getEnchantmentValue();
				}

				@Override
				public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
					return tier.getRepairIngredient().test(repair) || super.isValidRepairItem(toRepair, repair);
				}

				@Override
				public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
					ImmutableMultimap.Builder<Attribute, AttributeModifier> map = ImmutableMultimap.builder();
					if (!isBroken(stack)) {
						map.putAll(super.getAttributeModifiers(slot, stack));
						if (slot == EquipmentSlot.MAINHAND)
							map.putAll(factory.apply(null));
					}
					return map.build();
				}
			}));
		}

		static RegistryObject<Item> axe(ItemTier tier, Item.Properties properties, Function<Integer, Multimap<Attribute, AttributeModifier>> factory) {
			return ModItems.REGISTRY.register(tier.name().toLowerCase(Locale.US).concat("_axe"), () -> new LootingAxe(tier, 5F, -3.0F, properties) {

				@Override
				@OnlyIn(Dist.CLIENT)
				public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
					if (isBroken(stack))
						tooltip.add(new TranslatableComponent(Voidscape.MODID + ".tooltip.broken").withStyle(ChatFormatting.DARK_RED));
					super.appendHoverText(stack, worldIn, tooltip, flagIn);
				}

				@Override
				public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
					int remaining = (stack.getMaxDamage() - 1) - stack.getDamageValue();
					if (amount >= remaining)
						onBroken.accept(entity);
					return Math.min(remaining, amount);
				}

				@Override
				public float getDestroySpeed(ItemStack stack, BlockState state) {
					return isBroken(stack) ? 0 : super.getDestroySpeed(stack, state);
				}

				@Override
				public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
					if (!isBroken(stack)) {
						// This must remain an anon class to spoof the reobfuscator from mapping to the wrong SRG name
						//noinspection Convert2Lambda
						stack.hurtAndBreak(1, attacker, new Consumer<>() {
							@Override
							public void accept(LivingEntity entityIn1) {
								entityIn1.broadcastBreakEvent(EquipmentSlot.MAINHAND);
							}
						});
						return true;
					}
					return false;
				}

				@Override
				public InteractionResult useOn(UseOnContext context) {
					return isBroken(context.getItemInHand()) ? InteractionResult.FAIL : super.useOn(context);
				}

				@Override
				public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
					ImmutableMultimap.Builder<Attribute, AttributeModifier> map = ImmutableMultimap.builder();
					if (!isBroken(stack)) {
						map.putAll(super.getDefaultAttributeModifiers(slot));
						if (slot == EquipmentSlot.MAINHAND)
							map.putAll(factory.apply(null));
					}
					return map.build();
				}
			});
		}

		static RegistryObject<Item> pickaxe(ItemTier tier, Item.Properties properties, Function<Integer, Multimap<Attribute, AttributeModifier>> factory) {
			return ModItems.REGISTRY.register(tier.name().toLowerCase(Locale.US).concat("_pickaxe"), () -> new PickaxeItem(tier, 1, -2.8F, properties) {

				@Override
				@OnlyIn(Dist.CLIENT)
				public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
					if (isBroken(stack))
						tooltip.add(new TranslatableComponent(Voidscape.MODID + ".tooltip.broken").withStyle(ChatFormatting.DARK_RED));
					super.appendHoverText(stack, worldIn, tooltip, flagIn);
				}

				@Override
				public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
					int remaining = (stack.getMaxDamage() - 1) - stack.getDamageValue();
					if (amount >= remaining)
						onBroken.accept(entity);
					return Math.min(remaining, amount);
				}

				@Override
				public float getDestroySpeed(ItemStack stack, BlockState state) {
					return isBroken(stack) ? 0 : super.getDestroySpeed(stack, state);
				}

				@Override
				public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
					return !isBroken(stack) && super.hurtEnemy(stack, target, attacker);
				}

				@Override
				public InteractionResult useOn(UseOnContext context) {
					return isBroken(context.getItemInHand()) ? InteractionResult.FAIL : super.useOn(context);
				}

				@Override
				public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
					ImmutableMultimap.Builder<Attribute, AttributeModifier> map = ImmutableMultimap.builder();
					if (!isBroken(stack)) {
						map.putAll(super.getDefaultAttributeModifiers(slot));
						if (slot == EquipmentSlot.MAINHAND)
							map.putAll(factory.apply(null));
					}
					return map.build();
				}
			});
		}

		static RegistryObject<Item> shovel(ItemTier tier, Item.Properties properties, Function<Integer, Multimap<Attribute, AttributeModifier>> factory) {
			return ModItems.REGISTRY.register(tier.name().toLowerCase(Locale.US).concat("_shovel"), () -> new ShovelItem(tier, 1.5F, -3.0F, properties) {

				@Override
				@OnlyIn(Dist.CLIENT)
				public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
					if (isBroken(stack))
						tooltip.add(new TranslatableComponent(Voidscape.MODID + ".tooltip.broken").withStyle(ChatFormatting.DARK_RED));
					super.appendHoverText(stack, worldIn, tooltip, flagIn);
				}

				@Override
				public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
					int remaining = (stack.getMaxDamage() - 1) - stack.getDamageValue();
					if (amount >= remaining)
						onBroken.accept(entity);
					return Math.min(remaining, amount);
				}

				@Override
				public float getDestroySpeed(ItemStack stack, BlockState state) {
					return isBroken(stack) ? 0 : super.getDestroySpeed(stack, state);
				}

				@Override
				public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
					return !isBroken(stack) && super.hurtEnemy(stack, target, attacker);
				}

				@Override
				public InteractionResult useOn(UseOnContext context) {
					return isBroken(context.getItemInHand()) ? InteractionResult.FAIL : super.useOn(context);
				}

				@Override
				public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
					ImmutableMultimap.Builder<Attribute, AttributeModifier> map = ImmutableMultimap.builder();
					if (!isBroken(stack)) {
						map.putAll(super.getDefaultAttributeModifiers(slot));
						if (slot == EquipmentSlot.MAINHAND)
							map.putAll(factory.apply(null));
					}
					return map.build();
				}
			});
		}

		static RegistryObject<Item> hoe(ItemTier tier, Item.Properties properties, Function<Integer, Multimap<Attribute, AttributeModifier>> factory) {
			return ModItems.REGISTRY.register(tier.name().toLowerCase(Locale.US).concat("_hoe"), () -> new HoeItem(tier, -3, 0.0F, properties) {

				@Override
				@OnlyIn(Dist.CLIENT)
				public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
					if (isBroken(stack))
						tooltip.add(new TranslatableComponent(Voidscape.MODID + ".tooltip.broken").withStyle(ChatFormatting.DARK_RED));
					super.appendHoverText(stack, worldIn, tooltip, flagIn);
				}

				@Override
				public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
					int remaining = (stack.getMaxDamage() - 1) - stack.getDamageValue();
					if (amount >= remaining)
						onBroken.accept(entity);
					return Math.min(remaining, amount);
				}

				@Override
				public float getDestroySpeed(ItemStack stack, BlockState state) {
					return isBroken(stack) ? 0 : super.getDestroySpeed(stack, state);
				}

				@Override
				public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
					return !isBroken(stack) && super.hurtEnemy(stack, target, attacker);
				}

				@Override
				public InteractionResult useOn(UseOnContext context) {
					return isBroken(context.getItemInHand()) ? InteractionResult.FAIL : super.useOn(context);
				}

				@Override
				public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
					ImmutableMultimap.Builder<Attribute, AttributeModifier> map = ImmutableMultimap.builder();
					if (!isBroken(stack)) {
						map.putAll(super.getDefaultAttributeModifiers(slot));
						if (slot == EquipmentSlot.MAINHAND)
							map.putAll(factory.apply(null));
					}
					return map.build();
				}
			});
		}

		static RegistryObject<Item> helmet(ArmorMaterial tier, Item.Properties properties, Function<Integer, Multimap<Attribute, AttributeModifier>> factory) {
			return ModItems.REGISTRY.register(tier.name().toLowerCase(Locale.US).concat("_helmet"), armorFactory(tier, EquipmentSlot.HEAD, properties, factory));
		}

		static RegistryObject<Item> chest(ArmorMaterial tier, Item.Properties properties, Function<Integer, Multimap<Attribute, AttributeModifier>> factory) {
			return chest(tier, properties, factory, (stack, tick) -> false);
		}

		static RegistryObject<Item> chest(ArmorMaterial tier, Item.Properties properties, Function<Integer, Multimap<Attribute, AttributeModifier>> factory, BiPredicate<ItemStack, Boolean> elytra) {
			return ModItems.REGISTRY.register(tier.name().toLowerCase(Locale.US).concat("_chest"), armorFactory(tier, EquipmentSlot.CHEST, properties, factory, elytra));
		}

		static RegistryObject<Item> legs(ArmorMaterial tier, Item.Properties properties, Function<Integer, Multimap<Attribute, AttributeModifier>> factory) {
			return ModItems.REGISTRY.register(tier.name().toLowerCase(Locale.US).concat("_legs"), armorFactory(tier, EquipmentSlot.LEGS, properties, factory));
		}

		static RegistryObject<Item> boots(ArmorMaterial tier, Item.Properties properties, Function<Integer, Multimap<Attribute, AttributeModifier>> factory) {
			return ModItems.REGISTRY.register(tier.name().toLowerCase(Locale.US).concat("_boots"), armorFactory(tier, EquipmentSlot.FEET, properties, factory));
		}

		private static Supplier<ArmorItem> armorFactory(ArmorMaterial tier, EquipmentSlot slot, Item.Properties properties, Function<Integer, Multimap<Attribute, AttributeModifier>> factory) {
			return armorFactory(tier, slot, properties, factory, (stack, tick) -> false);
		}

		@SuppressWarnings("unchecked")
		private static Supplier<ArmorItem> armorFactory(ArmorMaterial tier, EquipmentSlot slot, Item.Properties properties, Function<Integer, Multimap<Attribute, AttributeModifier>> factory, BiPredicate<ItemStack, Boolean> elytra) {
			return () -> new ArmorItem(tier, slot, properties) {

				@Override
				public boolean elytraFlightTick(ItemStack stack, LivingEntity entity, int flightTicks) {
					return elytra.test(stack, true) || super.elytraFlightTick(stack, entity, flightTicks);
				}

				@Override
				public boolean canElytraFly(ItemStack stack, LivingEntity entity) {
					return elytra.test(stack, false) || super.canElytraFly(stack, entity);
				}

				@Override
				@OnlyIn(Dist.CLIENT)
				public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
					if (isBroken(stack))
						tooltip.add(new TranslatableComponent(Voidscape.MODID + ".tooltip.broken").withStyle(ChatFormatting.DARK_RED));
					if (elytra.test(stack, false))
						tooltip.add(new TranslatableComponent(Voidscape.MODID + ".tooltip.elytra").withStyle(ChatFormatting.DARK_AQUA));
					super.appendHoverText(stack, worldIn, tooltip, flagIn);
				}

				@Override
				public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
					int remaining = (stack.getMaxDamage() - 1) - stack.getDamageValue();
					if (amount >= remaining)
						onBroken.accept(entity);
					return Math.min(remaining, amount);
				}

				@Override
				public void onArmorTick(ItemStack stack, Level world, Player player) {
					if (isBroken(stack)) {
						if (!player.inventory.add(stack))
							Containers.dropItemStack(world, player.position().x(), player.position().y(), player.position().z(), stack);
						else
							stack.shrink(1);
					}
				}

				@Override
				public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot equipmentSlot, ItemStack stack) {
					ImmutableMultimap.Builder<Attribute, AttributeModifier> map = ImmutableMultimap.builder();
					if (!isBroken(stack)) {
						map.putAll(super.getDefaultAttributeModifiers(equipmentSlot));
						if (equipmentSlot == slot)
							map.putAll(factory.apply(equipmentSlot.getIndex()));
					}
					return map.build();
				}

				@Override
				@OnlyIn(Dist.CLIENT)
				public Object getRenderPropertiesInternal() {
					return new IItemRenderProperties() {
						@Override
						public <A extends HumanoidModel<?>> A getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlot armorSlot, A _default) {
							A tierModel = tier.getArmorModel(entityLiving, itemStack, armorSlot, _default);
							return tierModel != null ? tierModel : tier.fullbright ? (A) new HumanoidModel<>(Minecraft.getInstance().getEntityModels().
									bakeLayer(slot == EquipmentSlot.LEGS ? ModelLayers.PLAYER_INNER_ARMOR : ModelLayers.PLAYER_OUTER_ARMOR)) {
								@Override
								public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
									super.renderToBuffer(matrixStackIn, bufferIn, 0xF000F0, packedOverlayIn, red, green, blue, alpha);
								}
							} : IItemRenderProperties.super.getArmorModel(entityLiving, itemStack, armorSlot, _default);
						}
					};
				}

				@Nullable
				@Override
				@OnlyIn(Dist.CLIENT)
				public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
					String tierTexture = tier.getArmorTexture(stack, entity, slot, type);
					return tierTexture != null ? tierTexture : super.getArmorTexture(stack, entity, slot, type);
				}
			};
		}

		public static abstract class LootingAxe extends AxeItem {

			public LootingAxe(Tier tier, float attackDamageIn, float attackSpeedIn, Properties builder) {
				super(tier, attackDamageIn, attackSpeedIn, builder);
			}

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

	record AttributeData(Supplier<ModAttribute> attribute, AttributeModifier.Operation op, double value) {
		static AttributeData make(Supplier<ModAttribute> attribute, AttributeModifier.Operation op, double value) {
			return new AttributeData(attribute, op, value);
		}
	}

}
