package tamaized.voidscape.turmoil.abilities;

import net.minecraft.entity.LivingEntity;

import java.util.function.Consumer;

public class TurmoilAbility {

	private final String unloc;
	private final int cost;
	private final int cooldown;
	private final Consumer<LivingEntity> execute;

	public TurmoilAbility(String unloc, int cost, int cooldown, Consumer<LivingEntity> execute) {
		this.unloc = unloc;
		this.cost = cost;
		this.cooldown = cooldown;
		this.execute = execute;
	}

	public int cooldown() {
		return cooldown;
	}

	public int getCost() {
		return cost;
	}

	public void execute(LivingEntity caster) {
		execute.accept(caster);
	}
}
