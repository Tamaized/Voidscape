package tamaized.voidscape.client.layout;

import org.apache.logging.log4j.util.TriConsumer;
import tamaized.voidscape.turmoil.skills.HealerSkills;
import tamaized.voidscape.turmoil.skills.TurmoilSkill;
import tamaized.voidscape.turmoil.skills.TurmoilSkills;

public class HealerSkillLayout implements ISkillLayout {

	@Override
	public void fill(int x, int y, TriConsumer<TurmoilSkill, Integer, Integer> add) {
		x += 50;
		HealerSkills skills = TurmoilSkills.HEALER_SKILLS;

		add.accept(skills.CORE, x, y);

		final int y1 = y - 180;
		final int y2 = y - 60;
		final int y3 = y + 60;
		final int y4 = y + 180;

		final int yshift1 = -40;
		final int yshift2 = -10;
		final int yshift3 = 20;
		final int yshift4 = 50;

		final int x1 = x + 250;
		final int x2 = x1 + 40;
		final int x3 = x1 + 40 * 2;
		final int x4 = x1 + 40 * 3;
		final int x5 = x1 + 40 * 4;
		final int x6 = x1 + 40 * 5;

		add.accept(skills.MAD_PRIEST_1, x1, y1);
		addWithRNG(add, skills.MEND, x2, y1 + yshift1);
		addWithRNG(add, skills.MIND_WARP, x2, y1 + yshift2);
		addWithRNG(add, skills.MAD_PRIEST_2, x2, y1 + yshift3);
		addWithRNG(add, skills.EQUILIZE, x3, y1 + yshift1);
		addWithRNG(add, skills.UNCARING_MADNESS, x3, y1 + yshift2);
		addWithRNG(add, skills.MAD_PRIEST_3, x3, y1 + yshift3);
		addWithRNG(add, skills.EMERGENCY_TACTICS, x4, y1 + yshift1);
		addWithRNG(add, skills.GAZE, x4, y1 + yshift2);
		addWithRNG(add, skills.MAD_PRIEST_4, x4, y1 + yshift3);
		addWithRNG(add, skills.MIND_LINK_POS, x5, y1 + yshift1);
		addWithRNG(add, skills.MAD_PRIEST_5, x5, y1 + yshift3);
		addWithRNG(add, skills.MIND_LINK_NEG, x6, y1 + yshift2);

		add.accept(skills.VOIDIC_CLERIC_1, x1, y2);
		addWithRNG(add, skills.HEALING_BOLT, x2, y2 + yshift2);
		addWithRNG(add, skills.HEALING_BLAST, x2, y2 + yshift1);
		addWithRNG(add, skills.VOIDIC_CLERIC_2, x2, y2 + yshift3);
		addWithRNG(add, skills.GREATER_HEALING_BOLT, x3, y2 + yshift2);
		addWithRNG(add, skills.GREATER_HEALING_BLAST, x3, y2 + yshift1);
		addWithRNG(add, skills.VOIDIC_CLERIC_3, x3, y2 + yshift3);
		addWithRNG(add, skills.MAJOR_HEALING_BOLT, x4, y2 + yshift2);
		addWithRNG(add, skills.MAJOR_HEALING_BLAST, x4, y2 + yshift1);
		addWithRNG(add, skills.VOIDIC_CLERIC_4, x4, y2 + yshift3);
		addWithRNG(add, skills.ABSOLUTE_HEALING_BOLT, x5, y2 + yshift2);
		addWithRNG(add, skills.ABSOLUTE_HEALING_BLAST, x5, y2 + yshift1);
		addWithRNG(add, skills.VOIDIC_CLERIC_5, x5, y2 + yshift3);
		addWithRNG(add, skills.STALWART_SHIELD, x6, y2 + yshift2);

		add.accept(skills.VOIDIC_BOND, x1, y3);
		addWithRNG(add, skills.SPELLPOWER_1, x2, y3 + yshift1);
		addWithRNG(add, skills.SPELLPOWER_2, x3, y3 + yshift1);
		addWithRNG(add, skills.SPELLPOWER_3, x4, y3 + yshift1);
		addWithRNG(add, skills.SPELLPOWER_4, x5, y3 + yshift1);
		addWithRNG(add, skills.SPELLPOWER_5, x6, y3 + yshift1);
		addWithRNG(add, skills.BONDING_1, x2, y3 + yshift2);
		addWithRNG(add, skills.BONDING_2, x3, y3 + yshift2);
		addWithRNG(add, skills.BONDING_3, x4, y3 + yshift2);
		addWithRNG(add, skills.BONDING_4, x5, y3 + yshift2);
		addWithRNG(add, skills.BONDING_5, x6, y3 + yshift2);
		addWithRNG(add, skills.SPELL_DEX_1, x2, y3 + yshift3);
		addWithRNG(add, skills.SPELL_DEX_2, x3, y3 + yshift3);
		addWithRNG(add, skills.SPELL_DEX_3, x4, y3 + yshift3);
		addWithRNG(add, skills.SPELL_DEX_4, x5, y3 + yshift3);
		addWithRNG(add, skills.SPELL_DEX_5, x6, y3 + yshift3);
		addWithRNG(add, skills.ASTUTE_UNDERSTANDING_1, x2, y3 + yshift4);
		addWithRNG(add, skills.ASTUTE_UNDERSTANDING_2, x3, y3 + yshift4);
		addWithRNG(add, skills.ASTUTE_UNDERSTANDING_3, x4, y3 + yshift4);
		addWithRNG(add, skills.ASTUTE_UNDERSTANDING_4, x5, y3 + yshift4);
		addWithRNG(add, skills.ASTUTE_UNDERSTANDING_5, x6, y3 + yshift4);

		add.accept(skills.VOIDS_FAVOR_1, x1, y4);
		addWithRNG(add, skills.HEALING_AURA, x2, y4 + yshift4);
		addWithRNG(add, skills.EMPOWER_SWORD_OSMOSIS, x2, y4 + yshift3);
		addWithRNG(add, skills.VOIDS_FAVOR_2, x2, y4 + yshift2);
		addWithRNG(add, skills.HEALING_BURST, x3, y4 + yshift4);
		addWithRNG(add, skills.ANTICIPATION, x3, y4 + yshift3);
		addWithRNG(add, skills.VOIDS_FAVOR_3, x3, y4 + yshift2);
		addWithRNG(add, skills.HEALING_RAY, x4, y4 + yshift4);
		addWithRNG(add, skills.EMPOWER_SWORD_2X, x4, y4 + yshift3);
		addWithRNG(add, skills.VOIDS_FAVOR_4, x4, y4 + yshift2);
		addWithRNG(add, skills.VOIDIC_PROTECTION, x5, y4 + yshift4);
		addWithRNG(add, skills.EMPOWER_SWORD_GREATER_OSMOSIS, x5, y4 + yshift3);
		addWithRNG(add, skills.VOIDS_FAVOR_5, x5, y4 + yshift2);
		addWithRNG(add, skills.DETERMINATION, x6, y4 + yshift3);
	}

}
