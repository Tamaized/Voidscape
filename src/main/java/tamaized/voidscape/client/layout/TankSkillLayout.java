package tamaized.voidscape.client.layout;

import org.apache.logging.log4j.util.TriConsumer;
import tamaized.voidscape.turmoil.skills.TankSkills;
import tamaized.voidscape.turmoil.skills.TurmoilSkill;
import tamaized.voidscape.turmoil.skills.TurmoilSkills;

public class TankSkillLayout implements ISkillLayout {

	@Override
	public void fill(int x, int y, TriConsumer<TurmoilSkill, Integer, Integer> add) {
		x -= 50;
		TankSkills skills = TurmoilSkills.TANK_SKILLS;

		add.accept(skills.CORE, x, y);

		final int y1 = y - 180;
		final int y2 = y - 60;
		final int y3 = y + 60;
		final int y4 = y + 180;

		final int yshift1 = -40;
		final int yshift2 = -10;
		final int yshift3 = 20;
		final int yshift4 = 50;

		final int x1 = x - 250;
		final int x2 = x1 - 40;
		final int x3 = x1 - 40 * 2;
		final int x4 = x1 - 40 * 3;
		final int x5 = x1 - 40 * 4;
		final int x6 = x1 - 40 * 5;

		add.accept(skills.INSANE_BEAST_1, x1, y1);
		addWithRNG(add, skills.ADRENALINE, x2, y1 + yshift1);
		addWithRNG(add, skills.TUNNEL_VISION, x2, y1 + yshift2);
		addWithRNG(add, skills.INSANE_BEAST_2, x2, y1 + yshift3);
		addWithRNG(add, skills.RAGE, x3, y1 + yshift1);
		addWithRNG(add, skills.EMPOWER_STRIKE_GASH, x3, y1 + yshift2);
		addWithRNG(add, skills.INSANE_BEAST_3, x3, y1 + yshift3);
		addWithRNG(add, skills.STANCE_CHAOTIC, x4, y1 + yshift1);
		addWithRNG(add, skills.EMPOWER_STRIKE_BLOODTHIRST, x4, y1 + yshift2);
		addWithRNG(add, skills.INSANE_BEAST_4, x4, y1 + yshift3);
		addWithRNG(add, skills.FURY, x5, y1 + yshift1);
		addWithRNG(add, skills.INSANE_BEAST_5, x5, y1 + yshift3);
		addWithRNG(add, skills.RIP_AND_TEAR, x6, y1 + yshift2);

		add.accept(skills.VOIDIC_DEFENDER_1, x1, y2);
		addWithRNG(add, skills.BULWARK, x2, y2 + yshift2);
		addWithRNG(add, skills.SHOUT, x2, y2 + yshift1);
		addWithRNG(add, skills.VOIDIC_DEFENDER_2, x2, y2 + yshift3);
		addWithRNG(add, skills.STANCE_DEFENSIVE, x3, y2 + yshift2);
		addWithRNG(add, skills.EMPOWER_STRIKE_THREAT, x3, y2 + yshift1);
		addWithRNG(add, skills.VOIDIC_DEFENDER_3, x3, y2 + yshift3);
		addWithRNG(add, skills.STAND_YOUR_GROUND, x4, y2 + yshift2);
		addWithRNG(add, skills.EMPOWER_STRIKE_RETRIBUTION, x4, y2 + yshift1);
		addWithRNG(add, skills.VOIDIC_DEFENDER_4, x4, y2 + yshift3);
		addWithRNG(add, skills.BRACE_FOR_IMPACT, x5, y2 + yshift2);
		addWithRNG(add, skills.EMPOWER_STRIKE_SAP, x5, y2 + yshift1);
		addWithRNG(add, skills.VOIDIC_DEFENDER_5, x5, y2 + yshift3);
		addWithRNG(add, skills.IRON_WILL, x6, y2 + yshift2);

		add.accept(skills.VOIDIC_BOND, x1, y3);
		addWithRNG(add, skills.VOIDIC_DAMAGE_1, x2, y3 + yshift1);
		addWithRNG(add, skills.VOIDIC_DAMAGE_2, x3, y3 + yshift1);
		addWithRNG(add, skills.VOIDIC_DAMAGE_3, x4, y3 + yshift1);
		addWithRNG(add, skills.VOIDIC_DAMAGE_4, x5, y3 + yshift1);
		addWithRNG(add, skills.VOIDIC_DAMAGE_5, x6, y3 + yshift1);
		addWithRNG(add, skills.VOIDIC_BONDING_1, x2, y3 + yshift2);
		addWithRNG(add, skills.VOIDIC_BONDING_2, x3, y3 + yshift2);
		addWithRNG(add, skills.VOIDIC_BONDING_3, x4, y3 + yshift2);
		addWithRNG(add, skills.VOIDIC_BONDING_4, x5, y3 + yshift2);
		addWithRNG(add, skills.VOIDIC_BONDING_5, x6, y3 + yshift2);
		addWithRNG(add, skills.VOIDIC_HAMP_1, x2, y3 + yshift3);
		addWithRNG(add, skills.VOIDIC_HAMP_2, x3, y3 + yshift3);
		addWithRNG(add, skills.VOIDIC_HAMP_3, x4, y3 + yshift3);
		addWithRNG(add, skills.VOIDIC_HAMP_4, x5, y3 + yshift3);
		addWithRNG(add, skills.VOIDIC_HAMP_5, x6, y3 + yshift3);
		addWithRNG(add, skills.VOIDIC_DEFENSE_1, x2, y3 + yshift4);
		addWithRNG(add, skills.VOIDIC_DEFENSE_2, x3, y3 + yshift4);
		addWithRNG(add, skills.VOIDIC_DEFENSE_3, x4, y3 + yshift4);
		addWithRNG(add, skills.VOIDIC_DEFENSE_4, x5, y3 + yshift4);
		addWithRNG(add, skills.VOIDIC_DEFENSE_5, x6, y3 + yshift4);

		add.accept(skills.TACTICIAN_1, x1, y4);
		addWithRNG(add, skills.EMPOWER_SHIELD_2X_NULL, x2, y4 + yshift4);
		addWithRNG(add, skills.BACKSTEP, x2, y4 + yshift3);
		addWithRNG(add, skills.TACTICIAN_2, x2, y4 + yshift2);
		addWithRNG(add, skills.STANCE_STALWART, x3, y4 + yshift4);
		addWithRNG(add, skills.FORESIGHT, x3, y4 + yshift3);
		addWithRNG(add, skills.TACTICIAN_3, x3, y4 + yshift2);
		addWithRNG(add, skills.STRATEGIZE, x4, y4 + yshift4);
		addWithRNG(add, skills.EMPOWER_SHIELD_REFLECT, x4, y4 + yshift3);
		addWithRNG(add, skills.TACTICIAN_4, x4, y4 + yshift2);
		addWithRNG(add, skills.PREDICTION, x5, y4 + yshift4);
		addWithRNG(add, skills.EMPOWER_SHIELD_EMBRACE, x5, y4 + yshift3);
		addWithRNG(add, skills.TACTICIAN_5, x5, y4 + yshift2);
		addWithRNG(add, skills.TACTICAL_ADVANTAGE, x6, y4 + yshift3);
	}

}
