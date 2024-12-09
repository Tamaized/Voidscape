package tamaized.voidscape.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import tamaized.beanification.Component;
import tamaized.voidscape.Voidscape;

@Component
public class ItemAugmentUtil { // TODO: use components

	public boolean elytra(ItemStack stack) {
		if (stack.isEmpty())
			return false;
		if (!stack.is(voidicCrystalArmorSet.VOIDIC_CRYSTAL_CHEST.get()) && !stack.is(TITANITE_CHEST.get()) && !stack.is(ICHOR_CHEST.get()) && !stack.is(ASTRAL_CHEST.get()))
			return false; // Quick fail for performance, no nbt polling needed
		CompoundTag nbt = stack.getTagElement(Voidscape.MODID);
		return nbt != null && nbt.getBoolean("elytra");
	}

	public boolean draconic(ItemStack stack) {
		if (stack.isEmpty())
			return false;
		if (!stack.is(ASTRAL_HELMET.get()) && !stack.is(ASTRAL_CHEST.get()) && !stack.is(ASTRAL_LEGS.get()) && !stack.is(ASTRAL_BOOTS.get()))
			return false; // Quick fail for performance, no nbt polling needed
		CompoundTag nbt = stack.getTagElement(Voidscape.MODID);
		return nbt != null && nbt.getBoolean("draconic");
	}

	public boolean fang(ItemStack stack) {
		if (stack.isEmpty())
			return false;
		CompoundTag nbt = stack.getTagElement(Voidscape.MODID);
		return nbt != null && nbt.getBoolean("fang");
	}

}
