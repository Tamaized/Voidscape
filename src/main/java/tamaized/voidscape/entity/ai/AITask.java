package tamaized.voidscape.entity.ai;

import net.minecraft.entity.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

public class AITask<T extends Entity> {

	protected final BiConsumer<T, AITask> exec;
	protected boolean finished = false;
	protected AITask<T> next;

	public AITask(BiConsumer<T, AITask> handle) {
		exec = handle;
		if (!(this instanceof EmptyAITask))
			next = new EmptyAITask<>();
	}

	public AITask<T> next(AITask<T> task) {
		if (next instanceof EmptyAITask)
			next = task;
		return next;
	}

	public AITask<T> handle(T parent) {
		if (finished)
			return next.handle(parent);
		exec.accept(parent, this);
		return this;
	}

	public void finish() {
		finished = true;
	}

	public void reset() {
		finished = false;
	}

	private static class EmptyAITask<T extends Entity> extends AITask<T> {

		private EmptyAITask() {
			super((e, a) -> {

			});
		}

		@Override
		public AITask<T> next(AITask<T> task) {
			return this;
		}

		@Override
		public AITask<T> handle(T parent) {
			return this;
		}
	}

	public static class RepeatedAITask<T extends Entity> extends AITask<T> {

		public RepeatedAITask(BiConsumer<T, AITask> handle) {
			super(handle);
		}

		@Override
		public AITask<T> next(AITask<T> task) {
			return this;
		}

		@Override
		public AITask<T> handle(T parent) {
			exec.accept(parent, this);
			return this;
		}
	}

	public static class RandomAITask<T extends Entity> extends AITask<T> {

		private final List<AITask<T>> tasks = new ArrayList<>();
		private AITask<T> current;

		public RandomAITask() {
			super((e, a) -> {
			});
		}

		@Override
		public AITask<T> next(AITask<T> task) {
			tasks.add(task);
			return this;
		}

		@Override
		public AITask<T> handle(T parent) {
			if (current == null || current.finished) {
				if (current != null)
					current.reset();
				current = null;
				while (current == null || (current instanceof ChanceAITask && !((ChanceAITask<T>) current).execute(parent.level.getRandom())))
					current = tasks.get(parent.level.random.nextInt(tasks.size()));
			}
			current.handle(parent);
			return this;
		}

		public static class ChanceAITask<T extends Entity> extends AITask<T> {

			private final Predicate<Random> random;

			public ChanceAITask(Predicate<Random> rand) {
				super((e, a) -> {

				});
				random = rand;
			}

			public boolean execute(Random rand) {
				return random.test(rand);
			}

			@Override
			public AITask<T> next(AITask<T> task) {
				super.next(task);
				return this;
			}

			@Override
			public AITask<T> handle(T parent) {
				finished = next.finished;
				return next.handle(parent);
			}

			@Override
			public void reset() {
				super.reset();
				next.reset();
			}
		}

	}

	public static class PendingAITask<T extends Entity> extends AITask<T> {

		private final Predicate<T> condition;
		private boolean triggered = false;

		public PendingAITask(BiConsumer<T, AITask> handle, Predicate<T> condition) {
			super(handle);
			this.condition = condition;
		}

		@Override
		public AITask<T> handle(T parent) {
			if (finished)
				return next.handle(parent);
			if (!triggered && condition.test(parent))
				triggered = true;
			if (triggered)
				exec.accept(parent, this);
			else
				next.handle(parent);
			return this;
		}
	}

}
