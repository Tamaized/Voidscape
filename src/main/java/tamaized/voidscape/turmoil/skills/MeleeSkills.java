package tamaized.voidscape.turmoil.skills;

import tamaized.voidscape.turmoil.abilities.MeleeAbilities;

public class MeleeSkills implements ITurmoilSkills {

	public final TurmoilSkill CORE = builder("core").noDescription().core().abilities(MeleeAbilities.RUSH).build();

	public final TurmoilSkill VOIDIC_BOND = generic("voidic_bond").required(CORE).stats(stats -> stats.voidicDamage(1).rechargeRate(1)).build();
	public final TurmoilSkill VOIDIC_DAMAGE_1 = generic("voidic_damage_1").description("voidic_damage").required(VOIDIC_BOND).stats(stats -> stats.voidicDamage(1)).build();
	public final TurmoilSkill VOIDIC_DAMAGE_2 = generic("voidic_damage_2").description("voidic_damage").required(VOIDIC_DAMAGE_1).stats(stats -> stats.voidicDamage(1)).build();
	public final TurmoilSkill VOIDIC_DAMAGE_3 = generic("voidic_damage_3").description("voidic_damage").required(VOIDIC_DAMAGE_2).stats(stats -> stats.voidicDamage(1)).build();
	public final TurmoilSkill VOIDIC_DAMAGE_4 = generic("voidic_damage_4").description("voidic_damage").required(VOIDIC_DAMAGE_3).stats(stats -> stats.voidicDamage(2)).build();
	public final TurmoilSkill VOIDIC_DAMAGE_5 = generic("voidic_damage_5").description("voidic_damage").required(VOIDIC_DAMAGE_4).stats(stats -> stats.voidicDamage(5)).build();
	public final TurmoilSkill BONDING_1 = generic("voidic_bonding_1").description("voidic_bonding").required(VOIDIC_BOND).stats(stats -> stats.rechargeRate(1)).build();
	public final TurmoilSkill BONDING_2 = generic("voidic_bonding_2").description("voidic_bonding").required(BONDING_1).stats(stats -> stats.rechargeRate(1)).build();
	public final TurmoilSkill BONDING_3 = generic("voidic_bonding_3").description("voidic_bonding").required(BONDING_2).stats(stats -> stats.rechargeRate(1)).build();
	public final TurmoilSkill BONDING_4 = generic("voidic_bonding_4").description("voidic_bonding").required(BONDING_3).stats(stats -> stats.rechargeRate(2)).build();
	public final TurmoilSkill BONDING_5 = generic("voidic_bonding_5").description("voidic_bonding").required(BONDING_4).stats(stats -> stats.rechargeRate(4)).build();
	public final TurmoilSkill SPELL_DEX_1 = generic("spell_dex_1").description("spell_dex").required(VOIDIC_BOND).stats(stats -> stats.cooldown(5)).build();
	public final TurmoilSkill SPELL_DEX_2 = generic("spell_dex_2").description("spell_dex").required(SPELL_DEX_1).stats(stats -> stats.cooldown(5)).build();
	public final TurmoilSkill SPELL_DEX_3 = generic("spell_dex_3").description("spell_dex").required(SPELL_DEX_2).stats(stats -> stats.cooldown(5)).build();
	public final TurmoilSkill SPELL_DEX_4 = generic("spell_dex_4").description("spell_dex").required(SPELL_DEX_3).stats(stats -> stats.cooldown(5)).build();
	public final TurmoilSkill SPELL_DEX_5 = generic("spell_dex_5").description("spell_dex").required(SPELL_DEX_4).stats(stats -> stats.cooldown(10)).build();
	public final TurmoilSkill ASTUTE_UNDERSTANDING_1 = generic("astute_understanding_1").description("astute_understanding").required(VOIDIC_BOND).stats(stats -> stats.cost(1)).build();
	public final TurmoilSkill ASTUTE_UNDERSTANDING_2 = generic("astute_understanding_2").description("astute_understanding").required(ASTUTE_UNDERSTANDING_1).stats(stats -> stats.cost(1)).build();
	public final TurmoilSkill ASTUTE_UNDERSTANDING_3 = generic("astute_understanding_3").description("astute_understanding").required(ASTUTE_UNDERSTANDING_2).stats(stats -> stats.cost(1)).build();
	public final TurmoilSkill ASTUTE_UNDERSTANDING_4 = generic("astute_understanding_4").description("astute_understanding").required(ASTUTE_UNDERSTANDING_3).stats(stats -> stats.cost(2)).build();
	public final TurmoilSkill ASTUTE_UNDERSTANDING_5 = generic("astute_understanding_5").description("astute_understanding").required(ASTUTE_UNDERSTANDING_4).stats(stats -> stats.cost(5)).build();

