package tamaized.voidscape.turmoil.skills;

import tamaized.voidscape.turmoil.abilities.HealerAbilities;

public class HealerSkills implements ITurmoilSkills {

	public final TurmoilSkill CORE = builder("core").noDescription().core().stats(stats -> stats.threat(1)).abilities(HealerAbilities.REZ).build();

	public final TurmoilSkill VOIDIC_BOND = generic("voidic_bond").required(CORE).stats(stats -> stats.spellpower(5).rechargeRate(1)).build();
	public final TurmoilSkill SPELLPOWER_1 = generic("spellpower_1").description("spellpower").required(VOIDIC_BOND).stats(stats -> stats.spellpower(5)).build();
	public final TurmoilSkill SPELLPOWER_2 = generic("spellpower_2").description("spellpower").required(SPELLPOWER_1).stats(stats -> stats.spellpower(5)).build();
	public final TurmoilSkill SPELLPOWER_3 = generic("spellpower_3").description("spellpower").required(SPELLPOWER_2).stats(stats -> stats.spellpower(5)).build();
	public final TurmoilSkill SPELLPOWER_4 = generic("spellpower_4").description("spellpower").required(SPELLPOWER_3).stats(stats -> stats.spellpower(10)).build();
	public final TurmoilSkill SPELLPOWER_5 = generic("spellpower_5").description("spellpower").required(SPELLPOWER_4).stats(stats -> stats.spellpower(20)).build();
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

	public final TurmoilSkill VOIDIC_CLERIC_1 = builder("voidic_cleric_1").noDescription().required(CORE).stats(stats -> stats.voidicDamageReductionPercentage(1).spellpower(1)).build();
	public final TurmoilSkill HEALING_BOLT = builder("healing_bolt").abilities(HealerAbilities.HEALING_BOLT).required(VOIDIC_CLERIC_1).build();
	public final TurmoilSkill HEALING_BLAST = builder("healing_blast").abilities(HealerAbilities.HEALING_BURST).required(VOIDIC_CLERIC_1).build();
	public final TurmoilSkill VOIDIC_CLERIC_2 = builder("voidic_cleric_2").noDescription().required(VOIDIC_CLERIC_1).stats(stats -> stats.voidicDamageReductionPercentage(1).spellpower(1)).build();
	public final TurmoilSkill GREATER_HEALING_BOLT = builder("greater_healing_bolt").required(VOIDIC_CLERIC_2).build();
	public final TurmoilSkill GREATER_HEALING_BLAST = builder("greater_healing_blast").required(VOIDIC_CLERIC_2).build();
	public final TurmoilSkill VOIDIC_CLERIC_3 = builder("voidic_cleric_3").noDescription().required(VOIDIC_CLERIC_2).stats(stats -> stats.voidicDamageReductionPercentage(1).spellpower(1)).build();
	public final TurmoilSkill MAJOR_HEALING_BOLT = builder("major_healing_bolt").required(VOIDIC_CLERIC_3).build();
	public final TurmoilSkill MAJOR_HEALING_BLAST = builder("major_healing_blast").required(VOIDIC_CLERIC_3).build();
	public final TurmoilSkill VOIDIC_CLERIC_4 = builder("voidic_cleric_4").noDescription().required(VOIDIC_CLERIC_3).stats(stats -> stats.voidicDamageReductionPercentage(2).spellpower(2)).build();
	public final TurmoilSkill ABSOLUTE_HEALING_BOLT = builder("absolute_healing_bolt").required(VOIDIC_CLERIC_4).build();
	public final TurmoilSkill ABSOLUTE_HEALING_BLAST = builder("absolute_healing_blast").required(VOIDIC_CLERIC_4).build();
	public final TurmoilSkill VOIDIC_CLERIC_5 = builder("voidic_cleric_5").noDescription().required(VOIDIC_CLERIC_4).stats(stats -> stats.voidicDamageReductionPercentage(5).spellpower(5)).build();
	public final TurmoilSkill STALWART_SHIELD = builder("stalwart_shield").required(VOIDIC_CLERIC_5).build();

