package tamaized.voidscape.turmoil.skills;

import tamaized.voidscape.turmoil.abilities.TankAbilities;

public class TankSkills implements ITurmoilSkills {

	public final TurmoilSkill CORE = builder("core").noDescription().core().stats(stats -> stats.threat(1)).abilities(TankAbilities.TAUNT).build();

	public final TurmoilSkill VOIDIC_BOND = generic("voidic_bond").required(CORE).stats(stats -> stats.voidicDamage(1).rechargeRate(1)).build();
	public final TurmoilSkill VOIDIC_DAMAGE_1 = generic("voidic_damage_1").description("voidic_damage").required(VOIDIC_BOND).stats(stats -> stats.voidicDamage(1)).build();
	public final TurmoilSkill VOIDIC_DAMAGE_2 = generic("voidic_damage_2").description("voidic_damage").required(VOIDIC_DAMAGE_1).stats(stats -> stats.voidicDamage(1)).build();
	public final TurmoilSkill VOIDIC_DAMAGE_3 = generic("voidic_damage_3").description("voidic_damage").required(VOIDIC_DAMAGE_2).stats(stats -> stats.voidicDamage(1)).build();
	public final TurmoilSkill VOIDIC_DAMAGE_4 = generic("voidic_damage_4").description("voidic_damage").required(VOIDIC_DAMAGE_3).stats(stats -> stats.voidicDamage(2)).build();
	public final TurmoilSkill VOIDIC_DAMAGE_5 = generic("voidic_damage_5").description("voidic_damage").required(VOIDIC_DAMAGE_4).stats(stats -> stats.voidicDamage(5)).build();
	public final TurmoilSkill VOIDIC_BONDING_1 = generic("voidic_bonding_1").description("voidic_bonding").required(VOIDIC_BOND).stats(stats -> stats.rechargeRate(1)).build();
	public final TurmoilSkill VOIDIC_BONDING_2 = generic("voidic_bonding_2").description("voidic_bonding").required(VOIDIC_DAMAGE_1).stats(stats -> stats.rechargeRate(1)).build();
	public final TurmoilSkill VOIDIC_BONDING_3 = generic("voidic_bonding_3").description("voidic_bonding").required(VOIDIC_DAMAGE_2).stats(stats -> stats.rechargeRate(1)).build();
	public final TurmoilSkill VOIDIC_BONDING_4 = generic("voidic_bonding_4").description("voidic_bonding").required(VOIDIC_DAMAGE_3).stats(stats -> stats.rechargeRate(2)).build();
	public final TurmoilSkill VOIDIC_BONDING_5 = generic("voidic_bonding_5").description("voidic_bonding").required(VOIDIC_DAMAGE_4).stats(stats -> stats.rechargeRate(5)).build();
	public final TurmoilSkill VOIDIC_HAMP_1 = generic("voidic_hamp_1").description("voidic_hamp").required(VOIDIC_BOND).stats(stats -> stats.healAmp(1)).build();
	public final TurmoilSkill VOIDIC_HAMP_2 = generic("voidic_hamp_2").description("voidic_hamp").required(VOIDIC_DAMAGE_1).stats(stats -> stats.healAmp(1)).build();
	public final TurmoilSkill VOIDIC_HAMP_3 = generic("voidic_hamp_3").description("voidic_hamp").required(VOIDIC_DAMAGE_2).stats(stats -> stats.healAmp(1)).build();
	public final TurmoilSkill VOIDIC_HAMP_4 = generic("voidic_hamp_4").description("voidic_hamp").required(VOIDIC_DAMAGE_3).stats(stats -> stats.healAmp(2)).build();
	public final TurmoilSkill VOIDIC_HAMP_5 = generic("voidic_hamp_5").description("voidic_hamp").required(VOIDIC_DAMAGE_4).stats(stats -> stats.healAmp(5)).build();
	public final TurmoilSkill VOIDIC_DEFENSE_1 = generic("voidic_defense_1").description("voidic_defense").required(VOIDIC_BOND).stats(stats -> stats.voidicDamageReduction(1)).build();
	public final TurmoilSkill VOIDIC_DEFENSE_2 = generic("voidic_defense_2").description("voidic_defense").required(VOIDIC_DAMAGE_1).stats(stats -> stats.voidicDamageReduction(1)).build();
	public final TurmoilSkill VOIDIC_DEFENSE_3 = generic("voidic_defense_3").description("voidic_defense").required(VOIDIC_DAMAGE_2).stats(stats -> stats.voidicDamageReduction(1)).build();
	public final TurmoilSkill VOIDIC_DEFENSE_4 = generic("voidic_defense_4").description("voidic_defense").required(VOIDIC_DAMAGE_3).stats(stats -> stats.voidicDamageReduction(2)).build();
	public final TurmoilSkill VOIDIC_DEFENSE_5 = generic("voidic_defense_5").description("voidic_defense").required(VOIDIC_DAMAGE_4).stats(stats -> stats.voidicDamageReduction(5)).build();

