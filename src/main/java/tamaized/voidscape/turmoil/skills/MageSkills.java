package tamaized.voidscape.turmoil.skills;

import tamaized.voidscape.turmoil.abilities.MageAbilities;

public class MageSkills implements ITurmoilSkills {

	public final TurmoilSkill CORE = builder("core").noDescription().core().stats(stats -> stats.threat(1)).abilities(MageAbilities.BOLT).build();

	public final TurmoilSkill VOIDIC_BOND = generic("voidic_bond").required(CORE).stats(stats -> stats.spellpower(5)).build();
	public final TurmoilSkill SPELLPOWER_1 = generic("spellpower_1").description("spellpower").required(VOIDIC_BOND).stats(stats -> stats.spellpower(5)).build();
	public final TurmoilSkill SPELLPOWER_2 = generic("spellpower_2").description("spellpower").required(SPELLPOWER_1).stats(stats -> stats.spellpower(5)).build();
	public final TurmoilSkill SPELLPOWER_3 = generic("spellpower_3").description("spellpower").required(SPELLPOWER_2).stats(stats -> stats.spellpower(5)).build();
	public final TurmoilSkill SPELLPOWER_4 = generic("spellpower_4").description("spellpower").required(SPELLPOWER_3).stats(stats -> stats.spellpower(10)).build();
	public final TurmoilSkill SPELLPOWER_5 = generic("spellpower_5").description("spellpower").required(SPELLPOWER_4).stats(stats -> stats.spellpower(20)).build();
	public final TurmoilSkill BONDING_1 = generic("voidic_bonding_1").description("voidic_bonding").required(VOIDIC_BOND).stats(stats -> stats.rechargeRate(1)).build();
	public final TurmoilSkill BONDING_2 = generic("voidic_bonding_2").description("voidic_bonding").required(BONDING_1).stats(stats -> stats.rechargeRate(1)).build();
	public final TurmoilSkill BONDING_3 = generic("voidic_bonding_3").description("voidic_bonding").required(BONDING_2).stats(stats -> stats.rechargeRate(1)).build();
	public final TurmoilSkill BONDING_4 = generic("voidic_bonding_4").description("voidic_bonding").required(BONDING_3).stats(stats -> stats.rechargeRate(2)).build();
	public final TurmoilSkill BONDING_5 = generic("voidic_bonding_5").description("voidic_bonding").required(BONDING_4).stats(stats -> stats.rechargeRate(5)).build();
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

	public final TurmoilSkill VOIDMANCY_1 = builder("voidmancy_1").noDescription().required(CORE).stats(stats -> stats.spellpower(2).rechargeRate(1).spellCrit(1)).build();
	public final TurmoilSkill HOMINGBOLTS = builder("homingbolts").noDescription().required(VOIDMANCY_1).stats(stats -> stats.spellpower(5)).abilities(MageAbilities.HOMING_BOLTS).build();
	public final TurmoilSkill VOIDICAURA = builder("voidicaura").noDescription().required(VOIDMANCY_1).abilities(MageAbilities.AURA).build();
	public final TurmoilSkill VOIDMANCY_2 = builder("voidmancy_2").required(VOIDMANCY_1).stats(stats -> stats.spellpower(2).cooldown(1).spellCrit(1)).build();
	public final TurmoilSkill PENETRATINGBOLTS = builder("penetratingbolts").required(VOIDMANCY_2).stats(stats -> stats.spellpower(5)).build();
	public final TurmoilSkill EMPOWERBOLT_2X = builder("empowerbolt_2x").required(VOIDMANCY_2).build();
	public final TurmoilSkill VOIDICBURST = builder("voidicburst").required(VOIDMANCY_2, VOIDICAURA).build();
	public final TurmoilSkill VOIDMANCY_3 = builder("voidmancy_3").required(VOIDMANCY_2).stats(stats -> stats.spellpower(2).rechargeRate(1).spellCrit(1)).build();
	public final TurmoilSkill EXPLOSIVEBOLTS = builder("explosivebolts").required(VOIDMANCY_3).stats(stats -> stats.spellpower(5)).build();
	public final TurmoilSkill VOIDICHEALING = builder("voidichealing").required(VOIDMANCY_3, VOIDICBURST).build();
	public final TurmoilSkill VOIDICTELEPORT = builder("voidicteleport").required(VOIDMANCY_3).build();
	public final TurmoilSkill EMPOWERBOLT_VULN = builder("empowerbolt_vuln").required(VOIDMANCY_3).build();
	public final TurmoilSkill VOIDMANCY_4 = builder("voidmancy_4").required(VOIDMANCY_3).stats(stats -> stats.spellpower(2).cooldown(1).spellCrit(1)).build();
	public final TurmoilSkill NULLIFIEDBOLTS = builder("nullifiedbolts").required(VOIDMANCY_4).build();
	public final TurmoilSkill NULLIFIEDCONVERSION = builder("nullifiedconversion").required(NULLIFIEDBOLTS).build();
	public final TurmoilSkill NULLIFIEDREJUVINATION = builder("nullifiedrejuvination").required(NULLIFIEDBOLTS).build();
	public final TurmoilSkill PUREVOIDICHEALING = builder("purevoidichealing").required(VOIDMANCY_4, VOIDICHEALING).build();
	public final TurmoilSkill VOIDMANCY_5 = builder("voidmancy_5").required(VOIDMANCY_4).stats(stats -> stats.spellpower(12).rechargeRate(3).cooldown(3).spellCrit(6)).build();
	public final TurmoilSkill VOIDMANCERSTANCE = builder("voidmancerstance").required(VOIDMANCY_5, NULLIFIEDBOLTS).build();
	public final TurmoilSkill NULLSHIELD = builder("nullshield").required(VOIDMANCERSTANCE).build();
	public final TurmoilSkill NULLVULNERABILITY = builder("nullvulnerability").required(VOIDMANCERSTANCE).build();

