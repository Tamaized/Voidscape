package tamaized.voidscape.registry;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import tamaized.regutil.RegUtil;
import tamaized.regutil.RegistryClass;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.block.BlockEtherealPlant;
import tamaized.voidscape.capability.SubCapability;

public class ModItems implements RegistryClass {

	static class ItemProps {
		static final RegUtil.ItemProps DEFAULT = new RegUtil.ItemProps(Item.Properties::new);
		static final RegUtil.ItemProps LAVA_IMMUNE = new RegUtil.ItemProps(() -> DEFAULT.properties().get().fireResistant());
	}

	static final DeferredRegister<Item> REGISTRY = RegUtil.create(ForgeRegistries.ITEMS);
	public static final RegistryObject<Item> VOIDIC_CRYSTAL = REGISTRY.register("voidic_crystal", () -> new Item(ItemProps.LAVA_IMMUNE.properties().get()));
	public static final RegistryObject<Item> VOIDIC_TEMPLATE = REGISTRY.register("voidic_template", () -> new Item(ItemProps.LAVA_IMMUNE.properties().get()));
	public static final RegistryObject<Item> ETHEREAL_ESSENCE = REGISTRY.register("ethereal_essence", () -> new Item(ItemProps.LAVA_IMMUNE.properties().get()) {
		@Override
		public InteractionResult useOn(UseOnContext context) {
			if (Voidscape.checkForVoidDimension(context.getLevel()) && context.getLevel().getBlockState(context.getClickedPos()).is(Blocks.BEDROCK)) {
				context.getLevel().setBlockAndUpdate(context.getClickedPos(), ModBlocks.VOIDIC_CRYSTAL_ORE.get().defaultBlockState());
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

				if (player instanceof ServerPlayer) {
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
	public static final RegistryObject<Item> FRUIT = REGISTRY.register("fruit", () -> new Item(ItemProps.LAVA_IMMUNE.properties().get().
			food(new FoodProperties.Builder().nutrition(4).saturationMod(0.3F).alwaysEat().build())) {
		@Override
		public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
			ItemStack itemstack = super.finishUsingItem(stack, level, entity);
			if (!level.isClientSide) {
				CompoundTag tag = stack.getTag();
				tag = tag == null ? null : tag.getCompound(Voidscape.MODID);
				BlockEtherealPlant.State state = BlockEtherealPlant.State.VOID;
				if (tag != null)
					state = switch (tag.getString("augment")) {
						default -> BlockEtherealPlant.State.VOID;
						case "null" -> BlockEtherealPlant.State.NULL;
						case "overworld" -> BlockEtherealPlant.State.OVERWORLD;
						case "nether" -> BlockEtherealPlant.State.NETHER;
						case "end" -> BlockEtherealPlant.State.END;
					};
				switch (state) {
					case VOID -> entity.getCapability(SubCapability.CAPABILITY).ifPresent(cap -> cap.get(Voidscape.subCapInsanity)
							.ifPresent(data -> data.setInfusion(data.getInfusion() + (150 * (2F - (float) entity.getAttributeValue(ModAttributes.VOIDIC_INFUSION_RES.get()))))));
					case NULL -> entity.getCapability(SubCapability.CAPABILITY).ifPresent(cap -> cap.get(Voidscape.subCapInsanity)
							.ifPresent(data -> data.setInfusion(data.getInfusion() - 150)));
					case OVERWORLD -> entity.getCapability(SubCapability.CAPABILITY).ifPresent(cap -> cap.get(Voidscape.subCapInsanity)
							.ifPresent(data -> data.setParanoia(data.getParanoia() - 150)));
					case NETHER -> entity.getCapability(SubCapability.CAPABILITY).ifPresent(cap -> cap.get(Voidscape.subCapInsanity)
							.ifPresent(data -> data.setParanoia(data.getParanoia() + (150 * (2F - (float) entity.getAttributeValue(ModAttributes.VOIDIC_PARANOIA_RES.get()))))));
					case END -> entity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 20 * 20, 3));
				}
			}
			return itemstack;
		}
	});

	@Override
	public void init(IEventBus bus) {

	}

}
