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
import tamaized.voidscape.item.SpellTomeItem;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class ModTools implements RegistryClass {

	public static final DeferredHolder<Item, Item> ICHOR_TOME = ModItems.REGISTRY.register("ichor_tome", () -> new SpellTomeItem(ModItems.ItemProps.LAVA_IMMUNE.properties().get()
			.durability(100), ModItems.ICHOR_CRYSTAL, 20 * 10, context -> context.level().addFreshEntity(new IchorBoltEntity(context.parent()))));
	public static final DeferredHolder<Item, Item> VOIDIC_TOME = ModItems.REGISTRY.register("voidic_tome", () -> new SpellTomeItem(ModItems.ItemProps.LAVA_IMMUNE.properties().get()
			.durability(100), ModItems.VOIDIC_CRYSTAL, 20 * 45, context -> context.parent().addEffect(new MobEffectInstance(ModEffects.AURA.get(), 20 * 30))));
	public static final DeferredHolder<Item, Item> CORRUPT_TOME = ModItems.REGISTRY.register("corrupt_tome", () -> new SpellTomeItem(ModItems.ItemProps.LAVA_IMMUNE.properties().get()
			.durability(100), ModItems.TENDRIL, 20 * 5, context -> {
		context.parent().addDeltaMovement(context.parent().getLookAngle().scale(2.5D));
		context.level().playSound(null, context.parent(), SoundEvents.FIREWORK_ROCKET_LAUNCH, SoundSource.PLAYERS, 1F, 0.75F + context.parent().getRandom().nextFloat() * 0.5F);
		context.parent().getData(ModDataAttachments.INSANITY).enableLeapParticles();
		context.parent().addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 20 * 10));
	}));
	public static final DeferredHolder<Item, Item> TITANITE_TOME = ModItems.REGISTRY.register("titanite_tome", () -> new SpellTomeItem(ModItems.ItemProps.LAVA_IMMUNE.properties().get()
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


	private static DeferredHolder<Item, Item> threeByThreeShovel(RegUtil.ItemTier tier, Item.Properties properties, BiFunction<Integer, ItemStack, Multimap<Attribute, AttributeModifier>> factory, Consumer<RegUtil.ToolAndArmorHelper.TooltipContext> tooltipConsumer) {
		return ModItems.REGISTRY.register(tier.name().toLowerCase(Locale.US).concat("_shovel"), () -> new ThreeByThreeShovel(factory, tier, 1.5F, -3.0F, properties, tooltipConsumer));
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

}