	public final TurmoilSkill VOIDIC_DEFENDER_1 = builder("voidic_defender_1").noDescription().required(CORE).stats(stats -> stats.voidicDamageReductionPercentage(1).hearts(1).threat(1)).build();
	public final TurmoilSkill BULWARK = builder("bulwark").noDescription().required(VOIDIC_DEFENDER_1).abilities(TankAbilities.BULWARK).build();
	public final TurmoilSkill SHOUT = builder("shout").noDescription().required(VOIDIC_DEFENDER_1).abilities(TankAbilities.SHOUT).build();
	public final TurmoilSkill VOIDIC_DEFENDER_2 = builder("voidic_defender_2").noDescription().required(VOIDIC_DEFENDER_1).stats(stats -> stats.voidicDamageReductionPercentage(1).hearts(1).threat(1)).build();
	public final TurmoilSkill STANCE_DEFENSIVE = builder("stance_defensive").required(VOIDIC_DEFENDER_2).build();
	public final TurmoilSkill EMPOWER_STRIKE_THREAT = builder("empower_strike_threat").required(VOIDIC_DEFENDER_2).build();
	public final TurmoilSkill RALLY = builder("rally").required(VOIDIC_DEFENDER_2).build();
	public final TurmoilSkill VOIDIC_DEFENDER_3 = builder("voidic_defender_3").noDescription().required(VOIDIC_DEFENDER_2).stats(stats -> stats.voidicDamageReductionPercentage(1).hearts(1).threat(1)).build();
	public final TurmoilSkill STAND_YOUR_GROUND = builder("stand_your_ground").required(VOIDIC_DEFENDER_3).build();
	public final TurmoilSkill EMPOWER_STRIKE_RETRIBUTION = builder("empower_strike_retribution").required(VOIDIC_DEFENDER_3).build();
	public final TurmoilSkill VOIDIC_DEFENDER_4 = builder("voidic_defender_4").noDescription().required(VOIDIC_DEFENDER_3).stats(stats -> stats.voidicDamageReductionPercentage(2).hearts(2).threat(2)).build();
	public final TurmoilSkill BRACE_FOR_IMPACT = builder("brace_for_impact").required(VOIDIC_DEFENDER_4).build();
	public final TurmoilSkill EMPOWER_STRIKE_SAP = builder("brace_for_sap").required(VOIDIC_DEFENDER_4).build();
	public final TurmoilSkill VOIDIC_DEFENDER_5 = builder("voidic_defender_5").noDescription().required(VOIDIC_DEFENDER_4).stats(stats -> stats.voidicDamageReductionPercentage(5).hearts(5).threat(5)).build();
	public final TurmoilSkill IRON_WILL = builder("iron_will").required(VOIDIC_DEFENDER_5).build();

