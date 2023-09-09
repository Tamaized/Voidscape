package tamaized.voidscape.registry;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
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

	static class ItemTier {
		static final RegUtil.ItemTier VOIDIC_CRYSTAL = new RegUtil.ItemTier("voidic_crystal", 5, 2538, 9.5F, 5F, 17, () -> Ingredient.of(ModItems.VOIDIC_CRYSTAL.get()));
		static final RegUtil.ItemTier CHARRED = new RegUtil.ItemTier("charred", 5, 2538, 9.5F, 5F, 17, () -> Ingredient.of(ModItems.CHARRED_BONE.get()));
		static final RegUtil.ItemTier CORRUPT = new RegUtil.ItemTier("corrupt", 6, 3041, 10.0F, 6F, 19, () -> Ingredient.of(ModItems.TENDRIL.get()));
	}

	public static final RegistryObject<Item> VOIDIC_CRYSTAL_SWORD = RegUtil.ToolAndArmorHelper.
			sword(ItemTier.VOIDIC_CRYSTAL, ModItems.ItemProps.LAVA_IMMUNE.properties().get(), RegUtil.makeAttributeFactory(RegUtil.
					AttributeData.make(ModAttributes.VOIDIC_DMG, AttributeModifier.Operation.ADDITION, 1D)));
	public static final RegistryObject<Item> VOIDIC_CRYSTAL_BOW = RegUtil.ToolAndArmorHelper.
			bow(ItemTier.VOIDIC_CRYSTAL, ModItems.ItemProps.LAVA_IMMUNE.properties().get(), RegUtil.makeAttributeFactory(RegUtil.
					AttributeData.make(ModAttributes.VOIDIC_ARROW_DMG, AttributeModifier.Operation.ADDITION, 1D)));
	public static final RegistryObject<Item> VOIDIC_CRYSTAL_XBOW = RegUtil.ToolAndArmorHelper.
			xbow(ItemTier.VOIDIC_CRYSTAL, ModItems.ItemProps.LAVA_IMMUNE.properties().get(), RegUtil.makeAttributeFactory(RegUtil.
					AttributeData.make(ModAttributes.VOIDIC_ARROW_DMG, AttributeModifier.Operation.ADDITION, 1D)));
	public static final RegistryObject<Item> VOIDIC_CRYSTAL_SHIELD = RegUtil.ToolAndArmorHelper.
			shield(ItemTier.VOIDIC_CRYSTAL, ModItems.ItemProps.LAVA_IMMUNE.properties().get(), RegUtil.makeAttributeFactory(RegUtil.
					AttributeData.make(ModAttributes.VOIDIC_RES, AttributeModifier.Operation.ADDITION, 1D)));
	public static final RegistryObject<Item> VOIDIC_CRYSTAL_AXE = RegUtil.ToolAndArmorHelper.
			axe(ItemTier.VOIDIC_CRYSTAL, ModItems.ItemProps.LAVA_IMMUNE.properties().get(), RegUtil.makeAttributeFactory(RegUtil.
					AttributeData.make(ModAttributes.VOIDIC_DMG, AttributeModifier.Operation.ADDITION, 2D)));
	public static final RegistryObject<Item> VOIDIC_CRYSTAL_PICKAXE = RegUtil.ToolAndArmorHelper.
			pickaxe(ItemTier.VOIDIC_CRYSTAL, ModItems.ItemProps.LAVA_IMMUNE.properties().get(), RegUtil.makeAttributeFactory(RegUtil.
					AttributeData.make(ModAttributes.VOIDIC_DMG, AttributeModifier.Operation.ADDITION, 1D)));

	public static final RegistryObject<Item> CHARRED_WARHAMMER = hammer(ItemTier.CHARRED, ModItems.ItemProps.LAVA_IMMUNE.properties().get(), RegUtil.makeAttributeFactory(RegUtil.
					AttributeData.make(ModAttributes.VOIDIC_DMG, AttributeModifier.Operation.ADDITION, 1D)));

	public static final RegistryObject<Item> CORRUPT_SWORD = RegUtil.ToolAndArmorHelper.
			sword(ItemTier.CORRUPT, ModItems.ItemProps.LAVA_IMMUNE.properties().get(), RegUtil.makeAttributeFactory(RegUtil.
					AttributeData.make(ModAttributes.VOIDIC_DMG, AttributeModifier.Operation.ADDITION, 2D)));
	public static final RegistryObject<Item> CORRUPT_BOW = RegUtil.ToolAndArmorHelper.
			bow(ItemTier.CORRUPT, ModItems.ItemProps.LAVA_IMMUNE.properties().get(), RegUtil.makeAttributeFactory(RegUtil.
					AttributeData.make(ModAttributes.VOIDIC_ARROW_DMG, AttributeModifier.Operation.ADDITION, 2D)));
	public static final RegistryObject<Item> CORRUPT_XBOW = RegUtil.ToolAndArmorHelper.
			xbow(ItemTier.CORRUPT, ModItems.ItemProps.LAVA_IMMUNE.properties().get(), RegUtil.makeAttributeFactory(RegUtil.
					AttributeData.make(ModAttributes.VOIDIC_ARROW_DMG, AttributeModifier.Operation.ADDITION, 2D)));
	public static final RegistryObject<Item> CORRUPT_AXE = RegUtil.ToolAndArmorHelper.
			axe(ItemTier.CORRUPT, ModItems.ItemProps.LAVA_IMMUNE.properties().get(), RegUtil.makeAttributeFactory(RegUtil.
					AttributeData.make(ModAttributes.VOIDIC_DMG, AttributeModifier.Operation.ADDITION, 3D)));

	@Override
	public void init(IEventBus bus) {
		MinecraftForge.EVENT_BUS.addListener((Consumer<PlayerInteractEvent.LeftClickBlock>) event -> {
			LAST_HIT_BLOCK_FACE.put(event.getEntity().getUUID(), event.getFace());
		});
		MinecraftForge.EVENT_BUS.addListener((Consumer<PlayerEvent.PlayerLoggedOutEvent>) event -> {
			LAST_HIT_BLOCK_FACE.remove(event.getEntity().getUUID());
		});
	}

	private static RegistryObject<Item> hammer(RegUtil.ItemTier tier, Item.Properties properties, Function<Integer, Multimap<Attribute, AttributeModifier>> factory) {
		return ModItems.REGISTRY.register(tier.name().toLowerCase(Locale.US).concat("_warhammer"), () -> new LootingWarhammer(factory, tier, 7, -3.5F, properties));
	}

	public static class LootingWarhammer extends PickaxeItem {

		private final Function<Integer, Multimap<Attribute, AttributeModifier>> factory;

		public LootingWarhammer(Function<Integer, Multimap<Attribute, AttributeModifier>> factory, Tier tier, int attackDamage, float speed, Properties properties) {
			super(tier, attackDamage, speed, properties);
			this.factory = factory;
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
