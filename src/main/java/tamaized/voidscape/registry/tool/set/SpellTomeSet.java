package tamaized.voidscape.registry.tool.set;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.registries.DeferredHolder;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.regutil.RegUtil;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.entity.IchorBoltEntity;
import tamaized.voidscape.entity.ShroudBoltEntity;
import tamaized.voidscape.item.LingeringPotionAugmentableSpellTomeItem;
import tamaized.voidscape.item.SpellTomeItem;
import tamaized.voidscape.registry.*;

import java.util.function.Consumer;

@Component
public class SpellTomeSet {

	@Autowired
	private ModItemProperties itemProperties;

	@Autowired
	private ModEffects modEffects;

	@Autowired
	private ModDataAttachments dataAttachments;

	@Autowired
	private ModItemTags itemTags;

	@Autowired
	private ModEntities entities;

	@Autowired
	private ModDamageSource damageSource;

	public final DeferredHolder<Item, SpellTomeItem> ICHOR_TOME = RegUtil.register(Registries.ITEM, "ichor_tome", (id) -> new SpellTomeItem(
		itemProperties.LAVA_IMMUNE.apply(id).durability(100).repairable(itemTags.REPAIR_MATERIAL_ICHOR),
		20 * 10,
		context -> context.level().addFreshEntity(new IchorBoltEntity(context.parent()))
	));

	public final DeferredHolder<Item, SpellTomeItem> VOIDIC_TOME = RegUtil.register(Registries.ITEM, "voidic_tome", (id) -> new LingeringPotionAugmentableSpellTomeItem(
		itemProperties.LAVA_IMMUNE.apply(id).durability(100).repairable(itemTags.REPAIR_MATERIAL_VOIDIC_CRYSTAL),
		20 * 45,
		context -> {
			if (context.stack().has(DataComponents.POTION_CONTENTS))
				context.parent().setData(dataAttachments.AURA_EFFECT, context.stack().getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY));
			else
				context.parent().removeData(dataAttachments.AURA_EFFECT);
			context.parent().addEffect(new MobEffectInstance(modEffects.AURA, 20 * 30));
		}
	));

	public final DeferredHolder<Item, SpellTomeItem> CORRUPT_TOME = RegUtil.register(Registries.ITEM, "corrupt_tome", (id) -> new SpellTomeItem(
		itemProperties.LAVA_IMMUNE.apply(id).durability(100).repairable(itemTags.REPAIR_MATERIAL_CORRUPT),
		20 * 5,
		context -> {
			context.parent().addDeltaMovement(context.parent().getLookAngle().scale(2.5D));
			context.level().playSound(null, context.parent(), SoundEvents.FIREWORK_ROCKET_LAUNCH, SoundSource.PLAYERS, 1F, 0.75F + context.parent().getRandom().nextFloat() * 0.5F);
			context.parent().getData(dataAttachments.INSANITY).enableLeapParticles();
			context.parent().addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 20 * 10));
		}
	));

	public final DeferredHolder<Item, SpellTomeItem> TITANITE_TOME = RegUtil.register(Registries.ITEM, "titanite_tome", (id) -> new SpellTomeItem(
		itemProperties.LAVA_IMMUNE.apply(id).durability(100).repairable(itemTags.REPAIR_MATERIAL_TITANITE),
		20 * 45,
		context -> context.parent().addEffect(new MobEffectInstance(modEffects.FORTIFIED, 20 * 30))
	));

	public final DeferredHolder<Item, Item> ASTRAL_TOME = RegUtil.register(Registries.ITEM, "astral_tome", (id) -> new Item(
		itemProperties.LAVA_IMMUNE.apply(id)
	));

	public final DeferredHolder<Item, Item> SHROUD_TOME = RegUtil.register(Registries.ITEM, "shroud_tome", (id) -> new SpellTomeItem(
		itemProperties.LAVA_IMMUNE.apply(id).durability(500).repairable(itemTags.REPAIR_MATERIAL_SHROUD),
		10,
		_ -> {}
	) {
		@Override
		public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int ticksRemaining) {
			if (!(level instanceof ServerLevel serverLevel))
				return;

			if (stack.nextDamageWillBreak()) {
				entity.stopUsingItem();
				return;
			}

			if (entity.getTicksUsingItem() % 10 != 0)
				return;

			if (entity instanceof Player player)
				stack.hurtWithoutBreaking(1, player);

			HitResult hit = ProjectileUtil.getHitResultOnViewVector(entity, target -> target instanceof LivingEntity && target.isPickable(), 16D);
			Vec3 end = hit.getLocation();
			Vec3 start = entity.getEyePosition().add(entity.getLookAngle().scale(0.5D)).subtract(0D, 0.25D, 0D);

			level.playSound(null, entity, SoundEvents.EVOKER_PREPARE_ATTACK, SoundSource.NEUTRAL, 0.25F, 1.25F + entity.getRandom().nextFloat() * 0.5F);

			ShroudBoltEntity shroudBolt = new ShroudBoltEntity(entities.SHROUD_BOLT.get(), serverLevel, start, end);
			serverLevel.addFreshEntity(shroudBolt);

			for (LivingEntity target : serverLevel.getEntitiesOfClass(LivingEntity.class, new AABB(end, end).inflate(2D), other -> other != entity)) {
				target.hurtServer(serverLevel, level.damageSources().indirectMagic(shroudBolt, entity), 2F);
				target.hurtServer(serverLevel, damageSource.getIndirectEntityDamageSource(level, damageSource.VOIDIC, shroudBolt, entity), 5F);
				target.getData(dataAttachments.INSANITY).addInfusion(50F);
			}

			level.playSound(null, end.x, end.y, end.z, SoundEvents.TRIDENT_THUNDER, SoundSource.NEUTRAL, 0.15F, 1.0F + entity.getRandom().nextFloat() * 0.5F);
			level.playSound(null, end.x, end.y, end.z, SoundEvents.FIREWORK_ROCKET_TWINKLE, SoundSource.NEUTRAL, 0.15F, 1.0F + entity.getRandom().nextFloat() * 0.5F);
		}

		@Override
		public boolean releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
			if (entity instanceof Player player)
				player.getCooldowns().addCooldown(stack, 10);
			return false;
		}

		@Override
		@SuppressWarnings("deprecation")
		public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<net.minecraft.network.chat.Component> builder, TooltipFlag tooltipFlag) {
			super.appendHoverText(stack, context, display, builder, tooltipFlag);
			builder.accept(net.minecraft.network.chat.Component.empty());
			builder.accept(net.minecraft.network.chat.Component.translatable(Voidscape.MODID + ".tooltip.shroud_tome").withStyle(
				ChatFormatting.LIGHT_PURPLE,
				ChatFormatting.ITALIC
			));
		}
	});

}
