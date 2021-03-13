package tamaized.voidscape.turmoil.caps;

public class FireArrowCapability implements IFireArrow {

	private boolean active;

	@Override
	public void mark() {
		active = true;
	}

	@Override
	public boolean active() {
		return active;
	}
}
