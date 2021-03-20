package tamaized.voidscape.turmoil.abilities;

import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.entity.abilities.EntitySpellAura;
import tamaized.voidscape.entity.abilities.EntitySpellBolt;
import tamaized.voidscape.registry.ModDamageSource;
import tamaized.voidscape.registry.ModEffects;
import tamaized.voidscape.turmoil.SubCapability;

public class HealerAbilities {

	public static final TurmoilAbility REZ = new TurmoilAbility(unloc("resurrection"), TurmoilAbility.Type.Voidic, 800, 30 * 20, (spell, caster) -> {
		RayTraceResult ray = Voidscape.getHitResultFromEyes(caster, e -> e.getCapability(SubCapability.CAPABILITY).map(cap -> cap.get(Voidscape.subCapTurmoilTracked).map(data -> data.incapacitated).orElse(false)).orElse(false), 32, 1.5D, 0D);
		if (caster.level instanceof ServerWorld && ray instanceof EntityRayTraceResult) {
			Entity entity = ((EntityRayTraceResult) ray).getEntity();
			entity.getCapability(SubCapability.CAPABILITY).ifPresent(cap -> cap.get(Voidscape.subCapTurmoilTracked).ifPresent(data -> data.incapacitated = false));
			for (int i = 0; i < 10; i++) {
				Vector3d pos = new Vector3d(0.25F + entity.level.random.nextFloat() * 0.75F, 0, 0).yRot((float) Math.toRadians(entity.level.random.nextInt(360))).add(entity.getX(), entity.getY() + entity.level.random.nextFloat(), entity.getZ());
				((ServerWorld) caster.level).sendParticles(ParticleTypes.END_ROD, pos.x, pos.y, pos.z, 0, 0, 0, 0, 1);
			}
			caster.level.playSound(null, entity, SoundEvents.BEACON_POWER_SELECT, SoundCategory.PLAYERS, 1F, 0.75F + entity.level.random.nextFloat() * 0.5F);
			return true;
		}
		return false;
	});
	public static final TurmoilAbility HEALING_BOLT = new TurmoilAbility(unloc("healing_bolt"), TurmoilAbility.Type.Voidic, 100, 3 * 20, (spell, caster) -> {
		EntitySpellBolt bolt = new EntitySpellBolt(caster, HealerAbilities.HEALING_BOLT).healing().color(0xFFFF00);
		bolt.setDamage(spell.damage(caster));
		caster.level.addFreshEntity(bolt);
		return true;
	}).damage(1F);
	public static final TurmoilAbility HEALING_BLAST = new TurmoilAbility(unloc("healing_blast"), TurmoilAbility.Type.Voidic, 200, 3 * 20, (spell, caster) -> {
		EntitySpellBolt bolt = new EntitySpellBolt(caster, HealerAbilities.HEALING_BOLT).healing().color(0xFFFF00).burst();
		bolt.setDamage(spell.damage(caster));
		caster.level.addFreshEntity(bolt);
		return true;
	}).damage(0.5F);
	public static final TurmoilAbility MEND = new TurmoilAbility(unloc("mend"), TurmoilAbility.Type.Voidic, 100, 3 * 20, (spell, caster) -> {
		RayTraceResult ray = Voidscape.getHitResultFromEyes(caster, e -> e instanceof LivingEntity, 2);
		if (caster.level instanceof ServerWorld && ray instanceof EntityRayTraceResult) {
			LivingEntity entity = (LivingEntity) ((EntityRayTraceResult) ray).getEntity();
			if (entity.getMobType() == CreatureAttribute.UNDEAD)
				return entity.hurt(ModDamageSource.VOIDIC_WITH_ENTITY.apply(caster), spell.damage(caster));
			else
				return Voidscape.healTargetAndAggro(entity, caster, spell.damage(caster));
		}
		return false;
	}).damage(2F);
	public static final TurmoilAbility MIND_WARP = new TurmoilAbility(unloc("mind_warp"), TurmoilAbility.Type.Insane, 250, 10 * 20, (spell, caster) -> {
		RayTraceResult ray = Voidscape.getHitResultFromEyes(caster, e -> e instanceof LivingEntity, 32);
		if (caster.level instanceof ServerWorld && ray instanceof EntityRayTraceResult) {
			LivingEntity entity = (LivingEntity) ((EntityRayTraceResult) ray).getEntity();
			return ModEffects.dot(caster, entity, ModEffects.MIND_WARP.get(), 10 * 20, 1, spell.damage(caster));
		}
		return false;
	}).damage(0.5F);
	public static final TurmoilAbility HEALING_AURA = new TurmoilAbility(unloc("healing_aura"), TurmoilAbility.Type.Voidic, 400, 60 * 20, (spell, caster) ->

			caster.level.addFreshEntity(new EntitySpellAura(MageAbilities.AURA, caster, 0xFFFF00, 30L * 20L).damage(spell.damage(caster)).healing())).damage(0.5F);
	public static final TurmoilAbility EMPOWER_SWORD_OSMOSIS = new TurmoilAbility(unloc("empower_sword_osmosis"), TurmoilAbility.Type.Null, 100, 3 * 20, (spell, caster) -> ModEffects.
			apply(caster, ModEffects.EMPOWER_SWORD_OSMOSIS.get(), 10 * 20, 1));

	private static String unloc(String loc) {
		return Voidscape.MODID.concat(".abilities.healer.".concat(loc));
	}

}
