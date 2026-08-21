package tamaized.voidscape.capability;

import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.fluid.FluidStacksResourceHandler;

import java.util.function.BiPredicate;

public class FilteredFluidStacksResourceHandler extends FluidStacksResourceHandler {

	private final BiPredicate<Integer, FluidResource> slotFilter;

	public FilteredFluidStacksResourceHandler(int size, int capacity, BiPredicate<Integer, FluidResource> slotFilter) {
		super(size, capacity);
		this.slotFilter = slotFilter;
	}

	@Override
	public boolean isValid(int index, FluidResource resource) {
		return slotFilter.test(index, resource) && super.isValid(index, resource);
	}
}
