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

		final int x1 = x - 180;
		final int x2 = x - 60;
		final int x3 = x + 60;
		final int x4 = x + 180;

		final int xshift1 = -40;
		final int xshift2 = -10;
		final int xshift3 = 20;
		final int xshift4 = 50;

		final int y1 = y + 50;
		final int y2 = y + 90;
		final int y3 = y + 130;
		final int y4 = y + 170;
		final int y5 = y + 210;
		final int y6 = y + 250;

		add.accept(skills.CHAOS_BLADE_1, x1, y1);
		addWithRNG(add, skills.EMPOWER_ATTACK_BLEED, x1 + xshift1, y2);
		addWithRNG(add, skills.SENSE_WEAKNESS, x1 + xshift2, y2);
		addWithRNG(add, skills.CHAOS_BLADE_2, x1 + xshift3, y2);
		addWithRNG(add, skills.REJUVENATE, x1 + xshift1, y3);
		addWithRNG(add, skills.EMPOWER_ATTACK_SLASH, x1 + xshift2, y3);
		addWithRNG(add, skills.CHAOS_BLADE_3, x1 + xshift3, y3);
		addWithRNG(add, skills.OVERWHELMING_FORCE, x1 + xshift1, y4);
		addWithRNG(add, skills.SHARP_EDGE, x1 + xshift2, y4);
		addWithRNG(add, skills.CHAOS_BLADE_4, x1 + xshift3, y4);
		addWithRNG(add, skills.BLOODBATH, x1 + xshift1, y5);
		addWithRNG(add, skills.CHAOS_BLADE_5, x1 + xshift3, y5);
		addWithRNG(add, skills.PRIMAL_RAGE, x1 + xshift2, y6);

		add.accept(skills.VOIDIC_FIGHTER_1, x2, y1);
		addWithRNG(add, skills.WAY_OF_THE_BLADE_1, x2 + xshift2, y2);
		addWithRNG(add, skills.EMPOWER_ATTACK_SLICING, x2 + xshift1, y2);
		addWithRNG(add, skills.VOIDIC_FIGHTER_2, x2 + xshift3, y2);
		addWithRNG(add, skills.WAY_OF_THE_BLADE_2, x2 + xshift2, y3);
		addWithRNG(add, skills.EMPOWER_ATTACK_2X, x2 + xshift1, y3);
		addWithRNG(add, skills.VOIDIC_FIGHTER_3, x2 + xshift3, y3);
		addWithRNG(add, skills.WAY_OF_THE_BLADE_3, x2 + xshift2, y4);
		addWithRNG(add, skills.EMPOWER_ATTACK_LIFESTEAL, x2 + xshift1, y4);
		addWithRNG(add, skills.VOIDIC_FIGHTER_4, x2 + xshift3, y4);
		addWithRNG(add, skills.WAY_OF_THE_BLADE_4, x2 + xshift2, y5);
		addWithRNG(add, skills.EMPOWER_ATTACK_3X, x2 + xshift1, y5);
		addWithRNG(add, skills.VOIDIC_FIGHTER_5, x2 + xshift3, y5);
		addWithRNG(add, skills.WAY_OF_THE_BLADE_5, x2 + xshift2, y6);
		addWithRNG(add, skills.BLITZ, x2 + xshift1, y6);

		add.accept(skills.VOIDIC_BOND, x3, y1);
		addWithRNG(add, skills.VOIDIC_DAMAGE_1, x3 + xshift1, y2);
		addWithRNG(add, skills.VOIDIC_DAMAGE_2, x3 + xshift1, y3);
		addWithRNG(add, skills.VOIDIC_DAMAGE_3, x3 + xshift1, y4);
		addWithRNG(add, skills.VOIDIC_DAMAGE_4, x3 + xshift1, y5);
		addWithRNG(add, skills.VOIDIC_DAMAGE_5, x3 + xshift1, y6);
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

		add.accept(skills.TEMPEST_1, x4, y1);
		addWithRNG(add, skills.DEFT_STRIKES_1, x4 + xshift4, y2);
		addWithRNG(add, skills.CLEAVE, x4 + xshift3, y2);
		addWithRNG(add, skills.TEMPEST_2, x4 + xshift2, y2);
		addWithRNG(add, skills.DEFT_STRIKES_2, x4 + xshift4, y3);
		addWithRNG(add, skills.GREATER_CLEAVE, x4 + xshift3, y3);
		addWithRNG(add, skills.TEMPEST_3, x4 + xshift2, y3);
		addWithRNG(add, skills.DEFT_STRIKES_3, x4 + xshift4, y4);
		addWithRNG(add, skills.WHIRLWIND, x4 + xshift3, y4);
		addWithRNG(add, skills.TEMPEST_4, x4 + xshift2, y4);
		addWithRNG(add, skills.DEFT_STRIKES_4, x4 + xshift4, y5);
		addWithRNG(add, skills.TWO_WEAPON_FIGHTING, x4 + xshift3, y5);
		addWithRNG(add, skills.TEMPEST_5, x4 + xshift2, y5);
		addWithRNG(add, skills.DEFT_STRIKES_5, x4 + xshift3, y6);
		addWithRNG(add, skills.WHIRLING_BLADES, x4 + xshift2, y6);
	}

}
