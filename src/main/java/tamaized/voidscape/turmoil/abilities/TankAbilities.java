package tamaized.voidscape.turmoil.abilities;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.registry.ModDamageSource;
import tamaized.voidscape.registry.ModEffects;
import tamaized.voidscape.turmoil.SubCapability;

public class TankAbilities {

	public static final TurmoilAbility TAUNT = new TurmoilAbility(unloc("taunt"), TurmoilAbility.Type.Voidic, 50, 15 * 20, (spell, caster) -> {
		HitResult ray = Voidscape.getHitResultFromEyes(caster, e -> e instanceof Mob, 32);
		if (caster.level instanceof ServerLevel && ray instanceof EntityHitResult) {
			Mob entity = (Mob) ((EntityHitResult) ray).getEntity();
			entity.setTarget(caster);
			entity.getCapability(SubCapability.CAPABILITY_AGGRO).ifPresent(cap -> cap.placeAtTop(caster));
			for (int i = 0; i < 10; i++) {
				Vec3 pos = new Vec3(0.25F + entity.getRandom().nextFloat() * 0.75F, 0, 0).yRot((float) Math.toRadians(entity.getRandom().nextInt(360))).add(entity.getX(), entity.getEyeY() - 0.5F + entity.getRandom().nextFloat(), entity.getZ());
				((ServerLevel) caster.level).sendParticles(ParticleTypes.ANGRY_VILLAGER, pos.x, pos.y, pos.z, 0, 0, 0, 0, 1);
			}
			caster.level.playSound(null, entity, SoundEvents.FIRE_AMBIENT, SoundSource.PLAYERS, 1F, 0.75F + entity.getRandom().nextFloat() * 0.5F);
			return true;
		}
		return false;
	});
	public static final TurmoilAbility BULWARK = new TurmoilAbility(unloc("bulwark"), TurmoilAbility.Type.Voidic, 300, 30 * 20, (spell, caster) -> caster.
			addEffect(new MobEffectInstance(ModEffects.BULWARK.get(), 10 * 20)));
	public static final TurmoilAbility SHOUT = new TurmoilAbility(unloc("shout"), TurmoilAbility.Type.Voidic, 50, 5 * 20, (spell, caster) -> {
		float damage = spell.damage(caster);
		for (Entity e : caster.level.getEntities(caster, caster.getBoundingBox().inflate(1F).move(caster.getLookAngle().scale(2F)))) {
			if (e.hurt(ModDamageSource.VOIDIC_WITH_ENTITY.apply(caster), damage) && e instanceof Mob)
				e.getCapability(SubCapability.CAPABILITY_AGGRO).ifPresent(cap -> cap.mulHate(caster, 1.1D));
		}
		return true;
	}).damage(1F).texture(MageAbilities.ECHO.getTexture());
	public static final TurmoilAbility ADRENALINE = new TurmoilAbility(unloc("adrenaline"), TurmoilAbility.Type.Insane, 300, 30 * 20, (spell, caster) -> {
		if (!(caster.getMainHandItem().getItem() instanceof AxeItem))
			return false;
		return caster.addEffect(new MobEffectInstance(ModEffects.ADRENALINE.get(), 10 * 20));
	});
	public static final TurmoilAbility TUNNEL_VISION = new TurmoilAbility(unloc("tunnel_vision"), TurmoilAbility.Type.Voidic, 500, 30 * 20, (spell, caster) -> {
		HitResult ray = Voidscape.getHitResultFromEyes(caster, e -> e instanceof LivingEntity, 32);
		if (caster.level instanceof ServerLevel && ray instanceof EntityHitResult) {
			LivingEntity entity = (LivingEntity) ((EntityHitResult) ray).getEntity();
			return ModEffects.target(caster, entity, ModEffects.TUNNEL_VISION.get(), 10 * 20, 0, true);
		}
		return false;
	});
	public static final TurmoilAbility EMPOWER_SHIELD_2X_NULL = new TurmoilAbility(unloc("empower_shield_2x_null"), TurmoilAbility.Type.Voidic, 400, 5 * 20, (spell, caster) -> ModEffects.
			apply(caster, ModEffects.EMPOWER_SHIELD_2X_NULL.get(), 10 * 20, 0));
	public static final TurmoilAbility BACKSTEP = new TurmoilAbility(unloc("backstep"), TurmoilAbility.Type.Null, 200, 5 * 20, (spell, caster) -> {
		if (!caster.getOffhandItem().isShield(caster))
			return false;
		caster.setDeltaMovement(caster.getLookAngle().yRot((float) Math.toRadians(180)).add(0F, 0.5F, 0F));
		if (caster instanceof ServerPlayer)
			((ServerPlayer) caster).connection.send(new ClientboundSetEntityMotionPacket(caster));
		return true;
	});

	private static String unloc(String loc) {
		return Voidscape.MODID.concat(".abilities.tank.".concat(loc));
	}

}
