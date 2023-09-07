package tamaized.voidscape.client.layout;

import org.apache.logging.log4j.util.TriConsumer;
import tamaized.voidscape.turmoil.skills.TurmoilSkill;

import java.util.Random;
import java.util.function.Function;

public interface ISkillLayout {

	Random rand = new Random();
	Function<Integer, Integer> rng = bound -> -bound + rand.nextInt(bound);

	default void addWithRNG(TriConsumer<TurmoilSkill, Integer, Integer> add, TurmoilSkill skill, int x, int y) {
		addWithRNG(add, skill, x, y, 4);
	}

	default void addWithRNG(TriConsumer<TurmoilSkill, Integer, Integer> add, TurmoilSkill skill, int x, int y, int bound) {
		add.accept(skill, x + rng.apply(bound), y + rng.apply(bound));
	}

	void fill(int x, int y, TriConsumer<TurmoilSkill, Integer, Integer> add);

}