	public final TurmoilSkill VOIDIC_FIGHTER_1 = builder("voidic_fighter_1").noDescription().required(CORE).stats(stats -> stats.voidicDamage(1).voidicDamageReduction(1)).build();
	public final TurmoilSkill WAY_OF_THE_BLADE_1 = builder("way_of_the_blade_1").noDescription().required(VOIDIC_FIGHTER_1).stats(stats -> stats.voidicDamage(1)).build();
	public final TurmoilSkill EMPOWER_ATTACK_SLICING = builder("empower_attack_slicing").required(VOIDIC_FIGHTER_1).build();
	public final TurmoilSkill VOIDIC_FIGHTER_2 = builder("voidic_fighter_2").noDescription().required(VOIDIC_FIGHTER_1).stats(stats -> stats.voidicDamage(1).voidicDamageReduction(1)).build();
	public final TurmoilSkill WAY_OF_THE_BLADE_2 = builder("way_of_the_blade_2").noDescription().required(VOIDIC_FIGHTER_2, WAY_OF_THE_BLADE_1).stats(stats -> stats.voidicDamage(1)).build();
	public final TurmoilSkill EMPOWER_ATTACK_2X = builder("empower_attack_2x").required(VOIDIC_FIGHTER_2).build();
	public final TurmoilSkill VOIDIC_FIGHTER_3 = builder("voidic_fighter_3").noDescription().required(VOIDIC_FIGHTER_2).stats(stats -> stats.voidicDamage(1).voidicDamageReduction(1)).build();
	public final TurmoilSkill WAY_OF_THE_BLADE_3 = builder("way_of_the_blade_3").noDescription().required(VOIDIC_FIGHTER_3, WAY_OF_THE_BLADE_2).stats(stats -> stats.voidicDamage(1)).build();
	public final TurmoilSkill EMPOWER_ATTACK_LIFESTEAL = builder("empower_attack_lifesteal").required(VOIDIC_FIGHTER_3).build();
	public final TurmoilSkill VOIDIC_FIGHTER_4 = builder("voidic_fighter_4").noDescription().required(VOIDIC_FIGHTER_3).stats(stats -> stats.voidicDamage(2).voidicDamageReduction(2)).build();
	public final TurmoilSkill WAY_OF_THE_BLADE_4 = builder("way_of_the_blade_4").noDescription().required(VOIDIC_FIGHTER_4, WAY_OF_THE_BLADE_3).stats(stats -> stats.voidicDamage(2)).build();
	public final TurmoilSkill EMPOWER_ATTACK_3X = builder("empower_attack_3x").required(VOIDIC_FIGHTER_4).build();
	public final TurmoilSkill VOIDIC_FIGHTER_5 = builder("voidic_fighter_5").noDescription().required(VOIDIC_FIGHTER_4).stats(stats -> stats.voidicDamage(5).voidicDamageReduction(5)).build();
	public final TurmoilSkill WAY_OF_THE_BLADE_5 = builder("way_of_the_blade_5").noDescription().required(VOIDIC_FIGHTER_5, WAY_OF_THE_BLADE_4).stats(stats -> stats.voidicDamage(5)).build();
	public final TurmoilSkill BLITZ = builder("blitz").required(VOIDIC_FIGHTER_5).build();

