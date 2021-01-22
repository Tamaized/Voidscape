package tamaized.voidscape.turmoil.skills;

public class HealerSkills implements ITurmoilSkills {

	public final TurmoilSkill CORE = builder("core").core().build();

	public final TurmoilSkill VOIDIC_BOND = builder("voidic_bond").required(CORE).stats(stats -> stats.spellpower(5).rechargeRate(1)).build();
	public final TurmoilSkill SPELLPOWER_1 = builder("spellpower_1").required(VOIDIC_BOND).stats(stats -> stats.spellpower(5)).build();
	public final TurmoilSkill SPELLPOWER_2 = builder("spellpower_2").required(SPELLPOWER_1).stats(stats -> stats.spellpower(5)).build();
	public final TurmoilSkill SPELLPOWER_3 = builder("spellpower_3").required(SPELLPOWER_2).stats(stats -> stats.spellpower(5)).build();
	public final TurmoilSkill SPELLPOWER_4 = builder("spellpower_4").required(SPELLPOWER_3).stats(stats -> stats.spellpower(10)).build();
	public final TurmoilSkill SPELLPOWER_5 = builder("spellpower_5").required(SPELLPOWER_4).stats(stats -> stats.spellpower(20)).build();
	public final TurmoilSkill BONDING_1 = builder("bonding_1").required(VOIDIC_BOND).stats(stats -> stats.rechargeRate(1)).build();
	public final TurmoilSkill BONDING_2 = builder("bonding_2").required(BONDING_1).stats(stats -> stats.rechargeRate(1)).build();
	public final TurmoilSkill BONDING_3 = builder("bonding_3").required(BONDING_2).stats(stats -> stats.rechargeRate(1)).build();
	public final TurmoilSkill BONDING_4 = builder("bonding_4").required(BONDING_3).stats(stats -> stats.rechargeRate(2)).build();
	public final TurmoilSkill BONDING_5 = builder("bonding_5").required(BONDING_4).stats(stats -> stats.rechargeRate(4)).build();
	public final TurmoilSkill SPELL_DEX_1 = builder("spell_dex_1").required(VOIDIC_BOND).stats(stats -> stats.cooldown(5)).build();
	public final TurmoilSkill SPELL_DEX_2 = builder("spell_dex_2").required(SPELL_DEX_1).stats(stats -> stats.cooldown(5)).build();
	public final TurmoilSkill SPELL_DEX_3 = builder("spell_dex_3").required(SPELL_DEX_2).stats(stats -> stats.cooldown(5)).build();
	public final TurmoilSkill SPELL_DEX_4 = builder("spell_dex_4").required(SPELL_DEX_3).stats(stats -> stats.cooldown(5)).build();
	public final TurmoilSkill SPELL_DEX_5 = builder("spell_dex_5").required(SPELL_DEX_4).stats(stats -> stats.cooldown(10)).build();
	public final TurmoilSkill ASTUTE_UNDERSTANDING_1 = builder("astute_understanding_1").required(VOIDIC_BOND).stats(stats -> stats.cost(1)).build();
	public final TurmoilSkill ASTUTE_UNDERSTANDING_2 = builder("astute_understanding_2").required(ASTUTE_UNDERSTANDING_1).stats(stats -> stats.cost(1)).build();
	public final TurmoilSkill ASTUTE_UNDERSTANDING_3 = builder("astute_understanding_3").required(ASTUTE_UNDERSTANDING_2).stats(stats -> stats.cost(1)).build();
	public final TurmoilSkill ASTUTE_UNDERSTANDING_4 = builder("astute_understanding_4").required(ASTUTE_UNDERSTANDING_3).stats(stats -> stats.cost(2)).build();
	public final TurmoilSkill ASTUTE_UNDERSTANDING_5 = builder("astute_understanding_5").required(ASTUTE_UNDERSTANDING_4).stats(stats -> stats.cost(5)).build();

	public final TurmoilSkill VOIDIC_CLERIC_1 = builder("voidic_cleric_1").required(CORE).stats(stats -> stats.voidicDamageReductionPercentage(1).spellpower(1)).build();
	public final TurmoilSkill HEALING_BOLT = builder("healing_bolt").required(VOIDIC_CLERIC_1).build();
	public final TurmoilSkill HEALING_BLAST = builder("healing_blast").required(VOIDIC_CLERIC_1).build();
	public final TurmoilSkill VOIDIC_CLERIC_2 = builder("voidic_cleric_2").required(VOIDIC_CLERIC_1).stats(stats -> stats.voidicDamageReductionPercentage(1).spellpower(1)).build();
	public final TurmoilSkill GREATER_HEALING_BOLT = builder("greater_healing_bolt").required(VOIDIC_CLERIC_2).build();
	public final TurmoilSkill GREATER_HEALING_BLAST = builder("greater_healing_blast").required(VOIDIC_CLERIC_2).build();
	public final TurmoilSkill VOIDIC_CLERIC_3 = builder("voidic_cleric_3").required(VOIDIC_CLERIC_2).stats(stats -> stats.voidicDamageReductionPercentage(1).spellpower(1)).build();
	public final TurmoilSkill MAJOR_HEALING_BOLT = builder("major_healing_bolt").required(VOIDIC_CLERIC_3).build();
	public final TurmoilSkill MAJOR_HEALING_BLAST = builder("major_healing_blast").required(VOIDIC_CLERIC_3).build();
	public final TurmoilSkill VOIDIC_CLERIC_4 = builder("voidic_cleric_4").required(VOIDIC_CLERIC_3).stats(stats -> stats.voidicDamageReductionPercentage(2).spellpower(2)).build();
	public final TurmoilSkill ABSOLUTE_HEALING_BOLT = builder("absolute_healing_bolt").required(VOIDIC_CLERIC_4).build();
	public final TurmoilSkill ABSOLUTE_HEALING_BLAST = builder("absolute_healing_blast").required(VOIDIC_CLERIC_4).build();
	public final TurmoilSkill VOIDIC_CLERIC_5 = builder("voidic_cleric_5").required(VOIDIC_CLERIC_4).stats(stats -> stats.voidicDamageReductionPercentage(5).spellpower(5)).build();
	public final TurmoilSkill STALWART_SHIELD = builder("stalwart_shield").required(VOIDIC_CLERIC_5).build();

