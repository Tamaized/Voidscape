package tamaized.voidscape.util;

import net.neoforged.neoforge.transfer.transaction.Transaction;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;
import org.jspecify.annotations.Nullable;
import tamaized.beanification.Component;
import tamaized.pkginfoutil.PublicApi;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

@Component
public class TransactionUtil {

	private static final ThreadLocal<@Nullable Transaction> CURRENT_THREAD_LOCAL_TRANSACTION = new ThreadLocal<>();

	/**
	 * Returns true when `unexpected` is not present.
	 * <p/>
	 * `exec` -> 1, `unexpected` -> 0; // returns true
	 */
	public <T> boolean executeNegation(Function<Transaction, T> exec, T unexpected) {
		return execute(exec).filter(result -> Objects.equals(result, unexpected)).isEmpty();
	}

	/**
	 * Returns true when `expected` is present.
	 * <p/>
	 * `exec` -> 0, `expected` -> 0; // returns true
	 */
	public <T> boolean executeComparing(Function<Transaction, T> exec, T expected) {
		return execute(exec).filter(result -> Objects.equals(result, expected)).isPresent();
	}

	@PublicApi
	public <T> Optional<T> execute(Function<Transaction, T> exec) {
		return switch (Transaction.getLifecycle()) {
			case NONE -> Optional.of(root(exec));
			case OPEN -> Optional.of(execute(resolveCurrentTransaction(), exec));
			default -> Optional.empty();
		};
	}

	@PublicApi
	public <T> T execute(@Nullable TransactionContext parent, Function<Transaction, T> exec) {
		try (Transaction transaction = Transaction.open(parent)) {
			return run(transaction, exec);
		}
	}

	@Nullable
	@SuppressWarnings("deprecation")
	private TransactionContext resolveCurrentTransaction() {
		Transaction transaction = CURRENT_THREAD_LOCAL_TRANSACTION.get();

		if (CURRENT_THREAD_LOCAL_TRANSACTION.get() == null) {
			return Transaction.getCurrentOpenedTransaction();
		}

		return transaction;
	}

	private <T> T root(Function<Transaction, T> exec) {
		try (Transaction transaction = Transaction.openRoot()) {
			return run(transaction, exec);
		}
	}

	private <T> T run(Transaction transaction, Function<Transaction, T> exec) {
		Transaction prev = CURRENT_THREAD_LOCAL_TRANSACTION.get();
		CURRENT_THREAD_LOCAL_TRANSACTION.set(transaction);
		T result = exec.apply(transaction);
		transaction.commit();
		CURRENT_THREAD_LOCAL_TRANSACTION.set(prev);
		return result;
	}

}
