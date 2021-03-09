package tamaized.voidscape.turmoil.abilities;

import tamaized.voidscape.Voidscape;
import tamaized.voidscape.entity.abilities.mage.EntitySpellBolt;
import tamaized.voidscape.turmoil.SubCapability;
import tamaized.voidscape.turmoil.skills.TurmoilSkills;

public class MageAbilities {

	public static final TurmoilAbility HOMING_BOLTS = new TurmoilAbility(unloc("homing_bolts"), TurmoilAbility.Type.Voidic, 50, 10 * 20, TurmoilAbility.Toggle.Voidic);
	public static final TurmoilAbility BOLT = new TurmoilAbility(unloc("bolt"), TurmoilAbility.Type.Voidic, 150, 3 * 20, (spell, caster) -> {
		EntitySpellBolt bolt = new EntitySpellBolt(caster, MageAbilities.BOLT);
		bolt.setDamage(caster.getCapability(SubCapability.CAPABILITY).map(cap -> cap.get(Voidscape.subCapTurmoilStats).map(stats -> {
			if (stats.isActive(MageAbilities.HOMING_BOLTS))
				bolt.homing();
			float damage = spell.damage() + cap.get(Voidscape.subCapTurmoilData).map(data ->

					data.hasSkill(TurmoilSkills.MAGE_SKILLS.VOIDMANCY_5) ? 4 :

							data.hasSkill(TurmoilSkills.MAGE_SKILLS.VOIDMANCY_4) ? 3 :

									data.hasSkill(TurmoilSkills.MAGE_SKILLS.VOIDMANCY_3) ? 2 :

											data.hasSkill(TurmoilSkills.MAGE_SKILLS.VOIDMANCY_2) ? 1 : 0).orElse(0);
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
