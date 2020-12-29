package tamaized.voidscape.turmoil.skills;

import com.google.common.collect.ImmutableList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import tamaized.voidscape.turmoil.abilities.TurmoilAbility;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class TurmoilSkill {

	private static final List<TurmoilSkill> REGISTRY = new ArrayList<>();

	private final int id;

	private final TranslationTextComponent title;
	private final TranslationTextComponent description;
	private final Supplier<Supplier<ResourceLocation>> texture;
	private final int spentPoints;
	private final int cost;
	private final boolean core;
	private final List<TurmoilSkill> required = new ArrayList<>();
	private final List<TurmoilAbility> abilities = new ArrayList<>();
	private final Stats stats;
	private final boolean disabled;

	public TurmoilSkill(TranslationTextComponent title, TranslationTextComponent desc, Supplier<Supplier<ResourceLocation>> texture, int spentPoints, int cost, boolean core, TurmoilSkill[] required, TurmoilAbility[] abilities, Stats stats, boolean disabled) {
		this.title = title;
		this.description = desc;
		this.texture = texture;
		this.spentPoints = spentPoints;
		this.cost = cost;
		this.core = core;
		Collections.addAll(this.required, required);
		Collections.addAll(this.abilities, abilities);
		id = register(this);
		this.stats = stats;
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
		return texture.get().get();
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

	public Stats getStats() {
		return stats;
	}

	public boolean disabled() {
		return disabled;
	}

	public int getID() {
		return id;
	}

	static class Stats {
		private final float voidicDamage;
		private final int voidicDamagePercent;
		private final float voidicDamageReduction;
		private final int voidicDamageReductionPercentage;
		private final int spellpower;
		private final int rechargeRate;
		private final int cooldown;
		private final int cost;
		private final int healAmp;
		private final float heart;
		private final int threat;

		private Stats(float voidicDamage, int voidicDamagePercent, float voidicDamageReduction, int voidicDamageReductionPercentage, int spellpower, int rechargeRate, int cooldown, int cost, int healAmp, float heart, int threat) {
			this.voidicDamage = voidicDamage;
			this.voidicDamagePercent = voidicDamagePercent;
			this.voidicDamageReduction = voidicDamageReduction;
			this.voidicDamageReductionPercentage = voidicDamageReductionPercentage;
			this.spellpower = spellpower;
			this.rechargeRate = rechargeRate;
			this.cooldown = cooldown;
			this.cost = cost;
			this.healAmp = healAmp;
			this.heart = heart;
			this.threat = threat;
		}

		static class Builder {
			private float voidicDamage;
			private int voidicDamagePercent;
			private float voidicDamageReduction;
			private int voidicDamageReductionPercentage;
			private int spellpower;
			private int rechargeRate;
			private int cooldown;
			private int cost;
			private int healAmp;
			private float heart;
			private int threat;

			private Builder() {

			}

			static Builder begin() {
				return new Builder();
			}

			Builder voidicDamage(float a) {
				voidicDamage = a;
				return this;
			}

			Builder voidicDamagePercent(int a) {
				voidicDamagePercent = a;
				return this;
			}

			Builder voidicDamageReduction(float a) {
				voidicDamageReduction = a;
				return this;
			}

			Builder voidicDamageReductionPercentage(int a) {
				voidicDamageReductionPercentage = a;
				return this;
			}

			Builder spellpower(int a) {
				spellpower = a;
				return this;
			}

			Builder rechargeRate(int a) {
				rechargeRate = a;
				return this;
			}

			Builder cooldown(int a) {
				cooldown = a;
				return this;
			}

			Builder cost(int a) {
				cost = a;
				return this;
			}

			Builder healAmp(int a) {
				healAmp = a;
				return this;
			}

			Builder hearts(float a) {
				heart = a;
				return this;
			}

			Builder threat(int a) {
				threat = a;
				return this;
			}

			Stats build() {
				return new Stats(voidicDamage, voidicDamagePercent, voidicDamageReduction, voidicDamageReductionPercentage, spellpower, rechargeRate, cooldown, cost, healAmp, heart, threat);
			}

		}

	}
}
