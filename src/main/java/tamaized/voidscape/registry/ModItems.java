package tamaized.voidscape.registry;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
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
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.regutil.RegUtil;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.data.Insanity;
import tamaized.voidscape.network.client.ClientPacketSendParticles;
import tamaized.voidscape.registry.block.ModBlocksThunderForestBiome;

import java.util.function.Supplier;

@Component
public class ModItems {

	@Autowired
	private ModItemProperties itemProperties;

	private final DeferredRegister<Item> REGISTRY = RegUtil.create(Registries.ITEM);

	public final Supplier<Item> VOIDIC_TEMPLATE = REGISTRY.register("voidic_template", () -> new Item(itemProperties.LAVA_IMMUNE.get()));
	public final Supplier<Item> ETHEREAL_ESSENCE = REGISTRY.register("ethereal_essence", () -> new Item(itemProperties.LAVA_IMMUNE.get()) {
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
	public final Supplier<Item> ETHEREAL_SPIDER_EGGS = REGISTRY.register("ethereal_spider_eggs", () -> new Item(itemProperties.LAVA_IMMUNE.get()) {
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
	public final Supplier<Item> ETHEREAL_SPIDER_FANG = REGISTRY.register("ethereal_spider_fang", () -> new Item(itemProperties.LAVA_IMMUNE.get()));
	public final Supplier<Item> CHARRED_WARHAMMER_HEAD = REGISTRY.register("charred_warhammer_head", () -> new Item(itemProperties.LAVA_IMMUNE.get()));

	public final Supplier<Item> ETHEREAL_FRUIT_VOID = REGISTRY.register("ethereal_fruit_void", () -> new Item(itemProperties.LAVA_IMMUNE.get().
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
	public final Supplier<Item> ETHEREAL_FRUIT_NULL = REGISTRY.register("ethereal_fruit_null", () -> new Item(itemProperties.LAVA_IMMUNE.get().
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
	public final Supplier<Item> ETHEREAL_FRUIT_OVERWORLD = REGISTRY.register("ethereal_fruit_overworld", () -> new Item(itemProperties.LAVA_IMMUNE.get().
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
	public final Supplier<Item> ETHEREAL_FRUIT_NETHER = REGISTRY.register("ethereal_fruit_nether", () -> new Item(itemProperties.LAVA_IMMUNE.get().
			food(new FoodProperties.Builder().nutrition(4).saturationMod(0.3F).alwaysEat().build())) {
		@Override
		public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
			ItemStack itemstack = super.finishUsingItem(stack, level, entity);
			if (!level.isClientSide) {
				Insanity data = entity.getData(ModDataAttachments.INSANITY);
				data.setParanoia(data.getParanoia() + (150 * (2F - (float) entity.getAttributeValue(ModAttributes.VOIDIC_PARANOIA_RES.get()))));
			}
			return itemstack;
		}
	});
	public final Supplier<Item> ETHEREAL_FRUIT_END = REGISTRY.register("ethereal_fruit_end", () -> new Item(itemProperties.LAVA_IMMUNE.get().
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