	public final TurmoilSkill VOIDIC_ARCHER_1 = builder("voidic_archer_1").required(CORE).stats(stats -> stats.spellpower(1)).build();
	public final TurmoilSkill ARROWIMBUE_SPELLLIKE = builder("arrowimbue_spelllike").noDescription().required(VOIDIC_ARCHER_1).abilities(MageAbilities.ARROW_IMBUE_SPELLLIKE).build();
	public final TurmoilSkill FLAMESHOT = builder("flameshot").noDescription().required(VOIDIC_ARCHER_1).abilities(MageAbilities.FLAME_SHOT).build();
	public final TurmoilSkill VOIDIC_ARCHER_2 = builder("voidic_archer_2").required(VOIDIC_ARCHER_1).stats(stats -> stats.spellpower(1)).build();
	public final TurmoilSkill ARROWIMBUE_FOCUS = builder("arrowimbue_focus").required(VOIDIC_ARCHER_2).build();
	public final TurmoilSkill FORCESHOT = builder("forceshot").required(VOIDIC_ARCHER_2).build();
	public final TurmoilSkill VOIDIC_ARCHER_3 = builder("voidic_archer_3").required(VOIDIC_ARCHER_2).stats(stats -> stats.spellpower(1)).build();
	public final TurmoilSkill ARROWIMBUE_PENETRATING = builder("arrowimbue_penetrating").required(VOIDIC_ARCHER_3).build();
	public final TurmoilSkill VOIDIC_SHOT = builder("voidic_shot").required(VOIDIC_ARCHER_3).build();
	public final TurmoilSkill VOIDIC_ARCHER_4 = builder("voidic_archer_4").required(VOIDIC_ARCHER_3).stats(stats -> stats.spellpower(1)).build();
	public final TurmoilSkill ARROWIMBUE_DESTRUCTIVE = builder("arrowimbue_destructive").required(VOIDIC_ARCHER_4).build();
	public final TurmoilSkill SLAYINGSHOT = builder("slayingshot").required(VOIDIC_ARCHER_4).build();
	public final TurmoilSkill VOIDIC_ARCHER_5 = builder("voidic_archer_5").required(VOIDIC_ARCHER_4).stats(stats -> stats.spellpower(6).rechargeRate(5)).build();
	public final TurmoilSkill MANYSHOT = builder("manyshot").required(VOIDIC_ARCHER_5).build();
	public final TurmoilSkill NULLSHOT = builder("nullshot").required(VOIDIC_ARCHER_5).build();

	public final TurmoilSkill INSANE_MAGE_1 = builder("insane_mage_1").required(CORE).stats(stats -> stats.spellpower(2).cost(1)).build();
	public final TurmoilSkill ECHO = builder("echo").noDescription().required(INSANE_MAGE_1).abilities(MageAbilities.ECHO).build();
	public final TurmoilSkill TRAUMATIZE = builder("traumatize").noDescription().required(INSANE_MAGE_1).abilities(MageAbilities.TRAUMATIZE).build();
	public final TurmoilSkill INSANE_MAGE_2 = builder("insane_mage_2").noDescription().required(INSANE_MAGE_1).stats(stats -> stats.spellpower(2).cost(1)).build();
	public final TurmoilSkill CONVERT = builder("convert").required(INSANE_MAGE_2).build();
	public final TurmoilSkill DEMORALIZE = builder("demoralize").required(INSANE_MAGE_2).build();
	public final TurmoilSkill INSANE_MAGE_3 = builder("insane_mage_3").noDescription().required(INSANE_MAGE_2).stats(stats -> stats.spellpower(2).cost(1)).build();
	public final TurmoilSkill EQUILIBRIUM = builder("equilibrium").required(INSANE_MAGE_3).build();
	public final TurmoilSkill TERRORIZE = builder("terrorize").required(INSANE_MAGE_3).build();
	public final TurmoilSkill INSANE_MAGE_4 = builder("insane_mage_4").noDescription().required(INSANE_MAGE_3).stats(stats -> stats.spellpower(2).cost(1)).build();
	public final TurmoilSkill PSYCHOSIS = builder("psychosis").required(INSANE_MAGE_4).build();
	public final TurmoilSkill HYSTERIA = builder("hysteria").required(INSANE_MAGE_4).build();
	public final TurmoilSkill INSANE_MAGE_5 = builder("insane_mage_5").noDescription().required(INSANE_MAGE_4).stats(stats -> stats.spellpower(12).cost(6)).build();
	public final TurmoilSkill MANIA = builder("mania").required(INSANE_MAGE_5).build();
	public final TurmoilSkill DELIRIUM = builder("delirium").required(INSANE_MAGE_5).build();

	@Override
	public String location() {
		return "mage";
	}
}
