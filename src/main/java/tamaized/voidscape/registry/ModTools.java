package tamaized.voidscape.registry;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
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
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;
import tamaized.regutil.RegUtil;
import tamaized.regutil.RegistryClass;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.capability.Insanity;
import tamaized.voidscape.capability.SubCapability;
import tamaized.voidscape.entity.IchorBoltEntity;
import tamaized.voidscape.item.SpellTome;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
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

		public static boolean check(Tier tier) {
			return tier == VOIDIC_CRYSTAL ||
					tier == CHARRED ||
					tier == CORRUPT ||
					tier == TITANITE ||
					tier == ICHOR;
		}
	}

	public static final RegistryObject<Item> VOIDIC_CRYSTAL_SWORD = RegUtil.ToolAndArmorHelper.sword(ItemTier.VOIDIC_CRYSTAL, ModItems.ItemProps.LAVA_IMMUNE.properties().get(), 
					RegUtil.makeAttributeFactory(RegUtil.AttributeData.make(ModAttributes.VOIDIC_DMG, AttributeModifier.Operation.ADDITION, 1D)), tooltip -> {});
	public static final RegistryObject<Item> VOIDIC_CRYSTAL_BOW = RegUtil.ToolAndArmorHelper.bow(ItemTier.VOIDIC_CRYSTAL, ModItems.ItemProps.LAVA_IMMUNE.properties().get(), 
					RegUtil.makeAttributeFactory(RegUtil.AttributeData.make(ModAttributes.VOIDIC_ARROW_DMG, AttributeModifier.Operation.ADDITION, 1D)), tooltip -> {});
	public static final RegistryObject<Item> VOIDIC_CRYSTAL_XBOW = RegUtil.ToolAndArmorHelper.xbow(ItemTier.VOIDIC_CRYSTAL, ModItems.ItemProps.LAVA_IMMUNE.properties().get(), 
					RegUtil.makeAttributeFactory(RegUtil.AttributeData.make(ModAttributes.VOIDIC_ARROW_DMG, AttributeModifier.Operation.ADDITION, 1D)), tooltip -> {});
	public static final RegistryObject<Item> VOIDIC_CRYSTAL_SHIELD = RegUtil.ToolAndArmorHelper.shield(ItemTier.VOIDIC_CRYSTAL, ModItems.ItemProps.LAVA_IMMUNE.properties().get(), 
					RegUtil.makeAttributeFactory(RegUtil.AttributeData.make(ModAttributes.VOIDIC_RES, AttributeModifier.Operation.ADDITION, 1D)), tooltip -> {});
	public static final RegistryObject<Item> VOIDIC_CRYSTAL_AXE = RegUtil.ToolAndArmorHelper.axe(ItemTier.VOIDIC_CRYSTAL, ModItems.ItemProps.LAVA_IMMUNE.properties().get(), 
					RegUtil.makeAttributeFactory(RegUtil.AttributeData.make(ModAttributes.VOIDIC_DMG, AttributeModifier.Operation.ADDITION, 2D)), tooltip -> {});
	public static final RegistryObject<Item> VOIDIC_CRYSTAL_PICKAXE = RegUtil.ToolAndArmorHelper.pickaxe(ItemTier.VOIDIC_CRYSTAL, ModItems.ItemProps.LAVA_IMMUNE.properties().get(), 
					RegUtil.makeAttributeFactory(RegUtil.AttributeData.make(ModAttributes.VOIDIC_DMG, AttributeModifier.Operation.ADDITION, 1D)), tooltip -> {});

	public static final RegistryObject<Item> CHARRED_WARHAMMER = hammer(ItemTier.CHARRED, ModItems.ItemProps.LAVA_IMMUNE.properties().get(), 
					RegUtil.makeAttributeFactory(RegUtil.AttributeData.make(ModAttributes.VOIDIC_DMG, AttributeModifier.Operation.ADDITION, 1D)), tooltip -> {});

	public static final RegistryObject<Item> CORRUPT_SWORD = RegUtil.ToolAndArmorHelper.sword(ItemTier.CORRUPT, ModItems.ItemProps.LAVA_IMMUNE.properties().get(), 
					RegUtil.makeAttributeFactory(RegUtil.AttributeData.make(ModAttributes.VOIDIC_DMG, AttributeModifier.Operation.ADDITION, 2D)), tooltip -> {});
	public static final RegistryObject<Item> CORRUPT_BOW = RegUtil.ToolAndArmorHelper.bow(ItemTier.CORRUPT, ModItems.ItemProps.LAVA_IMMUNE.properties().get(), 
					RegUtil.makeAttributeFactory(RegUtil.AttributeData.make(ModAttributes.VOIDIC_ARROW_DMG, AttributeModifier.Operation.ADDITION, 2D)), tooltip -> {});
	public static final RegistryObject<Item> CORRUPT_XBOW = RegUtil.ToolAndArmorHelper.xbow(ItemTier.CORRUPT, ModItems.ItemProps.LAVA_IMMUNE.properties().get(), 
					RegUtil.makeAttributeFactory(RegUtil.AttributeData.make(ModAttributes.VOIDIC_ARROW_DMG, AttributeModifier.Operation.ADDITION, 2D)), tooltip -> {});
	public static final RegistryObject<Item> CORRUPT_AXE = RegUtil.ToolAndArmorHelper.axe(ItemTier.CORRUPT, ModItems.ItemProps.LAVA_IMMUNE.properties().get(), 
					RegUtil.makeAttributeFactory(RegUtil.AttributeData.make(ModAttributes.VOIDIC_DMG, AttributeModifier.Operation.ADDITION, 3D)), tooltip -> {});

	public static final RegistryObject<Item> TITANITE_SWORD = RegUtil.ToolAndArmorHelper.sword(ItemTier.TITANITE, ModItems.ItemProps.LAVA_IMMUNE.properties().get(), 
					RegUtil.makeAttributeFactory(RegUtil.AttributeData.make(ModAttributes.VOIDIC_DMG, AttributeModifier.Operation.ADDITION, 3D)),
					tooltip -> tooltip.tooltip().add(Component.translatable("voidscape.tooltip.textures").withStyle(ChatFormatting.ITALIC, ChatFormatting.DARK_GREEN)));
	public static final RegistryObject<Item> TITANITE_BOW = RegUtil.ToolAndArmorHelper.bow(ItemTier.TITANITE, ModItems.ItemProps.LAVA_IMMUNE.properties().get(), 
					RegUtil.makeAttributeFactory(RegUtil.AttributeData.make(ModAttributes.VOIDIC_ARROW_DMG, AttributeModifier.Operation.ADDITION, 3D)),
					tooltip -> tooltip.tooltip().add(Component.translatable("voidscape.tooltip.textures").withStyle(ChatFormatting.ITALIC, ChatFormatting.DARK_GREEN)));
	public static final RegistryObject<Item> TITANITE_XBOW = RegUtil.ToolAndArmorHelper.xbow(ItemTier.TITANITE, ModItems.ItemProps.LAVA_IMMUNE.properties().get(), 
					RegUtil.makeAttributeFactory(RegUtil.AttributeData.make(ModAttributes.VOIDIC_ARROW_DMG, AttributeModifier.Operation.ADDITION, 3D)),
					tooltip -> tooltip.tooltip().add(Component.translatable("voidscape.tooltip.textures").withStyle(ChatFormatting.ITALIC, ChatFormatting.DARK_GREEN)));
	public static final RegistryObject<Item> TITANITE_AXE = RegUtil.ToolAndArmorHelper.axe(ItemTier.TITANITE, ModItems.ItemProps.LAVA_IMMUNE.properties().get(), 
					RegUtil.makeAttributeFactory(RegUtil.AttributeData.make(ModAttributes.VOIDIC_DMG, AttributeModifier.Operation.ADDITION, 4D)), 
					tooltip -> tooltip.tooltip().add(Component.translatable("voidscape.tooltip.textures").withStyle(ChatFormatting.ITALIC, ChatFormatting.DARK_GREEN)));
	public static final RegistryObject<Item> TITANITE_PICKAXE = RegUtil.ToolAndArmorHelper.pickaxe(ItemTier.TITANITE, ModItems.ItemProps.LAVA_IMMUNE.properties().get(), 
					RegUtil.makeAttributeFactory(RegUtil.AttributeData.make(ModAttributes.VOIDIC_DMG, AttributeModifier.Operation.ADDITION, 2D)), 
					tooltip -> tooltip.tooltip().add(Component.translatable("voidscape.tooltip.textures").withStyle(ChatFormatting.ITALIC, ChatFormatting.DARK_GREEN)));
	public static final RegistryObject<Item> TITANITE_HOE = bonemealHoe(ItemTier.TITANITE, ModItems.ItemProps.LAVA_IMMUNE.properties().get(),
					RegUtil.makeAttributeFactory(RegUtil.AttributeData.make(ModAttributes.VOIDIC_DMG, AttributeModifier.Operation.ADDITION, 1D)), 
					tooltip -> tooltip.tooltip().add(Component.translatable("voidscape.tooltip.textures").withStyle(ChatFormatting.ITALIC, ChatFormatting.DARK_GREEN)));

	public static final RegistryObject<Item> ICHOR_TOME = ModItems.REGISTRY.register("ichor_tome", () -> new SpellTome(ModItems.ItemProps.LAVA_IMMUNE.properties().get()
			.durability(100), ModItems.ICHOR_CRYSTAL, 20 * 10, context -> context.level().addFreshEntity(new IchorBoltEntity(context.parent()))));
	public static final RegistryObject<Item> VOIDIC_TOME = ModItems.REGISTRY.register("voidic_tome", () -> new SpellTome(ModItems.ItemProps.LAVA_IMMUNE.properties().get()
			.durability(100), ModItems.VOIDIC_CRYSTAL, 20 * 45, context -> context.parent().addEffect(new MobEffectInstance(ModEffects.AURA.get(), 20 * 30))));
	public static final RegistryObject<Item> CORRUPT_TOME = ModItems.REGISTRY.register("corrupt_tome", () -> new SpellTome(ModItems.ItemProps.LAVA_IMMUNE.properties().get()
			.durability(100), ModItems.TENDRIL, 20 * 5, context -> {
		context.parent().addDeltaMovement(context.parent().getLookAngle().scale(2.5D));
		context.level().playSound(null, context.parent(), SoundEvents.FIREWORK_ROCKET_LAUNCH, SoundSource.PLAYERS, 1F, 0.75F + context.parent().getRandom().nextFloat() * 0.5F);
		context.parent().getCapability(SubCapability.CAPABILITY).ifPresent(cap -> cap.get(Voidscape.subCapInsanity).ifPresent(Insanity::enableLeapParticles));
		context.parent().addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 20 * 10));
	}));
	public static final RegistryObject<Item> TITANITE_TOME = ModItems.REGISTRY.register("titanite_tome", () -> new SpellTome(ModItems.ItemProps.LAVA_IMMUNE.properties().get()
			.durability(100), ModItems.TITANITE_SHARD, 20 * 45, context -> context.parent().addEffect(new MobEffectInstance(ModEffects.FORTIFIED.get(), 20 * 30))));
	public static final RegistryObject<Item> ICHOR_SWORD = RegUtil.ToolAndArmorHelper.sword(ItemTier.ICHOR, ModItems.ItemProps.LAVA_IMMUNE.properties().get(),
			RegUtil.makeAttributeFactory(RegUtil.AttributeData.make(ModAttributes.VOIDIC_DMG, AttributeModifier.Operation.ADDITION, 4D)), tooltip -> {});
	public static final RegistryObject<Item> ICHOR_BOW = RegUtil.ToolAndArmorHelper.bow(ItemTier.ICHOR, ModItems.ItemProps.LAVA_IMMUNE.properties().get(),
			RegUtil.makeAttributeFactory(RegUtil.AttributeData.make(ModAttributes.VOIDIC_ARROW_DMG, AttributeModifier.Operation.ADDITION, 4D)), tooltip -> {});
	public static final RegistryObject<Item> ICHOR_AXE = RegUtil.ToolAndArmorHelper.axe(ItemTier.ICHOR, ModItems.ItemProps.LAVA_IMMUNE.properties().get(),
			RegUtil.makeAttributeFactory(RegUtil.AttributeData.make(ModAttributes.VOIDIC_DMG, AttributeModifier.Operation.ADDITION, 5D)), tooltip -> {});
	public static final RegistryObject<Item> ICHOR_PICKAXE = RegUtil.ToolAndArmorHelper.pickaxe(ItemTier.ICHOR, ModItems.ItemProps.LAVA_IMMUNE.properties().get(),
							RegUtil.makeAttributeFactory(RegUtil.AttributeData.make(ModAttributes.VOIDIC_DMG, AttributeModifier.Operation.ADDITION, 3D)), tooltip -> {});

	@Override
	public void init(IEventBus bus) {
		MinecraftForge.EVENT_BUS.addListener((Consumer<PlayerInteractEvent.LeftClickBlock>) event -> {
			LAST_HIT_BLOCK_FACE.put(event.getEntity().getUUID(), event.getFace());
		});
		MinecraftForge.EVENT_BUS.addListener((Consumer<PlayerEvent.PlayerLoggedOutEvent>) event -> {
			LAST_HIT_BLOCK_FACE.remove(event.getEntity().getUUID());
		});
	}

	private static RegistryObject<Item> hammer(RegUtil.ItemTier tier, Item.Properties properties, Function<Integer, Multimap<Attribute, AttributeModifier>> factory, Consumer<RegUtil.ToolAndArmorHelper.TooltipContext> tooltipConsumer) {
		return ModItems.REGISTRY.register(tier.name().toLowerCase(Locale.US).concat("_warhammer"), () -> new LootingWarhammer(factory, tier, 7, -3.5F, properties, tooltipConsumer));
	}

	private static RegistryObject<Item> bonemealHoe(RegUtil.ItemTier tier, Item.Properties properties, Function<Integer, Multimap<Attribute, AttributeModifier>> factory, Consumer<RegUtil.ToolAndArmorHelper.TooltipContext> tooltipConsumer) {
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
							ModAdvancementTriggers.HOE_BONEMEAL_TRIGGER.trigger(player);
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
						map.putAll(factory.apply(null));
				}
				return map.build();
			}
		});
	}

	public static class LootingWarhammer extends PickaxeItem {

		private final Function<Integer, Multimap<Attribute, AttributeModifier>> factory;
		private final Consumer<RegUtil.ToolAndArmorHelper.TooltipContext> tooltipConsumer;

		public LootingWarhammer(Function<Integer, Multimap<Attribute, AttributeModifier>> factory, Tier tier, int attackDamage, float speed, Properties properties, Consumer<RegUtil.ToolAndArmorHelper.TooltipContext> tooltipConsumer) {
			super(tier, attackDamage, speed, properties);
			this.factory = factory;
			this.tooltipConsumer = tooltipConsumer;
		}

		@Override
		public boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
			return (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SWEEPING_EDGE, stack) > 0 && toolAction == ToolActions.SWORD_SWEEP) || super.canPerformAction(stack, toolAction);
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
			if (pl.level().isClientSide || !(pl instanceof final ServerPlayer player) || RegUtil.ToolAndArmorHelper.isBroken(stack))
				return super.onBlockStartBreak(stack, pos, pl);
			final ServerLevel level = player.serverLevel();
			final BlockState oState = level.getBlockState(pos);
			if (!isCorrectToolForDrops(stack, oState))
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
			if (this == CHARRED_WARHAMMER.get() && area.size() > 1)
				ModAdvancementTriggers.CHARRED_BONE_WAR_HAMMER_TRIGGER.trigger(player);
			// Using TCon's hardness division check
			area.stream().map(p -> Pair.of(p, level.getBlockState(p))).filter(p -> {
				final BlockState state = p.right();
				if (state.isAir())
					return false;
				final float h = state.getDestroySpeed(level, p.left());
				if (h < 0)
					return false;
				return (hardness == 0 ? h == 0 : hardness / h <= 3) && isCorrectToolForDrops(stack, state);
			}).forEach(p -> {
				final BlockPos blockPos = p.left();
				final BlockState state = p.right();
				int exp = net.minecraftforge.common.ForgeHooks.onBlockBreakEvent(level, player.gameMode.getGameModeForPlayer(), player, blockPos);
				if (exp != -1 && !player.blockActionRestricted(level, blockPos, player.gameMode.getGameModeForPlayer())) {
					if (player.isCreative()) {
						removeBlock(level, player, blockPos, false);
					} else {
						BlockEntity blockentity = level.getBlockEntity(blockPos);
						ItemStack cloneStack = stack.copy();
						boolean flag1 = state.canHarvestBlock(level, blockPos, player);
						stack.mineBlock(level, state, blockPos, player);
						if (stack.isEmpty() && !cloneStack.isEmpty())
							net.minecraftforge.event.ForgeEventFactory.onPlayerDestroyItem(player, cloneStack, InteractionHand.MAIN_HAND);
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
					map.putAll(factory.apply(null));
				}
			}

			return map.build();
		}
	}

}
