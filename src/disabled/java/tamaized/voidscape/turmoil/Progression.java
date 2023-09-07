package tamaized.voidscape.turmoil;

public enum Progression {

	None, Started, EnteredVoid, MidTutorial, PostTutorial, CorruptPhantom, Psychosis, PostPsychosis, CorruptPawn;

	private static final Progression[] VALUES = values();

	public static Progression get(int ordinal) {
		if (ordinal < 0 | ordinal >= VALUES.length)
			return None;
		else
			return VALUES[ordinal];
	}

}
