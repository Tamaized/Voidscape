package tamaized.voidscape.turmoil.abilities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.server.SEntityVelocityPacket;
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
	public static final TurmoilAbility BULWARK = new TurmoilAbility(unloc("bulwark"), TurmoilAbility.Type.Voidic, 300, 30 * 20, (spell, caster) -> caster.
			addEffect(new EffectInstance(ModEffects.BULWARK.get(), 10 * 20)));
	public static final TurmoilAbility SHOUT = new TurmoilAbility(unloc("shout"), TurmoilAbility.Type.Voidic, 50, 5 * 20, (spell, caster) -> {
		float damage = spell.damage(caster);
		for (Entity e : caster.level.getEntities(caster, caster.getBoundingBox().inflate(1F).move(caster.getLookAngle().scale(2F)))) {
			if (e.hurt(ModDamageSource.VOIDIC_WITH_ENTITY.apply(caster), damage) && e instanceof MobEntity)
				e.getCapability(SubCapability.CAPABILITY_AGGRO).ifPresent(cap -> cap.mulHate(caster, 1.1D));
		}
		return true;
	}).damage(1F);
	public static final TurmoilAbility ADRENALINE = new TurmoilAbility(unloc("adrenaline"), TurmoilAbility.Type.Insane, 300, 30 * 20, (spell, caster) -> caster.
			addEffect(new EffectInstance(ModEffects.ADRENALINE.get(), 10 * 20)));
	public static final TurmoilAbility TUNNEL_VISION = new TurmoilAbility(unloc("tunnel_vision"), TurmoilAbility.Type.Voidic, 500, 30 * 20, (spell, caster) -> {
		RayTraceResult ray = Voidscape.getHitResultFromEyes(caster, e -> e instanceof LivingEntity, 32);
		if (caster.level instanceof ServerWorld && ray instanceof EntityRayTraceResult) {
			LivingEntity entity = (LivingEntity) ((EntityRayTraceResult) ray).getEntity();
			return ModEffects.target(caster, entity, ModEffects.TUNNEL_VISION.get(), 10 * 20, 1, true);
		}
		return false;
	});
	public static final TurmoilAbility EMPOWER_SHIELD_2X_NULL = new TurmoilAbility(unloc("empower_shield_2x_null"), TurmoilAbility.Type.Voidic, 400, 5 * 20, (spell, caster) -> ModEffects.
			apply(caster, ModEffects.EMPOWER_SHIELD_2X_NULL.get(), 10 * 20, 1));
	public static final TurmoilAbility BACKSTEP = new TurmoilAbility(unloc("backstep"), TurmoilAbility.Type.Null, 200, 5 * 20, (spell, caster) -> {
		caster.setDeltaMovement(caster.getLookAngle().yRot((float) Math.toRadians(180)).add(0F, 0.5F, 0F));
		if (caster instanceof ServerPlayerEntity)
			((ServerPlayerEntity) caster).connection.send(new SEntityVelocityPacket(caster));
		return true;
	});

	private static String unloc(String loc) {
		return Voidscape.MODID.concat(".abilities.tank.".concat(loc));
	}

}