	public final TurmoilSkill MAD_PRIEST_1 = builder("mad_priest_1").required(CORE).stats(stats -> stats.spellpower(1)).build();
	public final TurmoilSkill MEND = builder("mend").abilities(HealerAbilities.MEND).required(MAD_PRIEST_1).build();
	public final TurmoilSkill MIND_WARP = builder("mind_warp").required(MAD_PRIEST_1).build();
	public final TurmoilSkill MAD_PRIEST_2 = builder("mad_priest_2").noDescription().required(MAD_PRIEST_1).stats(stats -> stats.spellpower(1)).build();
	public final TurmoilSkill EQUILIZE = builder("equilize").required(MAD_PRIEST_2).build();
	public final TurmoilSkill UNCARING_MADNESS = builder("uncaring_madness").required(MAD_PRIEST_2).build();
	public final TurmoilSkill MAD_PRIEST_3 = builder("mad_priest_3").noDescription().required(MAD_PRIEST_2).stats(stats -> stats.spellpower(1)).build();
	public final TurmoilSkill EMERGENCY_TACTICS = builder("emergency_tactics").required(MAD_PRIEST_3).build();
	public final TurmoilSkill GAZE = builder("gaze").required(MAD_PRIEST_3).build();
	public final TurmoilSkill MAD_PRIEST_4 = builder("mad_priest_4").noDescription().required(MAD_PRIEST_3).stats(stats -> stats.spellpower(2)).build();
	public final TurmoilSkill MIND_LINK_POS = builder("mind_link_pos").required(MAD_PRIEST_4).build();
	public final TurmoilSkill MAD_PRIEST_5 = builder("mad_priest_5").noDescription().required(MAD_PRIEST_4).stats(stats -> stats.spellpower(5)).build();
	public final TurmoilSkill MIND_LINK_NEG = builder("mind_link_neg").required(MAD_PRIEST_5).build();

	public final TurmoilSkill VOIDS_FAVOR_1 = builder("voids_favor_1").required(CORE).stats(stats -> stats.spellpower(1)).build();
	public final TurmoilSkill HEALING_AURA = builder("healing_aura").required(VOIDS_FAVOR_1).build();
	public final TurmoilSkill EMPOWER_SWORD_OSMOSIS = builder("empower_sword_osmosis").required(VOIDS_FAVOR_1).build();
	public final TurmoilSkill VOIDS_FAVOR_2 = builder("voids_favor_2").noDescription().required(VOIDS_FAVOR_1).stats(stats -> stats.spellpower(1)).build();
	public final TurmoilSkill HEALING_BURST = builder("healing_burst").required(VOIDS_FAVOR_2).build();
	public final TurmoilSkill ANTICIPATION = builder("anticipation").required(VOIDS_FAVOR_2).build();
	public final TurmoilSkill VOIDS_FAVOR_3 = builder("voids_favor_3").noDescription().required(VOIDS_FAVOR_2).stats(stats -> stats.spellpower(1)).build();
	public final TurmoilSkill HEALING_RAY = builder("healing_ray").required(VOIDS_FAVOR_3).build();
	public final TurmoilSkill EMPOWER_SWORD_2X = builder("empower_sword_2x").required(VOIDS_FAVOR_3).build();
	public final TurmoilSkill VOIDS_FAVOR_4 = builder("voids_favor_4").noDescription().required(VOIDS_FAVOR_3).stats(stats -> stats.spellpower(1)).build();
	public final TurmoilSkill VOIDIC_PROTECTION = builder("voidic_protection").required(VOIDS_FAVOR_4).build();
	public final TurmoilSkill EMPOWER_SWORD_GREATER_OSMOSIS = builder("empower_sword_greater_osmosis").required(VOIDS_FAVOR_4).build();
	public final TurmoilSkill VOIDS_FAVOR_5 = builder("voids_favor_5").noDescription().required(VOIDS_FAVOR_4).stats(stats -> stats.spellpower(2)).build();
	public final TurmoilSkill DETERMINATION = builder("determination").required(VOIDS_FAVOR_5).build();

	@Override
	public String location() {
		return "healer";
	}
}
