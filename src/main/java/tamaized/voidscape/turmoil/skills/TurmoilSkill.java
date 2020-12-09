package tamaized.voidscape.turmoil.skills;

import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TurmoilSkill {

	private static final List<TurmoilSkill> REGISTRY = new ArrayList<>();

	private final int id;

	private final TranslationTextComponent title;
	private final TranslationTextComponent description;
	private final int spentPoints;
	private final int cost;
	private final boolean core;
	private final List<TurmoilSkill> required = new ArrayList<>();

	public TurmoilSkill(TranslationTextComponent title, TranslationTextComponent desc, int spentPoints, int cost, TurmoilSkill... required) {
		this(title, desc, spentPoints, cost, false, required);
	}

	public TurmoilSkill(TranslationTextComponent title, TranslationTextComponent desc, int spentPoints, int cost, boolean core, TurmoilSkill... required) {
		this.title = title;
		this.description = desc;
		this.spentPoints = spentPoints;
		this.cost = cost;
		this.core = core;
		Collections.addAll(this.required, required);
		id = register(this);
	}

	private static int register(TurmoilSkill skill) {
		REGISTRY.add(skill);
		return REGISTRY.size() - 1;
	}

	@Nullable
	public static TurmoilSkill getFromID(int id) {
		return REGISTRY.get(id);
	}

	@SuppressWarnings("unused")
	public static void classload() {
		Object classload = TurmoilSkills.VOIDMANCER_SKILLS;
	}

	public TranslationTextComponent getTitle() {
		return title;
	}

	public TranslationTextComponent getDescription() {
		return description;
	}

	public int getSpentPoints() {
		return spentPoints;
	}

	public int getCost() {
		return cost;
	}

	public boolean isCore() {
		return core;
	}

	public boolean hasRequired(List<TurmoilSkill> others) {
		return others.containsAll(required);
	}

	public int getID() {
		return id;
	}
}
