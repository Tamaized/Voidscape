package tamaized.voidscape.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

import java.util.function.Consumer;

public class SpellTome extends Item {

	private final Consumer<ActionContext> action;

	public SpellTome(Properties properties, Consumer<ActionContext> action) {
		super(properties);
		this.action = action;
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
		}
	}

	public record ActionContext(ItemStack stack, Level level, LivingEntity parent) {

	}
}