	public final TurmoilSkill INSANE_BEAST_1 = builder("insane_beast_1").required(CORE).stats(stats -> stats.hearts(1)).build();
	public final TurmoilSkill ADRENALINE = builder("adrenaline").noDescription().required(INSANE_BEAST_1).abilities(TankAbilities.ADRENALINE).build();
	public final TurmoilSkill TUNNEL_VISION = builder("tunnel_vision").noDescription().required(INSANE_BEAST_1).abilities(TankAbilities.TUNNEL_VISION).build();
	public final TurmoilSkill INSANE_BEAST_2 = builder("insane_beast_2").noDescription().required(INSANE_BEAST_1).stats(stats -> stats.hearts(1)).build();
	public final TurmoilSkill RAGE = builder("rage").required(INSANE_BEAST_2).build();
	public final TurmoilSkill EMPOWER_STRIKE_GASH = builder("empower_strike_gash").required(INSANE_BEAST_2).build();
	public final TurmoilSkill INSANE_BEAST_3 = builder("insane_beast_3").noDescription().required(INSANE_BEAST_2).stats(stats -> stats.hearts(1)).build();
	public final TurmoilSkill STANCE_CHAOTIC = builder("stance_chaotic").required(INSANE_BEAST_3).build();
	public final TurmoilSkill EMPOWER_STRIKE_BLOODTHIRST = builder("empower_strike_bloodthirst").required(INSANE_BEAST_3).build();
	public final TurmoilSkill INSANE_BEAST_4 = builder("insane_beast_4").noDescription().required(INSANE_BEAST_3).stats(stats -> stats.hearts(2)).build();
	public final TurmoilSkill FURY = builder("fury").required(INSANE_BEAST_4).build();
	public final TurmoilSkill INSANE_BEAST_5 = builder("insane_beast_5").noDescription().required(INSANE_BEAST_4).stats(stats -> stats.hearts(5).healAmp(10)).build();
	public final TurmoilSkill RIP_AND_TEAR = builder("rip_and_tear").required(INSANE_BEAST_5).build();

	public final TurmoilSkill TACTICIAN_1 = builder("tactician_1").required(CORE).stats(stats -> stats.voidicDamage(1).voidicDamageReduction(1)).build();
	public final TurmoilSkill EMPOWER_SHIELD_2X_NULL = builder("empower_shield_2x_null").noDescription().required(TACTICIAN_1).abilities(TankAbilities.EMPOWER_SHIELD_2X_NULL).build();
	public final TurmoilSkill BACKSTEP = builder("backstep").noDescription().required(TACTICIAN_1).abilities(TankAbilities.BACKSTEP).build();
	public final TurmoilSkill TACTICIAN_2 = builder("tactician_2").noDescription().required(TACTICIAN_1).stats(stats -> stats.voidicDamage(1).voidicDamageReduction(1)).build();
	public final TurmoilSkill STANCE_STALWART = builder("stance_stalwart").required(TACTICIAN_2).build();
	public final TurmoilSkill FORESIGHT = builder("foresight").required(TACTICIAN_2).build();
	public final TurmoilSkill TACTICIAN_3 = builder("tactician_3").noDescription().required(TACTICIAN_2).stats(stats -> stats.voidicDamage(1).voidicDamageReduction(1)).build();
	public final TurmoilSkill STRATEGIZE = builder("strategize").required(TACTICIAN_3).build();
	public final TurmoilSkill EMPOWER_SHIELD_REFLECT = builder("empower_shield_reflect").required(TACTICIAN_3).build();
	public final TurmoilSkill TACTICIAN_4 = builder("tactician_4").noDescription().required(TACTICIAN_3).stats(stats -> stats.voidicDamage(2).voidicDamageReduction(2)).build();
	public final TurmoilSkill PREDICTION = builder("prediction").required(TACTICIAN_4).build();
	public final TurmoilSkill EMPOWER_SHIELD_EMBRACE = builder("empower_shield_embrace").required(TACTICIAN_4).build();
	public final TurmoilSkill TACTICIAN_5 = builder("tactician_5").noDescription().required(TACTICIAN_4).stats(stats -> stats.voidicDamage(5).voidicDamageReduction(5)).build();
	public final TurmoilSkill TACTICAL_ADVANTAGE = builder("tactical_advantage").required(TACTICIAN_5).build();

	@Override
	public String location() {
		return "tank";
	}
}
