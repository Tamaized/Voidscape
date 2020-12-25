package tamaized.voidscape.turmoil;

public enum Progression {

	None, Started, EnteredVoid, MidTutorial, PostTutorial, CorruptPhantom, CorruptPawnPre, CorruptPawnPost;

	private static final Progression[] VALUES = values();

	public static Progression get(int ordinal) {
		if (ordinal < 0 | ordinal >= VALUES.length)
			return None;
		else
			return VALUES[ordinal];
	}

}
