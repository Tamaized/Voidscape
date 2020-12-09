package tamaized.voidscape.turmoil.skills;

import net.minecraft.util.text.TranslationTextComponent;
import tamaized.voidscape.Voidscape;

public interface ITurmoilSkills {

	String location();

	default TurmoilSkill make(String name, int spent, int cost) {
		return make(name, spent, cost, false);
	}

	default TurmoilSkill make(String name, int spent, int cost, boolean core) {
		return new TurmoilSkill(format(name.concat(".title")), format(name.concat(".desc"), spent, cost), spent, cost, core);
	}

	default TranslationTextComponent format(String append, Object... args) {
		return new TranslationTextComponent(Voidscape.MODID + ".skills." + location() + "." + append, args);
	}
}
