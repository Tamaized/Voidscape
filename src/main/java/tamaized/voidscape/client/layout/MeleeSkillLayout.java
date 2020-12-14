package tamaized.voidscape.client.layout;

import org.apache.logging.log4j.util.TriConsumer;
import tamaized.voidscape.turmoil.skills.MeleeSkills;
import tamaized.voidscape.turmoil.skills.TurmoilSkill;
import tamaized.voidscape.turmoil.skills.TurmoilSkills;

public class MeleeSkillLayout implements ISkillLayout {

	@Override
	public void fill(int x, int y, TriConsumer<TurmoilSkill, Integer, Integer> add) {
		y += 50;
		MeleeSkills skills = TurmoilSkills.MELEE_SKILLS;

		add.accept(skills.CORE, x, y);
	}

}
