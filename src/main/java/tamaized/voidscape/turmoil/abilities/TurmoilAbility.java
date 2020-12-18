package tamaized.voidscape.turmoil.abilities;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class TurmoilAbility {

	private static final List<TurmoilAbility> REGISTRY = new ArrayList<>();

	private final int id;
	private final String unloc;
	private final int cost;
	private final int cooldown;
	private final BiConsumer<TurmoilAbility, LivingEntity> execute;

	private float damage = 0;

	public TurmoilAbility(String unloc, int cost, int cooldown, BiConsumer<TurmoilAbility, LivingEntity> execute) {
		id = register(this);
		this.unloc = unloc;
		this.cost = cost;
		this.cooldown = cooldown;
		this.execute = execute;
	}

	public int cooldown() {
		return cooldown;
	}

	public int cost() {
		return cost;
	}

	public void execute(LivingEntity caster) {
		execute.accept(this, caster);
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

	public static TurmoilAbility getFromID(int id) {
		return id < 0 || id >= REGISTRY.size() ? null : REGISTRY.get(id);
	}

	private static int register(TurmoilAbility ability) {
		int id = REGISTRY.size();
		REGISTRY.add(ability);
		return id;
	}

	public TranslationTextComponent getTitle() {
		return new TranslationTextComponent(unloc.concat(".title"));
	}

	public TranslationTextComponent getDescription() {
		return new TranslationTextComponent(unloc.concat(".desc"));
	}

	public ResourceLocation getTexture() {
		String[] loc = unloc.contains(".") ? unloc.replaceAll("\\.", "/").split("/", 2) : new String[]{"minecraft", unloc};
		return new ResourceLocation(loc[0], "textures/".concat(loc[1].concat(".png")));
	}
}
