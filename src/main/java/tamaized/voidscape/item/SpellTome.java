package tamaized.voidscape.item;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class SpellTome extends Item {

	private final Supplier<Item> repairMaterial;
	private final int cooldown;
	private final Consumer<ActionContext> action;

	public SpellTome(Properties properties, Supplier<Item> repairMaterial, int cooldown, Consumer<ActionContext> action) {
		super(properties);
		this.repairMaterial = repairMaterial;
		this.cooldown = cooldown;
		this.action = action;
	}

	@Override
	public boolean isValidRepairItem(ItemStack stack, ItemStack repairStack) {
		return repairStack.is(repairMaterial.get()) || super.isValidRepairItem(stack, repairStack);
	}

	public int getUseDuration(ItemStack stack) {
		return 72000;
	}

	public UseAnim getUseAnimation(ItemStack stack) {
		return UseAnim.BOW;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		player.startUsingItem(hand);
		return InteractionResultHolder.success(player.getItemInHand(hand));
	}

	@Override
	public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
		if (getUseDuration(stack) - timeLeft > 15) {
			action.accept(new ActionContext(stack, level, entity));
			level.playSound(null, entity.position().x(), entity.position().y(), entity.position().z(), SoundEvents.BLAZE_SHOOT, SoundSource.PLAYERS, 1F, 0.5F + entity.getRandom().nextFloat() * 0.25F);
			if (entity instanceof Player player)
				player.getCooldowns().addCooldown(this, cooldown);
			// This must remain an anon class to spoof the reobfuscator from mapping to the wrong SRG name
			//noinspection Convert2Lambda
			stack.hurtAndBreak(1, entity, new Consumer<>() {
				@Override
				public void accept(LivingEntity e) {
					e.broadcastBreakEvent(EquipmentSlot.MAINHAND);
				}
			});
		}
	}

	public record ActionContext(ItemStack stack, Level level, LivingEntity parent) {

	}
}
