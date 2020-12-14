package tamaized.voidscape.turmoil.skills;

import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.turmoil.abilities.TurmoilAbility;

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
		private ResourceLocation texture = TextureManager.INTENTIONAL_MISSING_TEXTURE;
		private int spent = 0;
		private int cost = 1;
		private boolean core = false;
		private List<TurmoilSkill> required = new ArrayList<>();
		private List<TurmoilAbility> abilities = new ArrayList<>();
		private boolean disabled;

		private Builder(String loc, String name) {
			this.loc = loc;
			this.name = name;
		}

		Builder texture(ResourceLocation loc) {
			texture = loc;
			return this;
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

		Builder abilities(TurmoilAbility... abilities) {
			Collections.addAll(this.abilities, abilities);
			return this;
		}

		Builder disabled() {
			disabled = true;
			return this;
		}

		private static TranslationTextComponent format(String loc, String append, Object... args) {
			return new TranslationTextComponent(Voidscape.MODID + ".skills." + loc + "." + append, args);
		}

		TurmoilSkill build() {
			return new TurmoilSkill(format(loc, name.concat(".title")), format(loc, name.concat(".desc")), texture, spent, cost, core, required.toArray(new TurmoilSkill[0]), abilities.toArray(new TurmoilAbility[0]), disabled);
		}

	}
}
