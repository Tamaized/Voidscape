package tamaized.voidscape.turmoil.abilities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.server.ServerWorld;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.entity.abilities.EntitySpellAura;
import tamaized.voidscape.entity.abilities.EntitySpellBolt;
import tamaized.voidscape.registry.ModDamageSource;
import tamaized.voidscape.registry.ModEffects;
import tamaized.voidscape.turmoil.SubCapability;
import tamaized.voidscape.turmoil.TurmoilStats;
import tamaized.voidscape.turmoil.skills.TurmoilSkills;

import java.util.Optional;

public class MageAbilities {

	public static final TurmoilAbility BOLT = new TurmoilAbility(unloc("bolt"), TurmoilAbility.Type.Voidic, 150, 3 * 20, (spell, caster) -> {
		EntitySpellBolt bolt = new EntitySpellBolt(caster, MageAbilities.BOLT).color(0x7700FF);
		caster.getCapability(SubCapability.CAPABILITY).ifPresent(cap -> cap.get(Voidscape.subCapTurmoilStats).ifPresent(stats -> {
			if (stats.isActive(MageAbilities.HOMING_BOLTS))
				bolt.homing();
		}));
		bolt.setDamage(spell.damage(caster, caster.getCapability(SubCapability.CAPABILITY).map(cap -> cap.get(Voidscape.subCapTurmoilData).map(data ->

					data.hasSkill(TurmoilSkills.MAGE_SKILLS.VOIDMANCY_5) ? 4 :

							data.hasSkill(TurmoilSkills.MAGE_SKILLS.VOIDMANCY_4) ? 3 :

									data.hasSkill(TurmoilSkills.MAGE_SKILLS.VOIDMANCY_3) ? 2 :

											data.hasSkill(TurmoilSkills.MAGE_SKILLS.VOIDMANCY_2) ? 1 : 0).orElse(0)).orElse(0)));
		caster.level.addFreshEntity(bolt);
		return true;
	}).damage(1F);
	public static final TurmoilAbility HOMING_BOLTS = new TurmoilAbility(unloc("homing_bolts"), TurmoilAbility.Type.Voidic, 50, 10 * 20, TurmoilAbility.Toggle.Voidic);
	public static final TurmoilAbility AURA = new TurmoilAbility(unloc("aura"), TurmoilAbility.Type.Voidic, 250, 45 * 20, (spell, caster) ->

			caster.level.addFreshEntity(new EntitySpellAura(MageAbilities.AURA, caster, 0x7700FF, 30L * 20L).damage(spell.damage(caster)))).damage(1F);
	public static final TurmoilAbility ARROW_IMBUE_SPELLLIKE = new TurmoilAbility(unloc("arrow_imbue_spelllike"), TurmoilAbility.Type.Voidic, 50, 3 * 20, TurmoilAbility.Toggle.Imbue);
	public static final TurmoilAbility FLAME_SHOT = new TurmoilAbility(unloc("flame_shot"), TurmoilAbility.Type.Voidic, 200, 5 * 20, (spell, caster) -> ModEffects.
			apply(caster, ModEffects.FIRE_ARROW.get(), 15 * 20, 0));
	public static final TurmoilAbility ECHO = new TurmoilAbility(unloc("echo"), TurmoilAbility.Type.Voidic, 50, 5 * 20, (spell, caster) -> {
		Optional<TurmoilStats> stats = caster.getCapability(SubCapability.CAPABILITY).map(cap -> cap.get(Voidscape.subCapTurmoilStats)).orElse(Optional.empty());
		float damage = spell.damage(caster);
		for (Entity e : caster.level.getEntities(caster, caster.getBoundingBox().inflate(1F).move(caster.getLookAngle().scale(2F)))) {
			if (e.hurt(ModDamageSource.VOIDIC_WITH_ENTITY.apply(caster), damage) && e instanceof LivingEntity)
				stats.ifPresent(data -> data.increasePowerFromAbilityDamage(caster, (LivingEntity) e, spell));
		}
		return true;
	}).damage(2F);
	public static final TurmoilAbility TRAUMATIZE = new TurmoilAbility(unloc("traumatize"), TurmoilAbility.Type.Insane, 200, 20 * 20, (spell, caster) -> {
		RayTraceResult ray = Voidscape.getHitResultFromEyes(caster, e -> e instanceof MobEntity, 32);
		if (caster.level instanceof ServerWorld && ray instanceof EntityRayTraceResult) {
			MobEntity entity = (MobEntity) ((EntityRayTraceResult) ray).getEntity();
			return ModEffects.dot(caster, entity, ModEffects.TRAUMATIZE.get(), 20 * 20, 0, spell.damage(caster));
		}
		return false;
	}).damage(1F);

	private static String unloc(String loc) {
		return Voidscape.MODID.concat(".abilities.mage.".concat(loc));
	}

}
