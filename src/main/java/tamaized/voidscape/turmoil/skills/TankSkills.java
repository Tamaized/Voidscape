package tamaized.voidscape.turmoil.skills;

public class TankSkills implements ITurmoilSkills {

	public final TurmoilSkill CORE = builder("core").core().disabled().build();

	@Override
	public String location() {
		return "tank";
	}
}
