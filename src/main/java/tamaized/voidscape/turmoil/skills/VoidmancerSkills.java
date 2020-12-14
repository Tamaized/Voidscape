package tamaized.voidscape.turmoil.skills;

public class VoidmancerSkills implements ITurmoilSkills {

	public final TurmoilSkill CORE = builder("core").core().build();

	public final TurmoilSkill VOIDIC_BOND = builder("voidic_bond").required(CORE).build();

	public final TurmoilSkill SPELLPOWER_1 = builder("spellpower_1").required(VOIDIC_BOND).build();
	public final TurmoilSkill SPELLPOWER_2 = builder("spellpower_2").required(SPELLPOWER_1).build();
	public final TurmoilSkill SPELLPOWER_3 = builder("spellpower_3").required(SPELLPOWER_2).build();
	public final TurmoilSkill SPELLPOWER_4 = builder("spellpower_4").required(SPELLPOWER_3).build();
	public final TurmoilSkill SPELLPOWER_5 = builder("spellpower_5").required(SPELLPOWER_4).build();

	public final TurmoilSkill BONDING_1 = builder("bonding_1").required(VOIDIC_BOND).build();
	public final TurmoilSkill BONDING_2 = builder("bonding_2").required(BONDING_1).build();
	public final TurmoilSkill BONDING_3 = builder("bonding_3").required(BONDING_2).build();
	public final TurmoilSkill BONDING_4 = builder("bonding_4").required(BONDING_3).build();
	public final TurmoilSkill BONDING_5 = builder("bonding_5").required(BONDING_4).build();

	public final TurmoilSkill SPELL_DEX_1 = builder("spell_dex_1").required(VOIDIC_BOND).build();
	public final TurmoilSkill SPELL_DEX_2 = builder("spell_dex_2").required(SPELL_DEX_1).build();
	public final TurmoilSkill SPELL_DEX_3 = builder("spell_dex_3").required(SPELL_DEX_2).build();
	public final TurmoilSkill SPELL_DEX_4 = builder("spell_dex_4").required(SPELL_DEX_3).build();
	public final TurmoilSkill SPELL_DEX_5 = builder("spell_dex_5").required(SPELL_DEX_4).build();

	public final TurmoilSkill ASTUTE_UNDERSTANDING_1 = builder("astute_understanding_1").required(VOIDIC_BOND).build();
	public final TurmoilSkill ASTUTE_UNDERSTANDING_2 = builder("astute_understanding_2").required(ASTUTE_UNDERSTANDING_1).build();
	public final TurmoilSkill ASTUTE_UNDERSTANDING_3 = builder("astute_understanding_3").required(ASTUTE_UNDERSTANDING_2).build();
	public final TurmoilSkill ASTUTE_UNDERSTANDING_4 = builder("astute_understanding_4").required(ASTUTE_UNDERSTANDING_3).build();
	public final TurmoilSkill ASTUTE_UNDERSTANDING_5 = builder("astute_understanding_5").required(ASTUTE_UNDERSTANDING_4).build();

	public final TurmoilSkill VOIDMANCY_1 = builder("voidmancy_1").required(CORE).build();

	public final TurmoilSkill VOIDIC_ARCHER_1 = builder("voidic_archer_1").required(CORE).build();

	public final TurmoilSkill INSANE_MAGE_1 = builder("insane_mage_1").required(CORE).build();

	@Override
	public String location() {
		return "voidmancer";
	}
}
