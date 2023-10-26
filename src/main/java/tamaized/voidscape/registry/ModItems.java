package tamaized.voidscape.registry;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;
import tamaized.regutil.RegUtil;
import tamaized.regutil.RegistryClass;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.capability.SubCapability;
import tamaized.voidscape.entity.StrangePearlEntity;

import java.util.List;

public class ModItems implements RegistryClass {

	public static class ItemProps {
		public static final RegUtil.ItemProps DEFAULT = new RegUtil.ItemProps(Item.Properties::new);
		public static final RegUtil.ItemProps LAVA_IMMUNE = new RegUtil.ItemProps(() -> DEFAULT.properties().get().fireResistant());
	}

	public static final DeferredRegister<Item> REGISTRY = RegUtil.create(ForgeRegistries.ITEMS);
	public static final RegistryObject<Item> VOIDIC_CRYSTAL = REGISTRY.register("voidic_crystal", () -> new Item(ItemProps.LAVA_IMMUNE.properties().get()));
	public static final RegistryObject<Item> VOIDIC_TEMPLATE = REGISTRY.register("voidic_template", () -> new Item(ItemProps.LAVA_IMMUNE.properties().get()));
	public static final RegistryObject<Item> ETHEREAL_ESSENCE = REGISTRY.register("ethereal_essence", () -> new Item(ItemProps.LAVA_IMMUNE.properties().get()) {
		@Override
		public InteractionResult useOn(UseOnContext context) {
			if (Voidscape.checkForVoidDimension(context.getLevel()) && context.getLevel().getBlockState(context.getClickedPos()).is(Blocks.BEDROCK)) {
				context.getLevel().setBlockAndUpdate(context.getClickedPos(), ModBlocks.VOIDIC_CRYSTAL_ORE.get().defaultBlockState());
				if (context.getPlayer() == null || !context.getPlayer().isCreative())
					context.getItemInHand().shrink(1);
				context.getLevel().playSound(null, context.getClickedPos(), SoundEvents.BEACON_POWER_SELECT, SoundSource.BLOCKS, 1F, 0.5F + context.getLevel().getRandom().nextFloat() * 0.5F);
				if (context.getLevel() instanceof ServerLevel)
					for (int i = 0; i < 50; i++)
						((ServerLevel) context.getLevel()).sendParticles(ParticleTypes.WITCH, context.
								getClickedPos().getX() + context.getLevel().getRandom().nextFloat(), context.
								getClickedPos().getY() + context.getLevel().getRandom().nextFloat(), context.
								getClickedPos().getZ() + context.getLevel().getRandom().nextFloat(), 0, 0, 0, 0, 1F);
				if (context.getPlayer() instanceof ServerPlayer serverPlayer)
					ModAdvancementTriggers.ETHEREAL_ESSENCE_TRIGGER.trigger(serverPlayer);
				return InteractionResult.SUCCESS;
			}
			return super.useOn(context);
		}
	});
	public static final RegistryObject<Item> CHARRED_BONE = REGISTRY.register("charred_bone", () -> new Item(ItemProps.LAVA_IMMUNE.properties().get()) {
		@Override
		public InteractionResult useOn(UseOnContext context) {
			Player player = context.getPlayer();
			Level level = context.getLevel();
			BlockPos blockpos = context.getClickedPos();
			BlockPos blockpos1 = blockpos.relative(context.getClickedFace());

			if (canCreatePortal(level.getBlockState(blockpos1), level, blockpos1)) {
				level.playSound(player, blockpos1, SoundEvents.TRIDENT_THUNDER, SoundSource.BLOCKS, 1F, 0.75F + context.getLevel().getRandom().nextFloat() * 0.5F);
				ModBlocks.PORTAL.get().tryToCreatePortal(level, blockpos1);
				ItemStack stack = context.getItemInHand();

				if (player instanceof ServerPlayer serverPlayer) {
					ModAdvancementTriggers.ACTIVATE_PORTAL_TRIGGER.trigger(serverPlayer);
					if (!serverPlayer.isCreative())
						stack.shrink(1);
				}

				return InteractionResult.SUCCESS;
			} else {
				return InteractionResult.FAIL;
			}
		}

		public static boolean canCreatePortal(BlockState state, Level level, BlockPos pos) {
			boolean flag = false;

			for (Direction direction : Direction.Plane.HORIZONTAL) {
				BlockState s = level.getBlockState(pos.relative(direction));
				if ((s.is(ModBlocks.VOIDIC_CRYSTAL_BLOCK.get()) || s.is(ModBlocks.FRAGILE_VOIDIC_CRYSTAL_BLOCK.get())) && ModBlocks.PORTAL.get().isPortal(level, pos) != null) {
					flag = true;
				}
			}

			return state.isAir() && flag;
		}
	});
	public static final RegistryObject<Item> CHARRED_WARHAMMER_HEAD = REGISTRY.register("charred_warhammer_head", () -> new Item(ItemProps.LAVA_IMMUNE.properties().get()));
	public static final RegistryObject<Item> TENDRIL = REGISTRY.register("tendril", () -> new Item(ItemProps.LAVA_IMMUNE.properties().get()));
	public static final RegistryObject<Item> TITANITE_CHUNK = REGISTRY.register("titanite_chunk", () -> new Item(ItemProps.LAVA_IMMUNE.properties().get()));
	public static final RegistryObject<Item> TITANITE_SHARD = REGISTRY.register("titanite_shard", () -> new Item(ItemProps.LAVA_IMMUNE.properties().get()));
	public static final RegistryObject<Item> FLESH_CHUNK = REGISTRY.register("flesh_chunk", () -> new Item(ItemProps.LAVA_IMMUNE.properties().get()));
	public static final RegistryObject<Item> ICHOR = REGISTRY.register("ichor", () -> new Item(ItemProps.LAVA_IMMUNE.properties().get()));
	public static final RegistryObject<Item> ICHOR_CRYSTAL = REGISTRY.register("ichor_crystal", () -> new Item(ItemProps.LAVA_IMMUNE.properties().get()));
	public static final RegistryObject<Item> STRANGE_PEARL = REGISTRY.register("strange_pearl", () -> new Item(ItemProps.LAVA_IMMUNE.properties().get()) {
		@Override
		public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
			ItemStack itemstack = pPlayer.getItemInHand(pHand);
			pLevel.playSound(null, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), SoundEvents.SNOWBALL_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (pLevel.getRandom().nextFloat() * 0.4F + 0.8F));
			if (!pLevel.isClientSide) {
				StrangePearlEntity pearl = new StrangePearlEntity(pLevel, pPlayer);
				pearl.shootFromRotation(pPlayer, pPlayer.getXRot(), pPlayer.getYRot(), 0.0F, 1.5F, 1.0F);
				pLevel.addFreshEntity(pearl);
			}

			pPlayer.awardStat(Stats.ITEM_USED.get(this));
			if (!pPlayer.isCreative()) {
				itemstack.shrink(1);
			}

			return InteractionResultHolder.sidedSuccess(itemstack, pLevel.isClientSide());
		}
	});
	public static final RegistryObject<Item> ASTRAL_SHARDS = REGISTRY.register("astral_shards", () -> new Item(ItemProps.LAVA_IMMUNE.properties().get()) {
		@Override
		public InteractionResult useOn(UseOnContext context) {
			if (Voidscape.checkForVoidDimension(context.getLevel()) && context.getLevel().getBlockState(context.getClickedPos()).is(ModBlocks.ANTIROCK.get())) {
				context.getLevel().setBlockAndUpdate(context.getClickedPos(), ModBlocks.ASTRALROCK.get().defaultBlockState());
				if (context.getPlayer() == null || !context.getPlayer().isCreative())
					context.getItemInHand().shrink(1);
				context.getLevel().playSound(null, context.getClickedPos(), SoundEvents.BEACON_POWER_SELECT, SoundSource.BLOCKS, 1F, 0.5F + context.getLevel().getRandom().nextFloat() * 0.5F);
				if (context.getLevel() instanceof ServerLevel)
					for (int i = 0; i < 50; i++)
						((ServerLevel) context.getLevel()).sendParticles(ParticleTypes.WITCH, context.
								getClickedPos().getX() + context.getLevel().getRandom().nextFloat(), context.
								getClickedPos().getY() + context.getLevel().getRandom().nextFloat(), context.
								getClickedPos().getZ() + context.getLevel().getRandom().nextFloat(), 0, 0, 0, 0, 1F);
				return InteractionResult.SUCCESS;
			}
			return super.useOn(context);
		}
	});
	public static final RegistryObject<Item> ASTRAL_ESSENCE = REGISTRY.register("astral_essence", () -> new Item(ItemProps.LAVA_IMMUNE.properties().get()));
	public static final RegistryObject<Item> ASTRAL_CRYSTAL = REGISTRY.register("astral_crystal", () -> new Item(ItemProps.LAVA_IMMUNE.properties().get()));

	public static final RegistryObject<Item> ETHEREAL_FRUIT_VOID = REGISTRY.register("ethereal_fruit_void", () -> new Item(ItemProps.LAVA_IMMUNE.properties().get().
			food(new FoodProperties.Builder().nutrition(4).saturationMod(0.3F).alwaysEat().build())) {
		@Override
		public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
			ItemStack itemstack = super.finishUsingItem(stack, level, entity);
			if (!level.isClientSide) {
				entity.getCapability(SubCapability.CAPABILITY).ifPresent(cap -> cap.get(Voidscape.subCapInsanity)
						.ifPresent(data -> data.setInfusion(data.getInfusion() + (150 * (2F - (float) entity.getAttributeValue(ModAttributes.VOIDIC_INFUSION_RES.get()))))));
			}
			return itemstack;
		}

		@Override
		public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
			super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
			pTooltipComponents.add(Component.translatable("voidscape.tooltip.textures").withStyle(ChatFormatting.ITALIC, ChatFormatting.DARK_GREEN));
		}
	});
	public static final RegistryObject<Item> ETHEREAL_FRUIT_NULL = REGISTRY.register("ethereal_fruit_null", () -> new Item(ItemProps.LAVA_IMMUNE.properties().get().
			food(new FoodProperties.Builder().nutrition(4).saturationMod(0.3F).alwaysEat().build())) {
		@Override
		public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
			ItemStack itemstack = super.finishUsingItem(stack, level, entity);
			if (!level.isClientSide) {
				entity.getCapability(SubCapability.CAPABILITY).ifPresent(cap -> cap.get(Voidscape.subCapInsanity)
						.ifPresent(data -> data.setInfusion(data.getInfusion() - 150)));
			}
			return itemstack;
		}

		@Override
		public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
			super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
			pTooltipComponents.add(Component.translatable("voidscape.tooltip.textures").withStyle(ChatFormatting.ITALIC, ChatFormatting.DARK_GREEN));
		}
	});
	public static final RegistryObject<Item> ETHEREAL_FRUIT_OVERWORLD = REGISTRY.register("ethereal_fruit_overworld", () -> new Item(ItemProps.LAVA_IMMUNE.properties().get().
			food(new FoodProperties.Builder().nutrition(4).saturationMod(0.3F).alwaysEat().build())) {
		@Override
		public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
			ItemStack itemstack = super.finishUsingItem(stack, level, entity);
			if (!level.isClientSide) {
				entity.getCapability(SubCapability.CAPABILITY).ifPresent(cap -> cap.get(Voidscape.subCapInsanity)
						.ifPresent(data -> data.setParanoia(data.getParanoia() - 150)));
			}
			return itemstack;
		}
	});
	public static final RegistryObject<Item> ETHEREAL_FRUIT_NETHER = REGISTRY.register("ethereal_fruit_nether", () -> new Item(ItemProps.LAVA_IMMUNE.properties().get().
			food(new FoodProperties.Builder().nutrition(4).saturationMod(0.3F).alwaysEat().build())) {
		@Override
		public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
			ItemStack itemstack = super.finishUsingItem(stack, level, entity);
			if (!level.isClientSide) {
				entity.getCapability(SubCapability.CAPABILITY).ifPresent(cap -> cap.get(Voidscape.subCapInsanity)
						.ifPresent(data -> data.setParanoia(data.getParanoia() + (150 * (2F - (float) entity.getAttributeValue(ModAttributes.VOIDIC_PARANOIA_RES.get()))))));
			}
			return itemstack;
		}
	});
	public static final RegistryObject<Item> ETHEREAL_FRUIT_END = REGISTRY.register("ethereal_fruit_end", () -> new Item(ItemProps.LAVA_IMMUNE.properties().get().
			food(new FoodProperties.Builder().nutrition(4).saturationMod(0.3F).alwaysEat().build())) {
		@Override
		public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
			ItemStack itemstack = super.finishUsingItem(stack, level, entity);
			if (!level.isClientSide) {
				entity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 20 * 20, 3));
			}
			return itemstack;
		}
	});

	@Override
	public void init(IEventBus bus) {

	}

}
