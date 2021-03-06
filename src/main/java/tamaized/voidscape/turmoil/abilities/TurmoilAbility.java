package tamaized.voidscape.turmoil.abilities;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.turmoil.TurmoilStats;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.BiPredicate;

public class TurmoilAbility {

	private static final List<TurmoilAbility> REGISTRY = new ArrayList<>();

	private final int id;
	private final String unloc;
	private final Toggle toggle;
	private final Type type;
	private final int cost;
	private final int cooldown;
	private final BiPredicate<TurmoilAbility, LivingEntity> execute;

	private float damage = 0;

	public TurmoilAbility(String unloc, Toggle toggle, Type type, int cost, int cooldown, BiPredicate<TurmoilAbility, LivingEntity> execute) {
		id = register(this);
		this.unloc = unloc;
		this.toggle = toggle;
		this.type = type;
		this.cost = cost;
		this.cooldown = cooldown;
		this.execute = execute;
	}

	public static int getPower(TurmoilStats stats, TurmoilAbility.Type type) {
		return type == TurmoilAbility.Type.Voidic ? stats.getVoidicPower() : type == TurmoilAbility.Type.Null ? stats.getNullPower() : stats.getInsanePower();
	}

	public static void drainPower(TurmoilStats stats, TurmoilAbility.Type type, int drain) {
		switch (type) {
			default:
			case Voidic:
				stats.setVoidicPower(stats.getVoidicPower() - drain);
				break;
			case Null:
				stats.setNullPower(stats.getNullPower() - drain);
				break;
			case Insane:
				stats.setInsanePower(stats.getInsanePower() - drain);
		}
	}

	public static TurmoilAbility getFromID(int id) {
		return id < 0 || id >= REGISTRY.size() ? null : REGISTRY.get(id);
	}

	private static int register(TurmoilAbility ability) {
		int id = REGISTRY.size();
		REGISTRY.add(ability);
		return id;
	}

	public int cooldown() {
		return cooldown;
	}

	public Type costType() {
		return type;
	}

	public int cost() {
		return cost;
	}

	public boolean execute(LivingEntity caster) {
		return execute.test(this, caster);
	}

	public TurmoilAbility damage(float d) {
		damage = d;
		return this;
	}

	public float damage() {
		return damage;
	}

	public final int id() {
		return id;
	}

	public TranslationTextComponent getTitle() {
		return new TranslationTextComponent(unloc.concat(".title"));
	}

	public TranslationTextComponent getDescription() {
		TranslationTextComponent desc = new TranslationTextComponent(unloc.concat(".desc"));
		desc.append("\n\n").append(new TranslationTextComponent(Voidscape.MODID + ".ability.cost", cost).append(" ").
				append(new TranslationTextComponent(Voidscape.MODID + ".ability.power." + type.name().toLowerCase(Locale.US)))).
				append("\n").append(new TranslationTextComponent(Voidscape.MODID + ".ability.cooldown", cooldown));
		if (damage > 0)
			desc.append("\n").append(new TranslationTextComponent(Voidscape.MODID + ".ability.damage", damage));
		return desc;
	}

	public ResourceLocation getTexture() {
		String[] loc = unloc.contains(".") ? unloc.replaceAll("\\.", "/").split("/", 2) : new String[]{"minecraft", unloc};
		return new ResourceLocation(loc[0], "textures/".concat(loc[1].concat(".png")));
	}

	public enum Type {
		Voidic, Null, Insane
	}

	public enum Toggle {
		None, Voidic, Imbue, Stance, Link, Empower
	}
}
