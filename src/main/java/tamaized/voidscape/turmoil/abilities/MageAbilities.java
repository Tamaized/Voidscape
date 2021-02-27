package tamaized.voidscape.turmoil.abilities;

import tamaized.voidscape.Voidscape;
import tamaized.voidscape.entity.abilities.mage.EntitySpellBolt;
import tamaized.voidscape.turmoil.SubCapability;

public class MageAbilities {

	public static final TurmoilAbility BOLT = new TurmoilAbility(unloc("bolt"), TurmoilAbility.Toggle.None, TurmoilAbility.Type.Voidic, 150, 3 * 20, (spell, caster) -> {
		EntitySpellBolt bolt = new EntitySpellBolt(caster);
		bolt.setDamage(caster.getCapability(SubCapability.CAPABILITY).map(cap -> cap.get(Voidscape.subCapTurmoilStats).map(stats -> {
			float damage = spell.damage();
			damage *= 1F + (stats.stats().spellpower / 100F);
			if (caster.level.getRandom().nextInt(100) + 1 <= stats.stats().spellCrit)
				damage *= 1.25F;
			return damage;
		}).get()).orElse(0F));
		caster.level.addFreshEntity(bolt);
		return true;
	}).damage(1F);

	private static String unloc(String loc) {
		return Voidscape.MODID.concat(".abilities.mage.".concat(loc));
	}

}
