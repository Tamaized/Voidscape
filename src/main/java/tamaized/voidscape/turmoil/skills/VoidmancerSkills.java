package tamaized.voidscape.turmoil.skills;

public class VoidmancerSkills implements ITurmoilSkills {

	public final TurmoilSkill CORE = make("core", 0, 1, true);

	@Override
	public String location() {
		return "voidmancer";
	}
}
