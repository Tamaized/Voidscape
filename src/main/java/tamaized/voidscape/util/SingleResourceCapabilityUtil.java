package tamaized.voidscape.util;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.StacksResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemUtil;
import net.neoforged.neoforge.transfer.resource.Resource;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;
import tamaized.beanification.Component;

import java.util.function.Supplier;

@Component
public class SingleResourceCapabilityUtil {

	public <S, T extends Resource> T resource(StacksResourceHandler<S, T> handler) {
		return handler.getResource(0);
	}

	public <S, T extends Resource> int insert(StacksResourceHandler<S, T> handler, int amount, TransactionContext transaction) {
		return insert(handler, resource(handler), amount, transaction);
	}

	public <S, T extends Resource> int insert(StacksResourceHandler<S, T> handler, T resource, int amount, TransactionContext transaction) {
		return handler.insert(resource, amount, transaction);
	}

	public <S, T extends Resource> int extract(StacksResourceHandler<S, T> handler, int amount, TransactionContext transaction) {
		return handler.extract(resource(handler), amount, transaction);
	}

	public <S, T extends Resource> int amount(StacksResourceHandler<S, T> handler) {
		return handler.getAmountAsInt(0);
	}

	public <S, T extends Resource> int capacity(StacksResourceHandler<S, T> handler) {
		return handler.getCapacityAsInt(0, resource(handler));
	}

	public ItemStack asItemStack(ResourceHandler<ItemResource> handler) {
		return ItemUtil.getStack(handler, 0);
	}

	public <S, T extends Resource> T resource(Supplier<? extends StacksResourceHandler<S, T>> handler) {
		return handler.get().getResource(0);
	}

	public <S, T extends Resource> int insert(Supplier<? extends StacksResourceHandler<S, T>> handler, int amount, TransactionContext transaction) {
		return insert(handler, resource(handler), amount, transaction);
	}

	public <S, T extends Resource> int insert(Supplier<? extends StacksResourceHandler<S, T>> handler, T resource, int amount, TransactionContext transaction) {
		return handler.get().insert(resource, amount, transaction);
	}

	public <S, T extends Resource> int extract(Supplier<? extends StacksResourceHandler<S, T>> handler, int amount, TransactionContext transaction) {
		return handler.get().extract(resource(handler), amount, transaction);
	}

	public <S, T extends Resource> int amount(Supplier<? extends StacksResourceHandler<S, T>> handler) {
		return handler.get().getAmountAsInt(0);
	}

	public <S, T extends Resource> int capacity(Supplier<? extends StacksResourceHandler<S, T>> handler) {
		return handler.get().getCapacityAsInt(0, resource(handler));
	}

}
