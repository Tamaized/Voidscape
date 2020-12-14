package tamaized.voidscape.client.layout;

import org.apache.logging.log4j.util.TriConsumer;
import tamaized.voidscape.turmoil.skills.TurmoilSkill;
import tamaized.voidscape.turmoil.skills.TurmoilSkills;
import tamaized.voidscape.turmoil.skills.VoidmancerSkills;

public class VoidmancerSkillLayout implements ISkillLayout {

	@Override
	public void fill(int x, int y, TriConsumer<TurmoilSkill, Integer, Integer> add) {
		y -= 50;
		VoidmancerSkills skills = TurmoilSkills.VOIDMANCER_SKILLS;

		add.accept(skills.CORE, x, y);

		add.accept(skills.INSANE_MAGE_1, x - 180, y - 50);

		add.accept(skills.VOIDMANCY_1, x - 60, y - 50);

		add.accept(skills.VOIDIC_BOND, x + 60, y - 50);

		add.accept(skills.SPELLPOWER_1, x + 50, y - 90);
		add.accept(skills.SPELLPOWER_2, x + 40, y - 130);
		add.accept(skills.SPELLPOWER_3, x + 30, y - 170);
		add.accept(skills.SPELLPOWER_4, x + 20, y - 210);
		add.accept(skills.SPELLPOWER_5, x + 10, y - 250);

		add.accept(skills.VOIDIC_ARCHER_1, x + 140, y - 50);
	}

}
