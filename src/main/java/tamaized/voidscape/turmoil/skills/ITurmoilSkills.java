package tamaized.voidscape.turmoil.skills;

import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.client.ClientUtil;
import tamaized.voidscape.turmoil.abilities.TurmoilAbility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public interface ITurmoilSkills {

	String location();

	default Builder builder(String name) {
		return new Builder(location(), name);
	}

	default Builder generic(String name) {
		return new Builder("generic", name);
	}

	class Builder {

		private final String loc;
		private final String name;
		private Supplier<Supplier<ResourceLocation>> texture = () -> ClientUtil::getMissingTexture;
		private int spent = 0;
		private int cost = 1;
		private TurmoilSkill.CoreType core = TurmoilSkill.CoreType.Null;
		private List<TurmoilSkill> required = new ArrayList<>();
		private List<TurmoilAbility> abilities = new ArrayList<>();
		private TurmoilSkill.Stats.Builder stats = TurmoilSkill.Stats.Builder.begin();
		private boolean disabled;
		private String desc = "";

		private Builder(String loc, String name) {
			this.loc = loc;
			this.name = name;
		}

		Builder description(String desc) {
			this.desc = desc;
			return this;
		}

		Builder noDescription() {
			this.desc = null;
			return this;
		}

		Builder texture(ResourceLocation loc) {
			texture = () -> () -> loc;
			return this;
		}

		Builder texture(String loc) {
			return texture(new ResourceLocation(Voidscape.MODID, "textures/" + loc + ".png"));
		}

		Builder spent(int spent) {
			this.spent = spent;
			return this;
		}

		Builder cost(int cost) {
			this.cost = cost;
			return this;
		}

		Builder core(TurmoilSkill.CoreType type) {
			core = type;
			return this;
		}

		Builder required(TurmoilSkill... skills) {
			Collections.addAll(required, skills);
			return this;
		}

		Builder abilities(TurmoilAbility... abilities) {
			Collections.addAll(this.abilities, abilities);
			return this;
		}

		Builder stats(Consumer<TurmoilSkill.Stats.Builder> stats) {
			stats.accept(this.stats);
			return this;
		}

		Builder disabled() {
			disabled = true;
			return this;
		}

		private static TranslatableComponent format(String loc, String append, Object... args) {
			return new TranslatableComponent(Voidscape.MODID + ".skills." + loc + "." + append, args);
		}

		TurmoilSkill build() {
			return new TurmoilSkill(format(loc, name.concat(".title")), desc == null ? new TranslatableComponent("") : format(loc, desc.isEmpty() ? name.concat(".desc") : desc.concat(".desc")), texture, spent, cost, core, required.toArray(new TurmoilSkill[0]), abilities.toArray(new TurmoilAbility[0]), stats.build(), disabled);
		}

	}
}
