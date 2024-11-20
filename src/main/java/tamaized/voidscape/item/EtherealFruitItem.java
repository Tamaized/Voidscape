package tamaized.voidscape.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.function.Consumer;

public class EtherealFruitItem extends Item {

	private final Consumer<LivingEntity> action;

	public EtherealFruitItem(Consumer<LivingEntity> action, Properties properties) {
		super(properties);
		this.action = action;
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
		ItemStack itemstack = super.finishUsingItem(stack, level, entity);
		if (!level.isClientSide) {
			action.accept(entity);
		}
		return itemstack;
	}

}
