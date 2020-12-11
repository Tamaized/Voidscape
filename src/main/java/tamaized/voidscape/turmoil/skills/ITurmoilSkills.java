package tamaized.voidscape.turmoil.skills;

import net.minecraft.util.text.TranslationTextComponent;
import tamaized.voidscape.Voidscape;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public interface ITurmoilSkills {

	String location();

	default Builder builder(String name) {
		return new Builder(location(), name);
	}

	class Builder {

		private final String loc;
		private final String name;
		private int spent = 0;
		private int cost = 1;
		private boolean core = false;
		private List<TurmoilSkill> required = new ArrayList<>();

		private Builder(String loc, String name) {
			this.loc = loc;
			this.name = name;
		}

		Builder spent(int spent) {
			this.spent = spent;
			return this;
		}

		Builder cost(int cost) {
			this.cost = cost;
			return this;
		}

		Builder core() {
			core = true;
			return this;
		}

		Builder required(TurmoilSkill... skills) {
			Collections.addAll(required, skills);
			return this;
		}

		private static TranslationTextComponent format(String loc, String append, Object... args) {
			return new TranslationTextComponent(Voidscape.MODID + ".skills." + loc + "." + append, args);
		}

		TurmoilSkill build() {
			return new TurmoilSkill(format(loc, name.concat(".title")), format(loc, name.concat(".desc")), spent, cost, core, required.toArray(new TurmoilSkill[0]));
		}

	}
}
