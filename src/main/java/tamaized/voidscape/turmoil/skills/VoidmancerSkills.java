package tamaized.voidscape.turmoil.skills;

public class VoidmancerSkills implements ITurmoilSkills {

	public final TurmoilSkill CORE = builder("core").core().build();
	public final TurmoilSkill TEST = builder("test").spent(1).required(CORE).build();
	public final TurmoilSkill TEST2A = builder("test2A").spent(2).required(TEST).build();
	public final TurmoilSkill TEST2B = builder("test2B").spent(2).required(TEST).build();

	@Override
	public String location() {
		return "voidmancer";
	}
}
