package tamaized.voidscape.registry;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.ToolAction;
import net.neoforged.neoforge.common.ToolActions;
import net.neoforged.neoforge.event.EventHooks;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import tamaized.regutil.RegUtil;
import tamaized.regutil.RegistryClass;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.entity.IchorBoltEntity;
import tamaized.voidscape.item.SpellTome;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public class ModTools implements RegistryClass {

	private static final Map<UUID, Direction> LAST_HIT_BLOCK_FACE = new HashMap<>();

	public static class ItemTier {
		public static final RegUtil.ItemTier VOIDIC_CRYSTAL = new RegUtil.ItemTier("voidic_crystal", 5, 2538, 9.5F, 5F, 17, () -> Ingredient.of(ModItems.VOIDIC_CRYSTAL.get()));
		public static final RegUtil.ItemTier CHARRED = new RegUtil.ItemTier("charred", 5, 2538, 9.5F, 5F, 17, () -> Ingredient.of(ModItems.CHARRED_BONE.get()));
		public static final RegUtil.ItemTier CORRUPT = new RegUtil.ItemTier("corrupt", 6, 3041, 10.0F, 6F, 19, () -> Ingredient.of(ModItems.TENDRIL.get()));
		public static final RegUtil.ItemTier TITANITE = new RegUtil.ItemTier("titanite", 7, 3544, 10.5F, 7F, 21, () -> Ingredient.of(ModItems.TITANITE_SHARD.get()));
		public static final RegUtil.ItemTier ICHOR = new RegUtil.ItemTier("ichor", 8, 4047, 11.0F, 8F, 23, () -> Ingredient.of(ModItems.ICHOR_CRYSTAL.get()));
		public static final RegUtil.ItemTier ASTRAL = new RegUtil.ItemTier("astral", 9, 4550, 11.5F, 9F, 25, () -> Ingredient.of(ModItems.ASTRAL_CRYSTAL.get()));

		public static boolean check(Tier tier) {
			return tier == VOIDIC_CRYSTAL ||
					tier == CHARRED ||
					tier == CORRUPT ||
					tier == TITANITE ||
					tier == ICHOR ||
					tier == ASTRAL;
		}
	}

	public static final DeferredHolder<Item, Item> VOIDIC_CRYSTAL_SWORD = RegUtil.ToolAndArmorHelper.sword(ItemTier.VOIDIC_CRYSTAL, ModItems.ItemProps.LAVA_IMMUNE.properties().get(),
					RegUtil.makeAttributeFactory(
							RegUtil.AttributeData.make(ModAttributes.VOIDIC_DMG, AttributeModifier.Operation.ADDITION, 1D),
							RegUtil.AttributeData.make(ModTools::fang, ModAttributes.VOIDIC_INFUSION, AttributeModifier.Operation.MULTIPLY_BASE, 0.15D)
					), tooltip -> {});
	public static final DeferredHolder<Item, Item> VOIDIC_CRYSTAL_BOW = RegUtil.ToolAndArmorHelper.bow(ItemTier.VOIDIC_CRYSTAL, ModItems.ItemProps.LAVA_IMMUNE.properties().get(), 
					RegUtil.makeAttributeFactory(
							RegUtil.AttributeData.make(ModAttributes.VOIDIC_ARROW_DMG, AttributeModifier.Operation.ADDITION, 1D),
							RegUtil.AttributeData.make(ModTools::fang, ModAttributes.VOIDIC_INFUSION, AttributeModifier.Operation.MULTIPLY_BASE, 0.15D)
					), tooltip -> {});
	public static final DeferredHolder<Item, Item> VOIDIC_CRYSTAL_XBOW = RegUtil.ToolAndArmorHelper.xbow(ItemTier.VOIDIC_CRYSTAL, ModItems.ItemProps.LAVA_IMMUNE.properties().get(), 
					RegUtil.makeAttributeFactory(
							RegUtil.AttributeData.make(ModAttributes.VOIDIC_ARROW_DMG, AttributeModifier.Operation.ADDITION, 1D),
							RegUtil.AttributeData.make(ModTools::fang, ModAttributes.VOIDIC_INFUSION, AttributeModifier.Operation.MULTIPLY_BASE, 0.15D)
					), tooltip -> {});
	public static final DeferredHolder<Item, Item> VOIDIC_CRYSTAL_SHIELD = RegUtil.ToolAndArmorHelper.shield(ItemTier.VOIDIC_CRYSTAL, ModItems.ItemProps.LAVA_IMMUNE.properties().get(), 
					RegUtil.makeAttributeFactory(RegUtil.AttributeData.make(ModAttributes.VOIDIC_RES, AttributeModifier.Operation.ADDITION, 1D)), tooltip -> {});
	public static final DeferredHolder<Item, Item> VOIDIC_CRYSTAL_AXE = RegUtil.ToolAndArmorHelper.axe(ItemTier.VOIDIC_CRYSTAL, ModItems.ItemProps.LAVA_IMMUNE.properties().get(),
					RegUtil.makeAttributeFactory(
							RegUtil.AttributeData.make(ModAttributes.VOIDIC_DMG, AttributeModifier.Operation.ADDITION, 2D),
							RegUtil.AttributeData.make(ModTools::fang, ModAttributes.VOIDIC_INFUSION, AttributeModifier.Operation.MULTIPLY_BASE, 0.15D)
					), tooltip -> {});
	public static final DeferredHolder<Item, Item> VOIDIC_CRYSTAL_PICKAXE = RegUtil.ToolAndArmorHelper.pickaxe(ItemTier.VOIDIC_CRYSTAL, ModItems.ItemProps.LAVA_IMMUNE.properties().get(), 
					RegUtil.makeAttributeFactory(RegUtil.AttributeData.make(ModAttributes.VOIDIC_DMG, AttributeModifier.Operation.ADDITION, 1D)), tooltip -> {});

	public static final DeferredHolder<Item, Item> CHARRED_WARHAMMER = hammer(ItemTier.CHARRED, ModItems.ItemProps.LAVA_IMMUNE.properties().get(), 
					RegUtil.makeAttributeFactory(
							RegUtil.AttributeData.make(ModAttributes.VOIDIC_DMG, AttributeModifier.Operation.ADDITION, 3D),
							RegUtil.AttributeData.make(ModTools::fang, ModAttributes.VOIDIC_INFUSION, AttributeModifier.Operation.MULTIPLY_BASE, 0.15D)
					), tooltip -> {});

	public static final DeferredHolder<Item, Item> CORRUPT_SWORD = RegUtil.ToolAndArmorHelper.sword(ItemTier.CORRUPT, ModItems.ItemProps.LAVA_IMMUNE.properties().get(), 
					RegUtil.makeAttributeFactory(
							RegUtil.AttributeData.make(ModAttributes.VOIDIC_DMG, AttributeModifier.Operation.ADDITION, 2D),
							RegUtil.AttributeData.make(ModTools::fang, ModAttributes.VOIDIC_INFUSION, AttributeModifier.Operation.MULTIPLY_BASE, 0.15D)
					), tooltip -> {});
	public static final DeferredHolder<Item, Item> CORRUPT_BOW = RegUtil.ToolAndArmorHelper.bow(ItemTier.CORRUPT, ModItems.ItemProps.LAVA_IMMUNE.properties().get(), 
					RegUtil.makeAttributeFactory(
							RegUtil.AttributeData.make(ModAttributes.VOIDIC_ARROW_DMG, AttributeModifier.Operation.ADDITION, 2D),
							RegUtil.AttributeData.make(ModTools::fang, ModAttributes.VOIDIC_INFUSION, AttributeModifier.Operation.MULTIPLY_BASE, 0.15D)
					), tooltip -> {});
	public static final DeferredHolder<Item, Item> CORRUPT_XBOW = RegUtil.ToolAndArmorHelper.xbow(ItemTier.CORRUPT, ModItems.ItemProps.LAVA_IMMUNE.properties().get(), 
					RegUtil.makeAttributeFactory(
							RegUtil.AttributeData.make(ModAttributes.VOIDIC_ARROW_DMG, AttributeModifier.Operation.ADDITION, 2D),
							RegUtil.AttributeData.make(ModTools::fang, ModAttributes.VOIDIC_INFUSION, AttributeModifier.Operation.MULTIPLY_BASE, 0.15D)
					), tooltip -> {});
	public static final DeferredHolder<Item, Item> CORRUPT_AXE = RegUtil.ToolAndArmorHelper.axe(ItemTier.CORRUPT, ModItems.ItemProps.LAVA_IMMUNE.properties().get(), 
					RegUtil.makeAttributeFactory(
							RegUtil.AttributeData.make(ModAttributes.VOIDIC_DMG, AttributeModifier.Operation.ADDITION, 3D),
							RegUtil.AttributeData.make(ModTools::fang, ModAttributes.VOIDIC_INFUSION, AttributeModifier.Operation.MULTIPLY_BASE, 0.15D)
					), tooltip -> {});

	public static final DeferredHolder<Item, Item> TITANITE_SWORD = RegUtil.ToolAndArmorHelper.sword(ItemTier.TITANITE, ModItems.ItemProps.LAVA_IMMUNE.properties().get(), 
					RegUtil.makeAttributeFactory(
							RegUtil.AttributeData.make(ModAttributes.VOIDIC_DMG, AttributeModifier.Operation.ADDITION, 3D),
							RegUtil.AttributeData.make(ModTools::fang, ModAttributes.VOIDIC_INFUSION, AttributeModifier.Operation.MULTIPLY_BASE, 0.15D)
					), tooltip -> {});
	public static final DeferredHolder<Item, Item> TITANITE_BOW = RegUtil.ToolAndArmorHelper.bow(ItemTier.TITANITE, ModItems.ItemProps.LAVA_IMMUNE.properties().get(), 
					RegUtil.makeAttributeFactory(
							RegUtil.AttributeData.make(ModAttributes.VOIDIC_ARROW_DMG, AttributeModifier.Operation.ADDITION, 3D),
							RegUtil.AttributeData.make(ModTools::fang, ModAttributes.VOIDIC_INFUSION, AttributeModifier.Operation.MULTIPLY_BASE, 0.15D)
					), tooltip -> {});
	public static final DeferredHolder<Item, Item> TITANITE_XBOW = RegUtil.ToolAndArmorHelper.xbow(ItemTier.TITANITE, ModItems.ItemProps.LAVA_IMMUNE.properties().get(), 
					RegUtil.makeAttributeFactory(
							RegUtil.AttributeData.make(ModAttributes.VOIDIC_ARROW_DMG, AttributeModifier.Operation.ADDITION, 3D),
							RegUtil.AttributeData.make(ModTools::fang, ModAttributes.VOIDIC_INFUSION, AttributeModifier.Operation.MULTIPLY_BASE, 0.15D)
					), tooltip -> {});
	public static final DeferredHolder<Item, Item> TITANITE_AXE = RegUtil.ToolAndArmorHelper.axe(ItemTier.TITANITE, ModItems.ItemProps.LAVA_IMMUNE.properties().get(), 
					RegUtil.makeAttributeFactory(
							RegUtil.AttributeData.make(ModAttributes.VOIDIC_DMG, AttributeModifier.Operation.ADDITION, 4D),
							RegUtil.AttributeData.make(ModTools::fang, ModAttributes.VOIDIC_INFUSION, AttributeModifier.Operation.MULTIPLY_BASE, 0.15D)
					), tooltip -> {});
	public static final DeferredHolder<Item, Item> TITANITE_PICKAXE = RegUtil.ToolAndArmorHelper.pickaxe(ItemTier.TITANITE, ModItems.ItemProps.LAVA_IMMUNE.properties().get(), 
					RegUtil.makeAttributeFactory(RegUtil.AttributeData.make(ModAttributes.VOIDIC_DMG, AttributeModifier.Operation.ADDITION, 2D)), tooltip -> {});
	public static final DeferredHolder<Item, Item> TITANITE_HOE = bonemealHoe(ItemTier.TITANITE, ModItems.ItemProps.LAVA_IMMUNE.properties().get(),
					RegUtil.makeAttributeFactory(RegUtil.AttributeData.make(ModAttributes.VOIDIC_DMG, AttributeModifier.Operation.ADDITION, 1D)), tooltip -> {});

	public static final DeferredHolder<Item, Item> ICHOR_TOME = ModItems.REGISTRY.register("ichor_tome", () -> new SpellTome(ModItems.ItemProps.LAVA_IMMUNE.properties().get()
			.durability(100), ModItems.ICHOR_CRYSTAL, 20 * 10, context -> context.level().addFreshEntity(new IchorBoltEntity(context.parent()))));
	public static final DeferredHolder<Item, Item> VOIDIC_TOME = ModItems.REGISTRY.register("voidic_tome", () -> new SpellTome(ModItems.ItemProps.LAVA_IMMUNE.properties().get()
			.durability(100), ModItems.VOIDIC_CRYSTAL, 20 * 45, context -> context.parent().addEffect(new MobEffectInstance(ModEffects.AURA.get(), 20 * 30))));
	public static final DeferredHolder<Item, Item> CORRUPT_TOME = ModItems.REGISTRY.register("corrupt_tome", () -> new SpellTome(ModItems.ItemProps.LAVA_IMMUNE.properties().get()
			.durability(100), ModItems.TENDRIL, 20 * 5, context -> {
		context.parent().addDeltaMovement(context.parent().getLookAngle().scale(2.5D));
		context.level().playSound(null, context.parent(), SoundEvents.FIREWORK_ROCKET_LAUNCH, SoundSource.PLAYERS, 1F, 0.75F + context.parent().getRandom().nextFloat() * 0.5F);
		context.parent().getData(ModDataAttachments.INSANITY).enableLeapParticles();
		context.parent().addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 20 * 10));
	}));
	public static final DeferredHolder<Item, Item> TITANITE_TOME = ModItems.REGISTRY.register("titanite_tome", () -> new SpellTome(ModItems.ItemProps.LAVA_IMMUNE.properties().get()
			.durability(100), ModItems.TITANITE_SHARD, 20 * 45, context -> context.parent().addEffect(new MobEffectInstance(ModEffects.FORTIFIED.get(), 20 * 30))));
	public static final DeferredHolder<Item, Item> ICHOR_SWORD = RegUtil.ToolAndArmorHelper.sword(ItemTier.ICHOR, ModItems.ItemProps.LAVA_IMMUNE.properties().get(),
			RegUtil.makeAttributeFactory(
					RegUtil.AttributeData.make(ModAttributes.VOIDIC_DMG, AttributeModifier.Operation.ADDITION, 4D),
					RegUtil.AttributeData.make(ModTools::fang, ModAttributes.VOIDIC_INFUSION, AttributeModifier.Operation.MULTIPLY_BASE, 0.15D)
			), tooltip -> {});
	public static final DeferredHolder<Item, Item> ICHOR_BOW = RegUtil.ToolAndArmorHelper.bow(ItemTier.ICHOR, ModItems.ItemProps.LAVA_IMMUNE.properties().get(),
			RegUtil.makeAttributeFactory(
					RegUtil.AttributeData.make(ModAttributes.VOIDIC_ARROW_DMG, AttributeModifier.Operation.ADDITION, 4D),
					RegUtil.AttributeData.make(ModTools::fang, ModAttributes.VOIDIC_INFUSION, AttributeModifier.Operation.MULTIPLY_BASE, 0.15D)
			), tooltip -> {});
	public static final DeferredHolder<Item, Item> ICHOR_XBOW = RegUtil.ToolAndArmorHelper.xbow(ItemTier.ICHOR, ModItems.ItemProps.LAVA_IMMUNE.properties().get(),
			RegUtil.makeAttributeFactory(
					RegUtil.AttributeData.make(ModAttributes.VOIDIC_ARROW_DMG, AttributeModifier.Operation.ADDITION, 4D),
					RegUtil.AttributeData.make(ModTools::fang, ModAttributes.VOIDIC_INFUSION, AttributeModifier.Operation.MULTIPLY_BASE, 0.15D)
			), tooltip -> {});
	public static final DeferredHolder<Item, Item> ICHOR_AXE = RegUtil.ToolAndArmorHelper.axe(ItemTier.ICHOR, ModItems.ItemProps.LAVA_IMMUNE.properties().get(),
			RegUtil.makeAttributeFactory(
					RegUtil.AttributeData.make(ModAttributes.VOIDIC_DMG, AttributeModifier.Operation.ADDITION, 5D),
					RegUtil.AttributeData.make(ModTools::fang, ModAttributes.VOIDIC_INFUSION, AttributeModifier.Operation.MULTIPLY_BASE, 0.15D)
			), tooltip -> {});
	public static final DeferredHolder<Item, Item> ICHOR_PICKAXE = RegUtil.ToolAndArmorHelper.pickaxe(ItemTier.ICHOR, ModItems.ItemProps.LAVA_IMMUNE.properties().get(),
							RegUtil.makeAttributeFactory(RegUtil.AttributeData.make(ModAttributes.VOIDIC_DMG, AttributeModifier.Operation.ADDITION, 3D)), tooltip -> {});

	public static final DeferredHolder<Item, Item> ASTRAL_SWORD = RegUtil.ToolAndArmorHelper.sword(ItemTier.ASTRAL, ModItems.ItemProps.LAVA_IMMUNE.properties().get(),
			RegUtil.makeAttributeFactory(
					RegUtil.AttributeData.make(ModAttributes.VOIDIC_DMG, AttributeModifier.Operation.ADDITION, 5D),
					RegUtil.AttributeData.make(ModTools::fang, ModAttributes.VOIDIC_INFUSION, AttributeModifier.Operation.MULTIPLY_BASE, 0.15D)
			), tooltip -> {});
	public static final DeferredHolder<Item, Item> ASTRAL_AXE = RegUtil.ToolAndArmorHelper.axe(ItemTier.ASTRAL, ModItems.ItemProps.LAVA_IMMUNE.properties().get(),
			RegUtil.makeAttributeFactory(
					RegUtil.AttributeData.make(ModAttributes.VOIDIC_DMG, AttributeModifier.Operation.ADDITION, 6D),
					RegUtil.AttributeData.make(ModTools::fang, ModAttributes.VOIDIC_INFUSION, AttributeModifier.Operation.MULTIPLY_BASE, 0.15D)
			), tooltip -> {});
	public static final DeferredHolder<Item, Item> ASTRAL_PICKAXE = RegUtil.ToolAndArmorHelper.pickaxe(ItemTier.ASTRAL, ModItems.ItemProps.LAVA_IMMUNE.properties().get(),
			RegUtil.makeAttributeFactory(RegUtil.AttributeData.make(ModAttributes.VOIDIC_DMG, AttributeModifier.Operation.ADDITION, 4D)), tooltip -> {});
	public static final DeferredHolder<Item, Item> ASTRAL_SHOVEL = threeByThreeShovel(ItemTier.ASTRAL, ModItems.ItemProps.LAVA_IMMUNE.properties().get(),
			RegUtil.makeAttributeFactory(RegUtil.AttributeData.make(ModAttributes.VOIDIC_DMG, AttributeModifier.Operation.ADDITION, 3D)), tooltip -> {});
	public static final DeferredHolder<Item, Item> ASTRAL_BOW = RegUtil.ToolAndArmorHelper.bow(ItemTier.ASTRAL, ModItems.ItemProps.LAVA_IMMUNE.properties().get(),
			RegUtil.makeAttributeFactory(
					RegUtil.AttributeData.make(ModAttributes.VOIDIC_ARROW_DMG, AttributeModifier.Operation.ADDITION, 5D),
					RegUtil.AttributeData.make(ModTools::fang, ModAttributes.VOIDIC_INFUSION, AttributeModifier.Operation.MULTIPLY_BASE, 0.15D)
			), tooltip -> {});
	public static final DeferredHolder<Item, Item> ASTRAL_XBOW = RegUtil.ToolAndArmorHelper.xbow(ItemTier.ASTRAL, ModItems.ItemProps.LAVA_IMMUNE.properties().get(),
			RegUtil.makeAttributeFactory(
					RegUtil.AttributeData.make(ModAttributes.VOIDIC_ARROW_DMG, AttributeModifier.Operation.ADDITION, 5D),
					RegUtil.AttributeData.make(ModTools::fang, ModAttributes.VOIDIC_INFUSION, AttributeModifier.Operation.MULTIPLY_BASE, 0.15D)
			), tooltip -> {});

	@Override
	public void init(IEventBus bus) {
		NeoForge.EVENT_BUS.addListener(PlayerInteractEvent.LeftClickBlock.class, event -> {
			LAST_HIT_BLOCK_FACE.put(event.getEntity().getUUID(), event.getFace());
		});
		NeoForge.EVENT_BUS.addListener(PlayerEvent.PlayerLoggedOutEvent.class, event -> {
			LAST_HIT_BLOCK_FACE.remove(event.getEntity().getUUID());
		});
	}

	private static DeferredHolder<Item, Item> hammer(RegUtil.ItemTier tier, Item.Properties properties, BiFunction<Integer, ItemStack, Multimap<Attribute, AttributeModifier>> factory, Consumer<RegUtil.ToolAndArmorHelper.TooltipContext> tooltipConsumer) {
		return ModItems.REGISTRY.register(tier.name().toLowerCase(Locale.US).concat("_warhammer"), () -> new LootingWarhammer(factory, tier, 7, -3.5F, properties, tooltipConsumer));
	}

	private static DeferredHolder<Item, Item> threeByThreeShovel(RegUtil.ItemTier tier, Item.Properties properties, BiFunction<Integer, ItemStack, Multimap<Attribute, AttributeModifier>> factory, Consumer<RegUtil.ToolAndArmorHelper.TooltipContext> tooltipConsumer) {
		return ModItems.REGISTRY.register(tier.name().toLowerCase(Locale.US).concat("_shovel"), () -> new ThreeByThreeShovel(factory, tier, 1.5F, -3.0F, properties, tooltipConsumer));
	}

	private static DeferredHolder<Item, Item> bonemealHoe(RegUtil.ItemTier tier, Item.Properties properties, BiFunction<Integer, ItemStack, Multimap<Attribute, AttributeModifier>> factory, Consumer<RegUtil.ToolAndArmorHelper.TooltipContext> tooltipConsumer) {
		return ModItems.REGISTRY.register(tier.name().toLowerCase(Locale.US).concat("_hoe"), () -> new HoeItem(tier, -3, 0.0F, properties) {

			@Override
			@OnlyIn(Dist.CLIENT)
			public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
				if (RegUtil.ToolAndArmorHelper.isBroken(stack))
					tooltip.add(Component.translatable(Voidscape.MODID + ".tooltip.broken").withStyle(ChatFormatting.DARK_RED));
				tooltipConsumer.accept(new RegUtil.ToolAndArmorHelper.TooltipContext(stack, worldIn, tooltip, flagIn));
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
				return RegUtil.ToolAndArmorHelper.isBroken(stack) ? 0 : super.getDestroySpeed(stack, state);
			}

			@Override
			public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
				return !RegUtil.ToolAndArmorHelper.isBroken(stack) && super.hurtEnemy(stack, target, attacker);
			}

			@Override
			public InteractionResult useOn(UseOnContext context) {
				if (RegUtil.ToolAndArmorHelper.isBroken(context.getItemInHand()))
				return InteractionResult.FAIL;
				InteractionResult result = context.getPlayer() != null && context.getPlayer().isShiftKeyDown() ? InteractionResult.PASS : super.useOn(context);
				if (result == InteractionResult.PASS) {
					result = Items.BONE_MEAL.useOn(new UseOnContext(
							context.getLevel(),
							context.getPlayer(),
							context.getHand(),
							new ItemStack(Items.BONE_MEAL),
							new BlockHitResult(
									context.getClickLocation(),
									context.getHorizontalDirection(),
									context.getClickedPos(),
									context.isInside()
							)));
					if ((result == InteractionResult.SUCCESS || result == InteractionResult.CONSUME) && context.getPlayer() != null) {
						// This must remain an anon class to spoof the reobfuscator from mapping to the wrong SRG name
						//noinspection Convert2Lambda
						context.getItemInHand().hurtAndBreak(20, context.getPlayer(), new Consumer<LivingEntity>() {
							@Override
							public void accept(LivingEntity entityIn1) {
								entityIn1.broadcastBreakEvent(EquipmentSlot.MAINHAND);
							}
						});
						if (context.getPlayer() instanceof ServerPlayer player)
							ModAdvancementTriggers.HOE_BONEMEAL_TRIGGER.get().trigger(player);
					}
					return result;
				}
				return result;
			}

			@Override
			public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
				ImmutableMultimap.Builder<Attribute, AttributeModifier> map = ImmutableMultimap.builder();
				if (!RegUtil.ToolAndArmorHelper.isBroken(stack)) {
					map.putAll(super.getDefaultAttributeModifiers(slot));
					if (slot == EquipmentSlot.MAINHAND)
						map.putAll(factory.apply(null, stack));
				}
				return map.build();
			}
		});
	}

	public static class LootingWarhammer extends PickaxeItem {

		private final BiFunction<Integer, ItemStack, Multimap<Attribute, AttributeModifier>> factory;
		private final Consumer<RegUtil.ToolAndArmorHelper.TooltipContext> tooltipConsumer;

		public LootingWarhammer(BiFunction<Integer, ItemStack, Multimap<Attribute, AttributeModifier>> factory, Tier tier, int attackDamage, float speed, Properties properties, Consumer<RegUtil.ToolAndArmorHelper.TooltipContext> tooltipConsumer) {
			super(tier, attackDamage, speed, properties);
			this.factory = factory;
			this.tooltipConsumer = tooltipConsumer;
		}

		@Override
		public boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
			return (stack.getEnchantmentLevel(Enchantments.SWEEPING_EDGE) > 0 && toolAction == ToolActions.SWORD_SWEEP) || super.canPerformAction(stack, toolAction);
		}

		@Override
		@OnlyIn(Dist.CLIENT)
		public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
			if (RegUtil.ToolAndArmorHelper.isBroken(stack)) {
				tooltip.add((Component.translatable(Voidscape.MODID + ".tooltip.broken")).withStyle(ChatFormatting.DARK_RED));
			}
			tooltipConsumer.accept(new RegUtil.ToolAndArmorHelper.TooltipContext(stack, worldIn, tooltip, flagIn));
			super.appendHoverText(stack, worldIn, tooltip, flagIn);
		}

		@Override
		public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
			int remaining = stack.getMaxDamage() - 1 - stack.getDamageValue();
			if (amount >= remaining) {
				onBroken.accept(entity);
			}

			return Math.min(remaining, amount);
		}

		@Override
		public float getDestroySpeed(ItemStack stack, BlockState state) {
			return RegUtil.ToolAndArmorHelper.isBroken(stack) ? 0.0F : super.getDestroySpeed(stack, state) / 3F;
		}

		@Override
		public boolean onBlockStartBreak(ItemStack stack, BlockPos pos, Player pl) {
			Boolean state = ModTools.threeByThreeBreak(this, stack, pos, pl);
			if (state == null)
				return super.onBlockStartBreak(stack, pos, pl);
			else return state;
		}

		@Override
		public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
			return !RegUtil.ToolAndArmorHelper.isBroken(stack) && super.hurtEnemy(stack, target, attacker);
		}

		@Override
		public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
			ImmutableMultimap.Builder<Attribute, AttributeModifier> map = ImmutableMultimap.builder();
			if (!RegUtil.ToolAndArmorHelper.isBroken(stack)) {
				map.putAll(super.getDefaultAttributeModifiers(slot));
				if (slot == EquipmentSlot.MAINHAND) {
					map.putAll(factory.apply(null, stack));
				}
			}

			return map.build();
		}
	}

	public static class ThreeByThreeShovel extends ShovelItem {

		private final BiFunction<Integer, ItemStack, Multimap<Attribute, AttributeModifier>> factory;
		private final Consumer<RegUtil.ToolAndArmorHelper.TooltipContext> tooltipConsumer;

		public ThreeByThreeShovel(BiFunction<Integer, ItemStack, Multimap<Attribute, AttributeModifier>> factory, Tier tier, float attackDamage, float speed, Properties properties, Consumer<RegUtil.ToolAndArmorHelper.TooltipContext> tooltipConsumer) {
			super(tier, attackDamage, speed, properties);
			this.factory = factory;
			this.tooltipConsumer = tooltipConsumer;
		}

		@Override
		@OnlyIn(Dist.CLIENT)
		public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
			if (RegUtil.ToolAndArmorHelper.isBroken(stack)) {
				tooltip.add((Component.translatable(Voidscape.MODID + ".tooltip.broken")).withStyle(ChatFormatting.DARK_RED));
			}
			tooltipConsumer.accept(new RegUtil.ToolAndArmorHelper.TooltipContext(stack, worldIn, tooltip, flagIn));
			super.appendHoverText(stack, worldIn, tooltip, flagIn);
		}

		@Override
		public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
			int remaining = stack.getMaxDamage() - 1 - stack.getDamageValue();
			if (amount >= remaining) {
				onBroken.accept(entity);
			}

			return Math.min(remaining, amount);
		}

		@Override
		public float getDestroySpeed(ItemStack stack, BlockState state) {
			return RegUtil.ToolAndArmorHelper.isBroken(stack) ? 0.0F : super.getDestroySpeed(stack, state) / 3F;
		}

		@Override
		public boolean onBlockStartBreak(ItemStack stack, BlockPos pos, Player pl) {
			Boolean state = ModTools.threeByThreeBreak(this, stack, pos, pl);
			if (state == null)
				return super.onBlockStartBreak(stack, pos, pl);
			else return state;
		}

		@Override
		public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
			return !RegUtil.ToolAndArmorHelper.isBroken(stack) && super.hurtEnemy(stack, target, attacker);
		}

		@Override
		public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
			ImmutableMultimap.Builder<Attribute, AttributeModifier> map = ImmutableMultimap.builder();
			if (!RegUtil.ToolAndArmorHelper.isBroken(stack)) {
				map.putAll(super.getDefaultAttributeModifiers(slot));
				if (slot == EquipmentSlot.MAINHAND) {
					map.putAll(factory.apply(null, stack));
				}
			}

			return map.build();
		}
	}

	// Null return = call super
	@Nullable
	public static Boolean threeByThreeBreak(Item item, ItemStack stack, BlockPos pos, Player pl) {
		if (pl.level().isClientSide || !(pl instanceof final ServerPlayer player) || player.isShiftKeyDown() || RegUtil.ToolAndArmorHelper.isBroken(stack))
			return null;
		final ServerLevel level = player.serverLevel();
		final BlockState oState = level.getBlockState(pos);
		if (!item.isCorrectToolForDrops(stack, oState))
			return false;
		final float hardness = oState.getDestroySpeed(level, pos);
		List<BlockPos> area = new ArrayList<>();
		switch (LAST_HIT_BLOCK_FACE.get(player.getUUID())) {
			default -> area.add(pos);
			case DOWN, UP -> {
				for (int x = -1; x <= 1; x++)
					for (int z = -1; z <= 1; z++)
						area.add(pos.offset(x, 0, z));
			}
			case EAST, WEST -> {
				for (int y = -1; y <= 1; y++)
					for (int z = -1; z <= 1; z++)
						area.add(pos.offset(0, y, z));
			}
			case NORTH, SOUTH -> {
				for (int y = -1; y <= 1; y++)
					for (int x = -1; x <= 1; x++)
						area.add(pos.offset(x, y, 0));
			}
		}
		if (area.size() > 1)
			ModAdvancementTriggers.THREE_BY_THREE.get().trigger(player, stack);
		// Using TCon's hardness division check
		area.stream().map(p -> Pair.of(p, level.getBlockState(p))).filter(p -> {
			final BlockState state = p.right();
			if (state.isAir())
				return false;
			final float h = state.getDestroySpeed(level, p.left());
			if (h < 0)
				return false;
			return (hardness == 0 ? h == 0 : h / hardness <= 3) && item.isCorrectToolForDrops(stack, state);
		}).forEach(p -> {
			final BlockPos blockPos = p.left();
			final BlockState state = p.right();
			int exp = CommonHooks.onBlockBreakEvent(level, player.gameMode.getGameModeForPlayer(), player, blockPos);
			if (exp != -1 && !player.blockActionRestricted(level, blockPos, player.gameMode.getGameModeForPlayer())) {
				if (player.isCreative()) {
					removeBlock(level, player, blockPos, false);
				} else {
					BlockEntity blockentity = level.getBlockEntity(blockPos);
					ItemStack cloneStack = stack.copy();
					boolean flag1 = state.canHarvestBlock(level, blockPos, player);
					stack.mineBlock(level, state, blockPos, player);
					if (stack.isEmpty() && !cloneStack.isEmpty())
						EventHooks.onPlayerDestroyItem(player, cloneStack, InteractionHand.MAIN_HAND);
					boolean flag = removeBlock(level, player, blockPos, flag1);

					if (flag && flag1) {
						state.getBlock().playerDestroy(level, player, blockPos, state, blockentity, cloneStack);
					}

					if (flag && exp > 0)
						state.getBlock().popExperience(level, blockPos, exp);

					level.globalLevelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, blockPos, Block.getId(state));
					player.connection.send(new ClientboundBlockUpdatePacket(level, blockPos));

				}
			}
		});
		return true;
	}

	private static boolean removeBlock(Level level, Player player, BlockPos pos, boolean canHarvest) {
		BlockState state = level.getBlockState(pos);
		boolean removed = state.onDestroyedByPlayer(level, pos, player, canHarvest, level.getFluidState(pos));
		if (removed)
			state.getBlock().destroy(level, pos, state);
		return removed;
	}

	public static boolean fang(ItemStack stack) {
		if (stack.isEmpty())
			return false;
		CompoundTag nbt = stack.getTagElement(Voidscape.MODID);
		return nbt != null && nbt.getBoolean("fang");
	}

}
