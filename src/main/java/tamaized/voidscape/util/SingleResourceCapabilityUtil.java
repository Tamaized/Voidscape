package tamaized.voidscape.util;

import net.neoforged.neoforge.transfer.StacksResourceHandler;
import net.neoforged.neoforge.transfer.resource.Resource;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;
import tamaized.beanification.Component;

import java.util.function.Supplier;

@Component
public class SingleResourceCapabilityUtil {

	public <S, T extends Resource> void insert(StacksResourceHandler<S, T> handler, int amount, TransactionContext transaction) {
		handler.insert(handler.getResource(0), amount, transaction);
	}

	public <S, T extends Resource> void extract(StacksResourceHandler<S, T> handler, int amount, TransactionContext transaction) {
		handler.extract(handler.getResource(0), amount, transaction);
	}

	public <S, T extends Resource> int amount(StacksResourceHandler<S, T> handler) {
		return handler.getAmountAsInt(0);
	}

	public <S, T extends Resource> int capacity(StacksResourceHandler<S, T> handler) {
		return handler.getCapacityAsInt(0, handler.getResource(0));
	}

	public <S, T extends Resource> void insert(Supplier<? extends StacksResourceHandler<S, T>> handler, int amount, TransactionContext transaction) {
		handler.get().insert(handler.get().getResource(0), amount, transaction);
	}

	public <S, T extends Resource> void extract(Supplier<? extends StacksResourceHandler<S, T>> handler, int amount, TransactionContext transaction) {
		handler.get().extract(handler.get().getResource(0), amount, transaction);
	}

	public <S, T extends Resource> int amount(Supplier<? extends StacksResourceHandler<S, T>> handler) {
		return handler.get().getAmountAsInt(0);
	}

	public <S, T extends Resource> int capacity(Supplier<? extends StacksResourceHandler<S, T>> handler) {
		return handler.get().getCapacityAsInt(0, handler.get().getResource(0));
	}

}
