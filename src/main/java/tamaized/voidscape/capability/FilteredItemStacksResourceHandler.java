package tamaized.voidscape.capability;

import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;

import java.util.function.BiPredicate;

public class FilteredItemStacksResourceHandler extends ItemStacksResourceHandler {

	private final BiPredicate<Integer, ItemResource> slotFilter;

	public FilteredItemStacksResourceHandler(int size, BiPredicate<Integer, ItemResource> slotFilter) {
		super(size);
		this.slotFilter = slotFilter;
	}

	@Override
	public boolean isValid(int index, ItemResource resource) {
		return slotFilter.test(index, resource) && super.isValid(index, resource);
	}
}
