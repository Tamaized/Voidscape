package tamaized.voidscape.capability;

public class VoidicArrowCapability implements IVoidicArrow {

	private float damage;

	@Override
	public void setDamage(float dmg) {
		damage = dmg;
	}

	@Override
	public float getDamage() {
		return damage;
	}
}
