package tamaized.voidscape.turmoil.skills;

public class MeleeSkills implements ITurmoilSkills {

	public final TurmoilSkill CORE = builder("core").core().disabled().build();

	@Override
	public String location() {
		return "melee";
	}
}
