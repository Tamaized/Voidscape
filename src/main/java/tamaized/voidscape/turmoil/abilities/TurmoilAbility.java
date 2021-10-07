package tamaized.voidscape.turmoil.abilities;

import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.turmoil.SubCapability;
import tamaized.voidscape.turmoil.TurmoilStats;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.BiPredicate;

public class TurmoilAbility {

	private static final List<TurmoilAbility> REGISTRY = new ArrayList<>();

	private final int id;
	private final String unloc;
	private final ResourceLocation texture;
	private final Toggle toggle;
	private final Type type;
	private final int cost;
	private final int cooldown;
	private final BiPredicate<TurmoilAbility, LivingEntity> execute;

	private float damage = 0;

	public TurmoilAbility(String unloc, Type type, int cost, int cooldown, Toggle toggle) {
		this(unloc, toggle, type, cost, cooldown, null);
	}

	public TurmoilAbility(String unloc, Type type, int cost, int cooldown, BiPredicate<TurmoilAbility, LivingEntity> execute) {
		this(unloc, Toggle.None, type, cost, cooldown, execute);
	}

	private TurmoilAbility(String unloc, Toggle toggle, Type type, int cost, int cooldown, BiPredicate<TurmoilAbility, LivingEntity> execute) {
		id = register(this);
		this.unloc = unloc;
		String[] loc = unloc.contains(".") ? unloc.replaceAll("\\.", "/").split("/", 2) : new String[]{"minecraft", unloc};
		texture = new ResourceLocation(loc[0], "textures/".concat(loc[1].concat(".png")));
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

	@Nullable
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
		return toggle == Toggle.None ? execute.test(this, caster) : caster.getCapability(SubCapability.CAPABILITY).map(cap -> cap.get(Voidscape.subCapTurmoilStats).map(data -> data.toggleAbility(this)).orElse(false)).orElse(false);
	}

	public TurmoilAbility damage(float d) {
		damage = d;
		return this;
	}

	public float damage() {
		return damage;
	}

	public float damage(@Nullable Entity caster) {
		return damage(caster, 0);
	}

	public float damage(@Nullable Entity caster, float offset) {
		if (caster == null)
			return damage() + offset;
		Optional<TurmoilStats> stats = caster.getCapability(SubCapability.CAPABILITY).map(cap -> cap.get(Voidscape.subCapTurmoilStats)).orElse(Optional.empty());
		if (stats.isPresent()) {
			float dmg = damage() + offset;
			dmg *= 1F + stats.get().stats().spellpower / 100F;
			if (caster.level.getRandom().nextInt(100) + 1 <= stats.get().stats().spellCrit)
				dmg *= 1.25F;
			return dmg;
		}
		return damage() + offset;
	}

	public Toggle toggle() {
		return toggle;
	}

	public final int id() {
		return id;
	}

	public TranslatableComponent getTitle() {
		return new TranslatableComponent(unloc.concat(".title"));
	}

	public TranslatableComponent getDescription() {
		TranslatableComponent desc = new TranslatableComponent(unloc.concat(".desc"));
		desc.append("\n\n").append(new TranslatableComponent(Voidscape.MODID + ".ability.cost", cost).append(" ").
						append(new TranslatableComponent(Voidscape.MODID + ".ability.power." + type.name().toLowerCase(Locale.US)))).
				append("\n").append(new TranslatableComponent(Voidscape.MODID + ".ability.cooldown", cooldown));
		if (damage > 0)
			desc.append("\n").append(new TranslatableComponent(Voidscape.MODID + ".ability.damage", damage));
		return desc;
	}

	public ResourceLocation getTexture() {
		return texture;
	}

	public enum Type {
		Voidic, Null, Insane
	}

	public enum Toggle {
		None, Voidic, Imbue, ArrowShot, Stance, Link, Empower;
		public static final Toggle[] VALUES = values();
	}
}
