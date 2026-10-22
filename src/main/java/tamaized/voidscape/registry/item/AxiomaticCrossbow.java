package tamaized.voidscape.registry.item;

import net.minecraft.SharedConstants;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ChargedProjectiles;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import tamaized.beanification.Autowired;
import tamaized.regutil.ExtraTooltipContext;
import tamaized.regutil.item.BreakableCrossbow;
import tamaized.regutil.item.BreakableHelper;
import tamaized.voidscape.data.AxiomBurst;
import tamaized.voidscape.registry.ModItemComponents;

import java.util.List;
import java.util.function.Consumer;

public class AxiomaticCrossbow extends BreakableCrossbow {

	public static final int BURST_TICKS = SharedConstants.TICKS_PER_SECOND * 10;
	public static final int COOLDOWN_TICKS = SharedConstants.TICKS_PER_SECOND * 30;

	@Autowired
	private BreakableHelper breakableHelper;

	@Autowired
	private ModItemComponents itemComponents;

	private final Holder<Item> activator;

	public AxiomaticCrossbow(
		Properties properties,
		Consumer<ExtraTooltipContext> tooltipConsumer,
		Holder<Item> activator
	) {
		super(properties, tooltipConsumer);
		this.activator = activator;
	}

	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (hand != InteractionHand.MAIN_HAND || !player.isSecondaryUseActive() || !canActivate(stack, player, level.getGameTime()))
			return super.use(level, player, hand);
		if (level instanceof ServerLevel serverLevel) {
			stack.set(itemComponents.AXIOM_BURST.get(), new AxiomBurst(serverLevel.getGameTime() + BURST_TICKS, 0L));
			if (!isCharged(stack))
				reload(stack, player);
			level.playSound(null, player, SoundEvents.WARDEN_SONIC_CHARGE, SoundSource.PLAYERS, 1F, 2F);
		}
		return InteractionResult.SUCCESS;
	}

	@Override
	public void performShooting(
		Level level,
		LivingEntity shooter,
		InteractionHand hand,
		ItemStack weapon,
		float power,
		float uncertainty,
		@Nullable LivingEntity targetOverride
	) {
		super.performShooting(level, shooter, hand, weapon, power, uncertainty, targetOverride);
		if (!(level instanceof ServerLevel serverLevel) || !(shooter instanceof Player player))
			return;
		if (isCharged(weapon) || breakableHelper.isBroken(weapon) || !isBurstActive(weapon, player, serverLevel.getGameTime()))
			return;
		if (reload(weapon, player))
			player.getCooldowns().addCooldown(weapon, 8);
		else
			enterCooldown(weapon, weapon.getOrDefault(itemComponents.AXIOM_BURST.get(), AxiomBurst.EMPTY), serverLevel.getGameTime());
	}

	@Override
	public void inventoryTick(ItemStack itemStack, ServerLevel level, Entity owner, @Nullable EquipmentSlot slot) {
		super.inventoryTick(itemStack, level, owner, slot);
		AxiomBurst current = itemStack.get(itemComponents.AXIOM_BURST.get());
		if (current == null || !current.isStarted())
			return;
		long gameTime = level.getGameTime();
		if (owner instanceof LivingEntity living && isBurstActive(itemStack, living, gameTime))
			return;
		enterCooldown(itemStack, current, gameTime);
	}

	private void enterCooldown(ItemStack stack, AxiomBurst current, long gameTime) {
		stack.set(itemComponents.AXIOM_BURST.get(), new AxiomBurst(0L, Math.min(gameTime, current.activeUntil()) + COOLDOWN_TICKS));
	}

	private boolean canActivate(ItemStack stack, LivingEntity holder, long gameTime) {
		AxiomBurst current = stack.getOrDefault(itemComponents.AXIOM_BURST.get(), AxiomBurst.EMPTY);
		return holder.getOffhandItem().is(activator) &&
			!breakableHelper.isBroken(stack) &&
			(isCharged(stack) || !holder.getProjectile(stack).isEmpty()) &&
			!current.isActive(gameTime) &&
			!current.isCoolingDown(gameTime);
	}

	private boolean isBurstActive(ItemStack stack, LivingEntity holder, long gameTime) {
		return holder.getOffhandItem().is(activator) && stack.getOrDefault(itemComponents.AXIOM_BURST.get(), AxiomBurst.EMPTY).isActive(gameTime);
	}

	private boolean reload(ItemStack weapon, LivingEntity shooter) {
		List<ItemStack> drawn = draw(weapon, shooter.getProjectile(weapon), shooter);
		if (drawn.isEmpty())
			return false;
		weapon.set(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.ofNonEmpty(drawn));
		return true;
	}

}
