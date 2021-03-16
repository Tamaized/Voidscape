package tamaized.voidscape.turmoil.abilities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.registry.ModDamageSource;
import tamaized.voidscape.registry.ModEffects;
import tamaized.voidscape.turmoil.SubCapability;

public class TankAbilities {

	public static final TurmoilAbility TAUNT = new TurmoilAbility(unloc("taunt"), TurmoilAbility.Type.Voidic, 50, 15 * 20, (spell, caster) -> {
		RayTraceResult ray = Voidscape.getHitResultFromEyes(caster, e -> e instanceof MobEntity, 32);
		if (caster.level instanceof ServerWorld && ray instanceof EntityRayTraceResult) {
			MobEntity entity = (MobEntity) ((EntityRayTraceResult) ray).getEntity();
			entity.setTarget(caster);
			entity.getCapability(SubCapability.CAPABILITY_AGGRO).ifPresent(cap -> cap.placeAtTop(caster));
			for (int i = 0; i < 10; i++) {
				Vector3d pos = new Vector3d(0.25F + entity.getRandom().nextFloat() * 0.75F, 0, 0).yRot((float) Math.toRadians(entity.getRandom().nextInt(360))).add(entity.getX(), entity.getEyeY() - 0.5F + entity.getRandom().nextFloat(), entity.getZ());
				((ServerWorld) caster.level).sendParticles(ParticleTypes.ANGRY_VILLAGER, pos.x, pos.y, pos.z, 0, 0, 0, 0, 1);
			}
			caster.level.playSound(null, entity, SoundEvents.FIRE_AMBIENT, SoundCategory.PLAYERS, 1F, 0.75F + entity.getRandom().nextFloat() * 0.5F);
			return true;
		}
		return false;
	});
	public static final TurmoilAbility BULWARK = new TurmoilAbility(unloc("bulwark"), TurmoilAbility.Type.Voidic, 300, 30 * 20, (spell, caster) -> {
		caster.addEffect(new EffectInstance(ModEffects.BULWARK.get(), 10 * 20));
		return true;
	});
	public static final TurmoilAbility SHOUT = new TurmoilAbility(unloc("shout"), TurmoilAbility.Type.Voidic, 50, 5 * 20, (spell, caster) -> {
		float damage = spell.damage(caster);
		for (Entity e : caster.level.getEntities(caster, caster.getBoundingBox().inflate(1F).move(caster.getLookAngle().scale(2F)))) {
			if (e.hurt(ModDamageSource.VOIDIC_WITH_ENTITY.apply(caster), damage) && e instanceof MobEntity)
				e.getCapability(SubCapability.CAPABILITY_AGGRO).ifPresent(cap -> cap.mulHate(caster, 1.1D));
		}
		return true;
	}).damage(2F);

	private static String unloc(String loc) {
		return Voidscape.MODID.concat(".abilities.tank.".concat(loc));
	}

}