	public final TurmoilSkill TEMPEST_1 = builder("tempest_1").noDescription().required(CORE).stats(stats -> stats.voidicDamage(1)).build();
	public final TurmoilSkill DEFT_STRIKES_1 = builder("deft_strikes_1").noDescription().required(TEMPEST_1).stats(stats -> stats.cooldown(1)).build();
	public final TurmoilSkill CLEAVE = builder("cleave").required(TEMPEST_1).build();
	public final TurmoilSkill TEMPEST_2 = builder("tempest_2").noDescription().required(TEMPEST_1).stats(stats -> stats.voidicDamage(1)).build();
	public final TurmoilSkill DEFT_STRIKES_2 = builder("deft_strikes_2").noDescription().required(TEMPEST_2).stats(stats -> stats.cooldown(1)).build();
	public final TurmoilSkill GREATER_CLEAVE = builder("greater_cleave").required(TEMPEST_2).build();
	public final TurmoilSkill TEMPEST_3 = builder("tempest_3").noDescription().required(TEMPEST_2).stats(stats -> stats.voidicDamage(1)).build();
	public final TurmoilSkill DEFT_STRIKES_3 = builder("deft_strikes_3").noDescription().required(TEMPEST_3).stats(stats -> stats.voidicDamage(1)).build();
	public final TurmoilSkill WHIRLWIND = builder("whirlwind").required(TEMPEST_3).build();
	public final TurmoilSkill TEMPEST_4 = builder("tempest_4").noDescription().required(TEMPEST_3).stats(stats -> stats.voidicDamage(2)).build();
	public final TurmoilSkill DEFT_STRIKES_4 = builder("deft_strikes_4").noDescription().required(TEMPEST_4).stats(stats -> stats.cooldown(2)).build();
	public final TurmoilSkill TWO_WEAPON_FIGHTING = builder("two_weapon_fighting").required(TEMPEST_4).build();
	public final TurmoilSkill TEMPEST_5 = builder("tempest_5").noDescription().required(TEMPEST_4).stats(stats -> stats.voidicDamage(5)).build();
	public final TurmoilSkill DEFT_STRIKES_5 = builder("deft_strikes_5").noDescription().required(TEMPEST_5).stats(stats -> stats.cooldown(5)).build();
	public final TurmoilSkill WHIRLING_BLADES = builder("whirling_blades").required(TEMPEST_5).build();

	public final TurmoilSkill CHAOS_BLADE_1 = builder("chaos_blade_1").required(CORE).stats(stats -> stats.voidicDamage(1)).build();
	public final TurmoilSkill EMPOWER_ATTACK_BLEED = builder("empower_attack_bleed").required(CHAOS_BLADE_1).build();
	public final TurmoilSkill SENSE_WEAKNESS = builder("sense_weakness").required(CHAOS_BLADE_1).build();
	public final TurmoilSkill CHAOS_BLADE_2 = builder("chaos_blade_2").noDescription().required(CHAOS_BLADE_1).stats(stats -> stats.voidicDamage(1)).build();
	public final TurmoilSkill REJUVENATE = builder("rejuvenate").required(CHAOS_BLADE_2).build();
	public final TurmoilSkill EMPOWER_ATTACK_SLASH = builder("empower_attack_slash").required(CHAOS_BLADE_2).build();
	public final TurmoilSkill CHAOS_BLADE_3 = builder("chaos_blade_3").noDescription().required(CHAOS_BLADE_2).stats(stats -> stats.voidicDamage(1)).build();
	public final TurmoilSkill OVERWHELMING_FORCE = builder("overwhelming_force").required(CHAOS_BLADE_3).build();
	public final TurmoilSkill SHARP_EDGE = builder("sharp_edge").required(CHAOS_BLADE_3).build();
	public final TurmoilSkill CHAOS_BLADE_4 = builder("chaos_blade_4").noDescription().required(CHAOS_BLADE_3).stats(stats -> stats.voidicDamage(2)).build();
	public final TurmoilSkill BLOODBATH = builder("bloodbath").required(CHAOS_BLADE_4).build();
	public final TurmoilSkill CHAOS_BLADE_5 = builder("chaos_blade_5").noDescription().required(CHAOS_BLADE_4).stats(stats -> stats.voidicDamage(5)).build();
	public final TurmoilSkill PRIMAL_RAGE = builder("primal_rage").required(CHAOS_BLADE_5).build();

	@Override
	public String location() {
		return "melee";
	}
}