	public final TurmoilSkill MAD_PRIEST_1 = builder("mad_priest_1").required(CORE).stats(stats -> stats.spellpower(1)).build();
	public final TurmoilSkill MEND = builder("mend").required(MAD_PRIEST_1).build();
	public final TurmoilSkill MIND_WARP = builder("mind_warp").required(MAD_PRIEST_1).build();
	public final TurmoilSkill MAD_PRIEST_2 = builder("mad_priest_2").required(MAD_PRIEST_1).stats(stats -> stats.spellpower(1)).build();
	public final TurmoilSkill EQUILIZE = builder("equilize").required(MAD_PRIEST_2).build();
	public final TurmoilSkill UNCARING_MADNESS = builder("uncaring_madness").required(MAD_PRIEST_2).build();
	public final TurmoilSkill MAD_PRIEST_3 = builder("mad_priest_3").required(MAD_PRIEST_2).stats(stats -> stats.spellpower(1)).build();
	public final TurmoilSkill EMERGENCY_TACTICS = builder("emergency_tactics").required(MAD_PRIEST_3).build();
	public final TurmoilSkill GAZE = builder("gaze").required(MAD_PRIEST_3).build();
	public final TurmoilSkill MAD_PRIEST_4 = builder("mad_priest_4").required(MAD_PRIEST_3).stats(stats -> stats.spellpower(2)).build();
	public final TurmoilSkill MIND_LINK_POS = builder("mind_link_pos").required(MAD_PRIEST_4).build();
	public final TurmoilSkill MAD_PRIEST_5 = builder("mad_priest_5").required(MAD_PRIEST_4).stats(stats -> stats.spellpower(5)).build();
	public final TurmoilSkill MIND_LINK_NEG = builder("mind_link_neg").required(MAD_PRIEST_5).build();

	public final TurmoilSkill VOIDS_FAVOR_1 = builder("voids_favor_1").required(CORE).stats(stats -> stats.spellpower(1)).build();
	public final TurmoilSkill HEALING_AURA = builder("healing_aura").required(VOIDS_FAVOR_1).build();
	public final TurmoilSkill EMPOWER_SWORD_OSMOSIS = builder("empower_sword_osmosis").required(VOIDS_FAVOR_1).build();
	public final TurmoilSkill VOIDS_FAVOR_2 = builder("voids_favor_2").required(VOIDS_FAVOR_1).stats(stats -> stats.spellpower(1)).build();
	public final TurmoilSkill HEALING_BURST = builder("healing_burst").required(VOIDS_FAVOR_2).build();
	public final TurmoilSkill ANTICIPATION = builder("anticipation").required(VOIDS_FAVOR_2).build();
	public final TurmoilSkill VOIDS_FAVOR_3 = builder("voids_favor_3").required(VOIDS_FAVOR_2).stats(stats -> stats.spellpower(1)).build();
	public final TurmoilSkill HEALING_RAY = builder("healing_ray").required(VOIDS_FAVOR_3).build();
	public final TurmoilSkill EMPOWER_SWORD_2X = builder("empower_sword_2x").required(VOIDS_FAVOR_3).build();
	public final TurmoilSkill VOIDS_FAVOR_4 = builder("voids_favor_4").required(VOIDS_FAVOR_3).stats(stats -> stats.spellpower(1)).build();
	public final TurmoilSkill VOIDIC_PROTECTION = builder("voidic_protection").required(VOIDS_FAVOR_4).build();
	public final TurmoilSkill EMPOWER_SWORD_GREATER_OSMOSIS = builder("empower_sword_greater_osmosis").required(VOIDS_FAVOR_4).build();
	public final TurmoilSkill VOIDS_FAVOR_5 = builder("voids_favor_5").required(VOIDS_FAVOR_4).stats(stats -> stats.spellpower(2)).build();
	public final TurmoilSkill DETERMINATION = builder("determination").required(VOIDS_FAVOR_5).build();

	@Override
	public String location() {
		return "healer";
	}
}
