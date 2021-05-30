package tamaized.voidscape.turmoil.skills;

import com.google.common.collect.ImmutableList;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.turmoil.abilities.TurmoilAbility;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

public class TurmoilSkill {

	private static final List<TurmoilSkill> REGISTRY = new ArrayList<>();

	private final int id;

	private final TranslationTextComponent title;
	private final TranslationTextComponent description;
	private final Supplier<Supplier<ResourceLocation>> texture;
	private final int spentPoints;
	private final int cost;
	private final CoreType core;
	private final List<TurmoilSkill> required = new ArrayList<>();
	private final List<TurmoilAbility> abilities = new ArrayList<>();
	private final Stats stats;
	private final boolean disabled;

	public enum CoreType {
		Null, Tank, Healer, Melee, Ranged
	}

	public TurmoilSkill(TranslationTextComponent title, TranslationTextComponent desc, Supplier<Supplier<ResourceLocation>> texture, int spentPoints, int cost, CoreType core, TurmoilSkill[] required, TurmoilAbility[] abilities, Stats stats, boolean disabled) {
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
		desc.append("\n");
		for (TurmoilAbility ability : this.abilities)
			desc.append("\n").append(new TranslationTextComponent(Voidscape.MODID + ".skills.ability").append(ability.getTitle()).append("\n").append(ability.getDescription())).append("\n");
		stat(stat(stat(stat(stat(stat(stat(stat(stat(stat(stat(stat(desc,

				"voidic_damage", stats.voidicDamage),

				"voidic_damage", stats.voidicDamagePercent, true),

				"voidic_reduction", stats.voidicDamageReduction),

				"voidic_reduction", stats.voidicDamageReductionPercentage, true),

				"spellpower", stats.spellpower),

				"recharge", stats.rechargeRate),

				"spellcrit", stats.spellCrit, true),

				"cooldown", stats.cooldown, true),

				"cost", stats.cost, true),

				"hamp", stats.healAmp, true),

				"heart", stats.heart),

				"threat", stats.threat, true);


	}

	private static TranslationTextComponent stat(TranslationTextComponent parent, String name, Number stat) {
		return stat(parent, name, stat, false);
	}

	private static TranslationTextComponent stat(TranslationTextComponent parent, String name, Number stat, boolean percent) {
		if (stat.doubleValue() > 0)
			parent.append("\n").append(new TranslationTextComponent(Voidscape.MODID.concat(".skills.stats.".concat(name)), percent ? stat + "%" : stat));
		return parent;
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
		Object classload = TurmoilSkills.MAGE_SKILLS;
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
		return core != CoreType.Null;
	}

	public CoreType coreType() {
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

	public static class Stats {
		public static final UUID HEALTH = UUID.fromString("fe7b05c2-e1d5-4af5-a249-2f10564e2200");
		public static final UUID VOIDIC_DAMAGE = UUID.fromString("92c9d3fa-9bcb-482f-af9d-46c5435dc246");
		public static final UUID VOIDIC_DAMAGE_PERC = UUID.fromString("ba95bf3e-af82-44b1-8d3a-dcf5cc3e8bec");
		public static final UUID VOIDIC_RESISTANCE = UUID.fromString("dcb02f0b-4f63-4583-be72-8fe1c8230655");
		public static final UUID VOIDIC_RESISTANCE_PERC = UUID.fromString("6801d807-9ba8-4bc2-8b56-3dbb714556f3");
		private static final Stats EMPTY = new Stats(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		public final float voidicDamage;
		public final int voidicDamagePercent;
		public final float voidicDamageReduction;
		public final int voidicDamageReductionPercentage;
		public final int spellpower;
		public final int rechargeRate;
		public final int spellCrit;
		public final int cooldown;
		public final int cost;
		public final int healAmp;
		public final float heart;
		public final int threat;

		private Stats(float voidicDamage, int voidicDamagePercent, float voidicDamageReduction, int voidicDamageReductionPercentage, int spellpower, int rechargeRate, int spellCrit, int cooldown, int cost, int healAmp, float heart, int threat) {
			this.voidicDamage = voidicDamage;
			this.voidicDamagePercent = voidicDamagePercent;
			this.voidicDamageReduction = voidicDamageReduction;
			this.voidicDamageReductionPercentage = voidicDamageReductionPercentage;
			this.spellpower = spellpower;
			this.rechargeRate = rechargeRate;
			this.spellCrit = spellCrit;
			this.cooldown = cooldown;
			this.cost = cost;
			this.healAmp = healAmp;
			this.heart = heart;
			this.threat = threat;
		}

		public static Stats empty() {
			return EMPTY;
		}

		public static Stats decode(PacketBuffer buffer) {
			return new Stats(buffer.readFloat(),

					buffer.readInt(),

					buffer.readFloat(),

					buffer.readInt(),

					buffer.readInt(),

					buffer.readInt(),

					buffer.readInt(),

					buffer.readInt(),

					buffer.readInt(),

					buffer.readInt(),

					buffer.readFloat(),

					buffer.readInt());
		}

		public Stats add(Stats other) {
			return new Stats(

					voidicDamage + other.voidicDamage,

					voidicDamagePercent + other.voidicDamagePercent,

					voidicDamageReduction + other.voidicDamageReduction,

					voidicDamageReductionPercentage + other.voidicDamageReductionPercentage,

					spellpower + other.spellpower,

					rechargeRate + other.rechargeRate,

					spellCrit + other.spellCrit,

					cooldown + other.cooldown,

					cost + other.cost,

					healAmp + other.healAmp,

					heart + other.heart,

					threat + other.threat

			);
		}

		public void encode(PacketBuffer buffer) {
			buffer.writeFloat(voidicDamage).
					writeInt(voidicDamagePercent).
					writeFloat(voidicDamageReduction).
					writeInt(voidicDamageReductionPercentage).
					writeInt(spellpower).
					writeInt(rechargeRate).
					writeInt(spellCrit).
					writeInt(cooldown).
					writeInt(cost).
					writeInt(healAmp).
					writeFloat(heart).
					writeInt(threat);
		}

		static class Builder {
			private float voidicDamage;
			private int voidicDamagePercent;
			private float voidicDamageReduction;
			private int voidicDamageReductionPercentage;
			private int spellpower;
			private int rechargeRate;
			private int spellCrit;
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

			Builder spellCrit(int a) {
				spellCrit = a;
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
				return new Stats(voidicDamage, voidicDamagePercent, voidicDamageReduction, voidicDamageReductionPercentage, spellpower, rechargeRate, spellCrit, cooldown, cost, healAmp, heart, threat);
			}

		}

	}
}
