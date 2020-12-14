package tamaized.voidscape.client.layout;

import org.apache.logging.log4j.util.TriConsumer;
import tamaized.voidscape.turmoil.skills.TurmoilSkill;

public interface ISkillLayout {

	void fill(int x, int y, TriConsumer<TurmoilSkill, Integer, Integer> add);

}
