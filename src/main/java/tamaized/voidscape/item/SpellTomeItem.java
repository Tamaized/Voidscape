package tamaized.voidscape.item;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.level.Level;
import tamaized.voidscape.item.util.UseItemActionContext;

import java.util.function.Consumer;

public class SpellTomeItem extends Item {

	private final int cooldown;
	private final Consumer<UseItemActionContext> action;

	public SpellTomeItem(Properties properties, int cooldown, Consumer<UseItemActionContext> action) {
		super(properties);
		this.cooldown = cooldown;
		this.action = action;
	}

	@Override
	public int getUseDuration(ItemStack stack, LivingEntity entity) {
		return 72000;
	}

	@Override
	public ItemUseAnimation getUseAnimation(ItemStack stack) {
		return ItemUseAnimation.BOW;
	}

	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		player.startUsingItem(hand);
		return InteractionResult.CONSUME;
	}

	@Override
	public boolean releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
		if (getUseDuration(stack, entity) - timeLeft > 15) {
			doAction(new UseItemActionContext(stack, level, entity));
			level.playSound(null, entity.position().x(), entity.position().y(), entity.position().z(), SoundEvents.BLAZE_SHOOT, SoundSource.PLAYERS, 1F, 0.5F + entity.getRandom().nextFloat() * 0.25F);
			if (entity instanceof Player player)
				player.getCooldowns().addCooldown(stack, cooldown);
			stack.hurtAndBreak(1, entity, EquipmentSlot.MAINHAND);
			return true;
		}

		return super.releaseUsing(stack, level, entity, timeLeft);
	}

	public void doAction(UseItemActionContext context) {
		action.accept(context);
	}

}
