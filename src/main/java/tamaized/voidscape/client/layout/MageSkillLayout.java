package tamaized.voidscape.client.layout;

import org.apache.logging.log4j.util.TriConsumer;
import tamaized.voidscape.turmoil.skills.MageSkills;
import tamaized.voidscape.turmoil.skills.TurmoilSkill;
import tamaized.voidscape.turmoil.skills.TurmoilSkills;

public class MageSkillLayout implements ISkillLayout {

	@Override
	public void fill(int x, int y, TriConsumer<TurmoilSkill, Integer, Integer> add) {
		y -= 50;
		MageSkills skills = TurmoilSkills.MAGE_SKILLS;

		add.accept(skills.CORE, x, y);

		final int x1 = x - 180;
		final int x2 = x - 60;
		final int x3 = x + 60;
		final int x4 = x + 180;

		final int xshift1 = -40;
		final int xshift2 = -10;
		final int xshift3 = 20;
		final int xshift4 = 50;

		final int y1 = y - 250;
		final int y2 = y1 - 40;
		final int y3 = y1 - 40 * 2;
		final int y4 = y1 - 40 * 3;
		final int y5 = y1 - 40 * 4;
		final int y6 = y1 - 40 * 5;

		add.accept(skills.INSANE_MAGE_1, x1, y1);
		addWithRNG(add, skills.ECHO, x1 + xshift1, y2);
		addWithRNG(add, skills.TRAUMATIZE, x1 + xshift2, y2);
		addWithRNG(add, skills.INSANE_MAGE_2, x1 + xshift3, y2);
		addWithRNG(add, skills.CONVERT, x1 + xshift1, y3);
		addWithRNG(add, skills.DEMORALIZE, x1 + xshift2, y3);
		addWithRNG(add, skills.INSANE_MAGE_3, x1 + xshift3, y3);
		addWithRNG(add, skills.EQUILIBRIUM, x1 + xshift1, y4);
		addWithRNG(add, skills.TERRORIZE, x1 + xshift2, y4);
		addWithRNG(add, skills.INSANE_MAGE_4, x1 + xshift3, y4);
		addWithRNG(add, skills.PSYCHOSIS, x1 + xshift1, y5);
		addWithRNG(add, skills.HYSTERIA, x1 + xshift2, y5);
		addWithRNG(add, skills.INSANE_MAGE_5, x1 + xshift3, y5);
		addWithRNG(add, skills.MANIA, x1 + xshift2, y6);
		addWithRNG(add, skills.DELIRIUM, x1 + xshift3, y6);

		add.accept(skills.VOIDMANCY_1, x2, y1);
		addWithRNG(add, skills.HOMINGBOLTS, x2 + xshift1, y2);
		addWithRNG(add, skills.VOIDICAURA, x2 + xshift2, y2);
		addWithRNG(add, skills.VOIDMANCY_2, x2 + xshift3, y2);
		addWithRNG(add, skills.PENETRATINGBOLTS, x2 + xshift1, y3);
		addWithRNG(add, skills.EMPOWERBOLT_2X, x2 + xshift2, y3);
		addWithRNG(add, skills.VOIDICBURST, x2 + xshift4, y3);
		addWithRNG(add, skills.VOIDMANCY_3, x2 + xshift3, y3);
		addWithRNG(add, skills.EXPLOSIVEBOLTS, x2 + xshift1 - 30, y4);
		addWithRNG(add, skills.VOIDICHEALING, x2 + xshift1, y4);
		addWithRNG(add, skills.VOIDICTELEPORT, x2 + xshift2, y4);
		addWithRNG(add, skills.EMPOWERBOLT_VULN, x2 + xshift4, y4);
		addWithRNG(add, skills.VOIDMANCY_4, x2 + xshift3, y4);
		addWithRNG(add, skills.NULLIFIEDBOLTS, x2 + xshift2, y5);
		addWithRNG(add, skills.NULLIFIEDCONVERSION, x2 + xshift2, y6);
		addWithRNG(add, skills.NULLIFIEDREJUVINATION, x2 + xshift1, y6);
		addWithRNG(add, skills.PUREVOIDICHEALING, x2 + xshift4, y5);
		addWithRNG(add, skills.VOIDMANCY_5, x2 + xshift3, y5);
		addWithRNG(add, skills.VOIDMANCERSTANCE, x2 + xshift3, y6);
		addWithRNG(add, skills.NULLSHIELD, x2 + xshift1, y6 - 40);
		addWithRNG(add, skills.NULLVULNERABILITY, x2 + xshift2, y6 - 40);

		add.accept(skills.VOIDIC_BOND, x3, y1);
		addWithRNG(add, skills.SPELLPOWER_1, x3 + xshift1, y2);
		addWithRNG(add, skills.SPELLPOWER_2, x3 + xshift1, y3);
		addWithRNG(add, skills.SPELLPOWER_3, x3 + xshift1, y4);
		addWithRNG(add, skills.SPELLPOWER_4, x3 + xshift1, y5);
		addWithRNG(add, skills.SPELLPOWER_5, x3 + xshift1, y6);
		addWithRNG(add, skills.BONDING_1, x3 + xshift2, y2);
		addWithRNG(add, skills.BONDING_2, x3 + xshift2, y3);
		addWithRNG(add, skills.BONDING_3, x3 + xshift2, y4);
		addWithRNG(add, skills.BONDING_4, x3 + xshift2, y5);
		addWithRNG(add, skills.BONDING_5, x3 + xshift2, y6);
		addWithRNG(add, skills.SPELL_DEX_1, x3 + xshift3, y2);
		addWithRNG(add, skills.SPELL_DEX_2, x3 + xshift3, y3);
		addWithRNG(add, skills.SPELL_DEX_3, x3 + xshift3, y4);
		addWithRNG(add, skills.SPELL_DEX_4, x3 + xshift3, y5);
		addWithRNG(add, skills.SPELL_DEX_5, x3 + xshift3, y6);
		addWithRNG(add, skills.ASTUTE_UNDERSTANDING_1, x3 + xshift4, y2);
		addWithRNG(add, skills.ASTUTE_UNDERSTANDING_2, x3 + xshift4, y3);
		addWithRNG(add, skills.ASTUTE_UNDERSTANDING_3, x3 + xshift4, y4);
		addWithRNG(add, skills.ASTUTE_UNDERSTANDING_4, x3 + xshift4, y5);
		addWithRNG(add, skills.ASTUTE_UNDERSTANDING_5, x3 + xshift4, y6);

		add.accept(skills.VOIDIC_ARCHER_1, x4, y1);
		addWithRNG(add, skills.ARROWIMBUE_SPELLLIKE, x4 + xshift2, y2);
		addWithRNG(add, skills.FLAMESHOT, x4 + xshift3, y2);
		addWithRNG(add, skills.VOIDIC_ARCHER_2, x4 + xshift4, y2);
		addWithRNG(add, skills.ARROWIMBUE_FOCUS, x4 + xshift2, y3);
		addWithRNG(add, skills.FORCESHOT, x4 + xshift3, y3);
		addWithRNG(add, skills.VOIDIC_ARCHER_3, x4 + xshift4, y3);
		addWithRNG(add, skills.ARROWIMBUE_PENETRATING, x4 + xshift2, y4);
		addWithRNG(add, skills.VOIDIC_SHOT, x4 + xshift3, y4);
		addWithRNG(add, skills.VOIDIC_ARCHER_4, x4 + xshift4, y4);
		addWithRNG(add, skills.ARROWIMBUE_DESTRUCTIVE, x4 + xshift2, y5);
		addWithRNG(add, skills.SLAYINGSHOT, x4 + xshift3, y5);
		addWithRNG(add, skills.VOIDIC_ARCHER_5, x4 + xshift4, y5);
		addWithRNG(add, skills.MANYSHOT, x4 + xshift3, y6);
		addWithRNG(add, skills.NULLSHOT, x4 + xshift4, y6);

	}

}
