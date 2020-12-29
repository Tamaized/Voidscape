package tamaized.voidscape.turmoil.skills;

public class HealerSkills implements ITurmoilSkills {

	public final TurmoilSkill CORE = builder("core").core().build();

	public final TurmoilSkill VOIDIC_BOND = builder("voidic_bond").required(CORE).build();

	public final TurmoilSkill VOIDIC_CLERIC_1 = builder("voidic_cleric_1").required(CORE).build();
	public final TurmoilSkill VOIDIC_CLERIC_2 = builder("voidic_cleric_2").required(VOIDIC_CLERIC_1).build();
	public final TurmoilSkill VOIDIC_CLERIC_3 = builder("voidic_cleric_3").required(VOIDIC_CLERIC_2).build();
	public final TurmoilSkill VOIDIC_CLERIC_4 = builder("voidic_cleric_4").required(VOIDIC_CLERIC_3).build();
	public final TurmoilSkill VOIDIC_CLERIC_5 = builder("voidic_cleric_5").required(VOIDIC_CLERIC_4).build();

	public final TurmoilSkill MAD_PRIEST_1 = builder("mad_priest_1").required(CORE).build();
	public final TurmoilSkill MAD_PRIEST_2 = builder("mad_priest_2").required(MAD_PRIEST_1).build();
	public final TurmoilSkill MAD_PRIEST_3 = builder("mad_priest_3").required(MAD_PRIEST_2).build();
	public final TurmoilSkill MAD_PRIEST_4 = builder("mad_priest_4").required(MAD_PRIEST_3).build();
	public final TurmoilSkill MAD_PRIEST_5 = builder("mad_priest_5").required(MAD_PRIEST_4).build();

	public final TurmoilSkill VOIDS_FAVOR_1 = builder("voids_favor_1").required(CORE).build();
	public final TurmoilSkill VOIDS_FAVOR_2 = builder("voids_favor_2").required(VOIDS_FAVOR_1).build();
	public final TurmoilSkill VOIDS_FAVOR_3 = builder("voids_favor_3").required(VOIDS_FAVOR_2).build();
	public final TurmoilSkill VOIDS_FAVOR_4 = builder("voids_favor_4").required(VOIDS_FAVOR_3).build();
	public final TurmoilSkill VOIDS_FAVOR_5 = builder("voids_favor_5").required(VOIDS_FAVOR_4).build();

	@Override
	public String location() {
		return "healer";
	}
}
