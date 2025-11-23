package tamaized.voidscape.model;

import net.minecraft.world.item.ItemStack;

public record QuiverData(ItemStack quiver, ItemStack arrow) {

	public QuiverData() {
		this(ItemStack.EMPTY, ItemStack.EMPTY);
	}

}
