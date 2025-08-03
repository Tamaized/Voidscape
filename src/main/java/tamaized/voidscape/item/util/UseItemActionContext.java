package tamaized.voidscape.item.util;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public record UseItemActionContext(ItemStack stack, Level level, LivingEntity parent) {
}
