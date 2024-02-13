package tamaized.voidscape.registry;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
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
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.registries.DeferredRegister;
import tamaized.regutil.RegUtil;
import tamaized.regutil.RegistryClass;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.block.PortalBlock;
import tamaized.voidscape.data.Insanity;
import tamaized.voidscape.entity.StrangePearlEntity;
import tamaized.voidscape.network.client.ClientPacketSendParticles;
import tamaized.voidscape.registry.block.ModBlocksThunderForestBiome;
import tamaized.voidscape.world.ConfigurablePortalShape;

import java.util.Optional;
import java.util.function.Supplier;

public class ModItems implements RegistryClass {

	public static class ItemProps {
		public static final RegUtil.ItemProps DEFAULT = new RegUtil.ItemProps(Item.Properties::new);
		public static final RegUtil.ItemProps LAVA_IMMUNE = new RegUtil.ItemProps(() -> DEFAULT.properties().get().fireResistant());
	}

	public static final DeferredRegister<Item> REGISTRY = RegUtil.create(Registries.ITEM);
	public static final Supplier<Item> VOIDIC_CRYSTAL = REGISTRY.register("voidic_crystal", () -> new Item(ItemProps.LAVA_IMMUNE.properties().get()));
	public static final Supplier<Item> VOIDIC_TEMPLATE = REGISTRY.register("voidic_template", () -> new Item(ItemProps.LAVA_IMMUNE.properties().get()));
	public static final Supplier<Item> ETHEREAL_ESSENCE = REGISTRY.register("ethereal_essence", () -> new Item(ItemProps.LAVA_IMMUNE.properties().get()) {
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
					ModAdvancementTriggers.ETHEREAL_ESSENCE_TRIGGER.get().trigger(serverPlayer);
				return InteractionResult.SUCCESS;
			}
			return super.useOn(context);
		}
	});
	public static final Supplier<Item> ETHEREAL_SPIDER_EGGS = REGISTRY.register("ethereal_spider_eggs", () -> new Item(ItemProps.LAVA_IMMUNE.properties().get()) {
		@Override
		public InteractionResult useOn(UseOnContext context) {
			if (Voidscape.checkForVoidDimension(context.getLevel())) {
				BlockState state = context.getLevel().getBlockState(context.getClickedPos());
				if (state.is(Blocks.BEDROCK) || state.is(ModBlocksThunderForestBiome.THUNDER_NYLIUM.get())) {
					context.getLevel().removeBlock(context.getClickedPos(), false);
					if (context.getPlayer() == null || !context.getPlayer().isCreative())
						context.getItemInHand().shrink(1);
					context.getLevel().playSound(null, context.getClickedPos(), SoundEvents.CONDUIT_ATTACK_TARGET, SoundSource.BLOCKS, 1F, 0.5F + context.getLevel().getRandom().nextFloat() * 0.5F);
					if (context.getLevel() instanceof ServerLevel) {
						ClientPacketSendParticles particles = new ClientPacketSendParticles();
						for (int i = 0; i < 200; i++)
							particles.queueParticle(ParticleTypes.ASH, false,
									context.getClickedPos().getX() + context.getLevel().getRandom().nextFloat(),
									context.getClickedPos().getY() + context.getLevel().getRandom().nextFloat(),
									context.getClickedPos().getZ() + context.getLevel().getRandom().nextFloat(),
									0, 0, 0);
						PacketDistributor.TRACKING_CHUNK.with(context.getLevel().getChunkAt(context.getClickedPos())).send(particles);
					}
					if (context.getPlayer() instanceof ServerPlayer serverPlayer)
						ModAdvancementTriggers.ETHEREAL_SPIDER_EGGS_TRIGGER.get().trigger(serverPlayer);
					return InteractionResult.SUCCESS;
				}
			}
			return super.useOn(context);
		}
	});
	public static final Supplier<Item> CHARRED_BONE = REGISTRY.register("charred_bone", () -> new Item(ItemProps.LAVA_IMMUNE.properties().get()) {
		@Override
		public InteractionResult useOn(UseOnContext context) {
			Player player = context.getPlayer();
			Level level = context.getLevel();
			BlockPos blockpos = context.getClickedPos();
			BlockPos blockpos1 = blockpos.relative(context.getClickedFace());

			Optional<ConfigurablePortalShape> configurablePortalShape = ConfigurablePortalShape.findEmptyPortalShape(
					level,
					blockpos1,
					Direction.Axis.X,
					PortalBlock.FRAME_TEST,
					PortalBlock.PORTAL_TEST,
					PortalBlock.IGNITER_TEST
			);
			if (configurablePortalShape.isPresent()) {
				configurablePortalShape.get().createPortalBlocks(ModBlocks.PORTAL.get().defaultBlockState(), PortalBlock.AXIS);
				level.playSound(player, blockpos1, SoundEvents.TRIDENT_THUNDER, SoundSource.BLOCKS, 1F, 0.75F + context.getLevel().getRandom().nextFloat() * 0.5F);
				ItemStack stack = context.getItemInHand();

				if (player instanceof ServerPlayer serverPlayer) {
					ModAdvancementTriggers.ACTIVATE_PORTAL_TRIGGER.get().trigger(serverPlayer);
					if (!serverPlayer.isCreative())
						stack.shrink(1);
				}
				return InteractionResult.SUCCESS;
			}
			return InteractionResult.FAIL;
		}
	});
	public static final Supplier<Item> CHARRED_WARHAMMER_HEAD = REGISTRY.register("charred_warhammer_head", () -> new Item(ItemProps.LAVA_IMMUNE.properties().get()));
	public static final Supplier<Item> TENDRIL = REGISTRY.register("tendril", () -> new Item(ItemProps.LAVA_IMMUNE.properties().get()));
	public static final Supplier<Item> TITANITE_CHUNK = REGISTRY.register("titanite_chunk", () -> new Item(ItemProps.LAVA_IMMUNE.properties().get()));
	public static final Supplier<Item> TITANITE_SHARD = REGISTRY.register("titanite_shard", () -> new Item(ItemProps.LAVA_IMMUNE.properties().get()));
	public static final Supplier<Item> FLESH_CHUNK = REGISTRY.register("flesh_chunk", () -> new Item(ItemProps.LAVA_IMMUNE.properties().get()));
	public static final Supplier<Item> ICHOR = REGISTRY.register("ichor", () -> new Item(ItemProps.LAVA_IMMUNE.properties().get()));
	public static final Supplier<Item> ICHOR_CRYSTAL = REGISTRY.register("ichor_crystal", () -> new Item(ItemProps.LAVA_IMMUNE.properties().get()));
	public static final Supplier<Item> STRANGE_PEARL = REGISTRY.register("strange_pearl", () -> new Item(ItemProps.LAVA_IMMUNE.properties().get()) {
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
	public static final Supplier<Item> ASTRAL_SHARDS = REGISTRY.register("astral_shards", () -> new Item(ItemProps.LAVA_IMMUNE.properties().get()) {
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
	public static final Supplier<Item> ASTRAL_ESSENCE = REGISTRY.register("astral_essence", () -> new Item(ItemProps.LAVA_IMMUNE.properties().get()));
	public static final Supplier<Item> ASTRAL_CRYSTAL = REGISTRY.register("astral_crystal", () -> new Item(ItemProps.LAVA_IMMUNE.properties().get()));

	public static final Supplier<Item> ETHEREAL_FRUIT_VOID = REGISTRY.register("ethereal_fruit_void", () -> new Item(ItemProps.LAVA_IMMUNE.properties().get().
			food(new FoodProperties.Builder().nutrition(4).saturationMod(0.3F).alwaysEat().build())) {
		@Override
		public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
			ItemStack itemstack = super.finishUsingItem(stack, level, entity);
			if (!level.isClientSide) {
				entity.getData(ModDataAttachments.INSANITY).addInfusion(150, entity);
			}
			return itemstack;
		}
	});
	public static final Supplier<Item> ETHEREAL_FRUIT_NULL = REGISTRY.register("ethereal_fruit_null", () -> new Item(ItemProps.LAVA_IMMUNE.properties().get().
			food(new FoodProperties.Builder().nutrition(4).saturationMod(0.3F).alwaysEat().build())) {
		@Override
		public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
			ItemStack itemstack = super.finishUsingItem(stack, level, entity);
			if (!level.isClientSide) {
				entity.getData(ModDataAttachments.INSANITY).removeInfusion(150);
			}
			return itemstack;
		}
	});
	public static final Supplier<Item> ETHEREAL_FRUIT_OVERWORLD = REGISTRY.register("ethereal_fruit_overworld", () -> new Item(ItemProps.LAVA_IMMUNE.properties().get().
			food(new FoodProperties.Builder().nutrition(4).saturationMod(0.3F).alwaysEat().build())) {
		@Override
		public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
			ItemStack itemstack = super.finishUsingItem(stack, level, entity);
			if (!level.isClientSide) {
				Insanity data = entity.getData(ModDataAttachments.INSANITY);
				data.setParanoia(data.getParanoia() - 150);
			}
			return itemstack;
		}
	});
	public static final Supplier<Item> ETHEREAL_FRUIT_NETHER = REGISTRY.register("ethereal_fruit_nether", () -> new Item(ItemProps.LAVA_IMMUNE.properties().get().
			food(new FoodProperties.Builder().nutrition(4).saturationMod(0.3F).alwaysEat().build())) {
		@Override
		public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
			ItemStack itemstack = super.finishUsingItem(stack, level, entity);
			if (!level.isClientSide) {
				Insanity data = entity.getData(ModDataAttachments.INSANITY);
				data.setParanoia(data.getParanoia() - (150 * (2F - (float) entity.getAttributeValue(ModAttributes.VOIDIC_PARANOIA_RES.get()))));
			}
			return itemstack;
		}
	});
	public static final Supplier<Item> ETHEREAL_FRUIT_END = REGISTRY.register("ethereal_fruit_end", () -> new Item(ItemProps.LAVA_IMMUNE.properties().get().
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
