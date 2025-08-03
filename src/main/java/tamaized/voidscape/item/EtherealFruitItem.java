package tamaized.voidscape.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import tamaized.voidscape.item.util.UseItemActionContext;

import java.util.function.Consumer;

public class EtherealFruitItem extends Item {

	private final Consumer<UseItemActionContext> action;

	public EtherealFruitItem(Consumer<UseItemActionContext> action, Properties properties) {
		super(properties);
		this.action = action;
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
		ItemStack itemstack = super.finishUsingItem(stack, level, entity);
		if (!level.isClientSide) {
			doAction(new UseItemActionContext(stack, level, entity));
		}
		return itemstack;
	}

	public void doAction(UseItemActionContext context) {
		action.accept(context);
	}

}
