package tamaized.voidscape.turmoil.skills;

import com.google.common.collect.ImmutableList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import tamaized.voidscape.turmoil.abilities.TurmoilAbility;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TurmoilSkill {

	private static final List<TurmoilSkill> REGISTRY = new ArrayList<>();

	private final int id;

	private final TranslationTextComponent title;
	private final TranslationTextComponent description;
	private final ResourceLocation texture;
	private final int spentPoints;
	private final int cost;
	private final boolean core;
	private final List<TurmoilSkill> required = new ArrayList<>();
	private final List<TurmoilAbility> abilities = new ArrayList<>();
	private final boolean disabled;

	public TurmoilSkill(TranslationTextComponent title, TranslationTextComponent desc, ResourceLocation texture, int spentPoints, int cost, boolean core, TurmoilSkill[] required, TurmoilAbility[] abilities, boolean disabled) {
		this.title = title;
		this.description = desc;
		this.texture = texture;
		this.spentPoints = spentPoints;
		this.cost = cost;
		this.core = core;
		Collections.addAll(this.required, required);
		Collections.addAll(this.abilities, abilities);
		id = register(this);
		this.disabled = disabled;
	}

	private static int register(TurmoilSkill skill) {
		REGISTRY.add(skill);
		return REGISTRY.size() - 1;
	}

	@Nullable
	public static TurmoilSkill getFromID(int id) {
		return id < 0 || id >= REGISTRY.size() ? null : REGISTRY.get(id);
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

	public ResourceLocation getTexture() {
		return texture;
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

	public List<TurmoilSkill> getRequired() {
		return ImmutableList.copyOf(required);
	}

	public List<TurmoilAbility> getAbilities() {
		return ImmutableList.copyOf(abilities);
	}

	public boolean disabled() {
		return disabled;
	}

	public int getID() {
		return id;
	}
}
