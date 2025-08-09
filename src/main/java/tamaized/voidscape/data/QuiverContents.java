package tamaized.voidscape.data;

import com.mojang.serialization.Codec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class QuiverContents implements TooltipComponent {

	public static final Codec<QuiverContents> CODEC = ItemStack.CODEC.listOf()
		.xmap(QuiverContents::new, instance -> instance.items);

	public static final StreamCodec<RegistryFriendlyByteBuf, QuiverContents> STREAM_CODEC = ItemStack.STREAM_CODEC
		.apply(ByteBufCodecs.list())
		.map(QuiverContents::new, instance -> instance.items);

	public static final QuiverContents EMPTY = new QuiverContents(new ArrayList<>());

	private static final int MAX_SLOT_SIZE = 5;

	private final List<ItemStack> items;

	public QuiverContents(List<ItemStack> items) {
		this.items = items.stream().map(ItemStack::copy).toList();
	}

	public boolean isEmpty() {
		return items.isEmpty() || items.stream().allMatch(ItemStack::isEmpty);
	}

	public List<ItemStack> view() {
		return items;
	}

	public float fullPercentage() {
		float sum = 0F;
		for (int i = 0; i < Math.min(MAX_SLOT_SIZE, items.size()); i++) {
			ItemStack stack = items.get(i);
			sum += (float) stack.getCount() / (float) stack.getMaxStackSize();
		}
		return sum / (float) MAX_SLOT_SIZE;
	}

	public Mutable toMutableCopy() {
		return new Mutable(items);
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		} else {
			return other instanceof QuiverContents contents && ItemStack.listMatches(this.items, contents.items);
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public int hashCode() {
		return ItemStack.hashStackList(this.items);
	}

	public static class Mutable {

		private final List<ItemStack> items = new ArrayList<>();

		Mutable(List<ItemStack> items) {
			items.forEach(stack -> this.items.add(stack.copy()));
		}

		public QuiverContents toImmutable() {
			return new QuiverContents(items);
		}

		public void shrinkFirstStack(int amount) {
			if (items.isEmpty())
				return;
			items.getFirst().shrink(amount);
			if (items.getFirst().isEmpty())
				items.removeFirst();
		}

		public ItemStack removeOneStack() {
			if (items.isEmpty())
				return ItemStack.EMPTY;
			return items.removeFirst().copy();
		}

		public ItemStack get(int slot) {
			return items.get(slot);
		}

		public ItemStack tryInsert(ItemStack stack) {
			ItemStack clone = stack.copy();
			items.stream().filter(slot -> ItemStack.isSameItemSameComponents(slot, stack)).forEach(slot -> {
				if (!clone.isEmpty()) {
					int space = slot.getMaxStackSize() - slot.getCount();
					if (space > 0) {
						int amount = Math.min(space, clone.getCount());
						slot.grow(amount);
						clone.shrink(amount);
					}
				}
			});
			if (!clone.isEmpty() && items.size() < MAX_SLOT_SIZE) {
				items.add(clone.copy());
				return ItemStack.EMPTY;
			}
			return clone;
		}

	}
}
