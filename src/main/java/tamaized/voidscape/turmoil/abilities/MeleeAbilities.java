package tamaized.voidscape.turmoil.abilities;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.registry.ModAttributes;
import tamaized.voidscape.registry.ModDamageSource;
import tamaized.voidscape.registry.ModEffects;
import tamaized.voidscape.turmoil.SubCapability;
import tamaized.voidscape.world.InstanceChunkGenerator;

public class MeleeAbilities {

	public static final TurmoilAbility RUSH = new TurmoilAbility(unloc("rush"), TurmoilAbility.Type.Voidic, 200, 15 * 20, (spell, caster) -> {
		final boolean flag = caster.level instanceof ServerWorld && ((ServerWorld) caster.level).getChunkSource().getGenerator() instanceof InstanceChunkGenerator;
		RayTraceResult ray = Voidscape.getHitResultFromEyes(caster, e -> !flag || !(e instanceof PlayerEntity), 16);
		if (caster.level instanceof ServerWorld && ray.getType() != RayTraceResult.Type.MISS) {
			caster.getCapability(SubCapability.CAPABILITY).ifPresent(cap -> cap.get(Voidscape.subCapTurmoilStats).ifPresent(stats -> stats.
					ramTowards(ray instanceof EntityRayTraceResult ? ((EntityRayTraceResult) ray).getEntity().position() : ray.getLocation(), spell.damage())));
			for (int i = 0; i < 25; i++) {
				Vector3d pos = new Vector3d(0.25F + caster.getRandom().nextFloat() * 0.75F, 0, 0).
						yRot((float) Math.toRadians(caster.getRandom().nextInt(360))).
						xRot((float) Math.toRadians(caster.getRandom().nextInt(360))).
						add(caster.getX(), caster.getBoundingBox().getCenter().y - 0.5F + caster.getRandom().nextFloat(), caster.getZ());
				((ServerWorld) caster.level).sendParticles(ParticleTypes.FIREWORK, pos.x, pos.y, pos.z, 0, 0, 0, 0, 1);
			}
			caster.level.playSound(null, caster, SoundEvents.FIREWORK_ROCKET_LAUNCH, SoundCategory.PLAYERS, 1F, 0.75F + caster.getRandom().nextFloat() * 0.5F);
			return true;
		}
		return false;
	}).damage(4);
	public static final TurmoilAbility EMPOWER_ATTACK_SLICING = new TurmoilAbility(unloc("empower_attack_slicing"), TurmoilAbility.Type.Voidic, 200, 30 * 20, (spell, caster) -> ModEffects.
			apply(caster, ModEffects.EMPOWER_ATTACK_SLICING.get(), 15 * 20, 1));
	public static final TurmoilAbility CLEAVE = new TurmoilAbility(unloc("cleave"), TurmoilAbility.Type.Voidic, 350, 30 * 20, (spell, caster) -> {
		float f = (float) (caster.getAttributeValue(Attributes.ATTACK_DAMAGE) + caster.getAttributeValue(ModAttributes.VOIDIC_DMG.get()));
		if (f > 0.0F) {
			caster.swing(Hand.MAIN_HAND, true);
			caster.level.playSound(null, caster.getX(), caster.getY(), caster.getZ(), SoundEvents.PLAYER_ATTACK_SWEEP, caster.getSoundSource(), 1.0F, 1.0F);
			double d0 = -MathHelper.sin(caster.yRot * ((float) Math.PI / 180F));
			double d1 = MathHelper.cos(caster.yRot * ((float) Math.PI / 180F));
			if (caster.level instanceof ServerWorld) {
				((ServerWorld) caster.level).sendParticles(ParticleTypes.SWEEP_ATTACK, caster.getX() + d0, caster.getY(0.5D), caster.getZ() + d1, 0, d0, 0.0D, d1, 0.0D);
			}
			for (LivingEntity livingentity : caster.level.getEntitiesOfClass(LivingEntity.class, caster.getBoundingBox().inflate(1F).move(caster.getLookAngle().scale(2F)))) {
				if (livingentity != caster && !caster.isAlliedTo(livingentity) && (!(livingentity instanceof ArmorStandEntity) || !((ArmorStandEntity) livingentity).isMarker()) && caster.distanceToSqr(livingentity) < 9.0D) {
					livingentity.knockback(0.4F, MathHelper.sin(caster.yRot * ((float) Math.PI / 180F)), -MathHelper.cos(caster.yRot * ((float) Math.PI / 180F)));
					livingentity.hurt(ModDamageSource.VOIDIC_WITH_ENTITY.apply(caster), f);
				}
			}
			return true;
		}
		return false;
	});
	public static final TurmoilAbility EMPOWER_ATTACK_BLEED = new TurmoilAbility(unloc("empower_attack_bleed"), TurmoilAbility.Type.Insane, 200, 45 * 20, (spell, caster) -> ModEffects.
			apply(caster, ModEffects.EMPOWER_ATTACK_BLEED.get(), 15 * 20, 1));
	public static final TurmoilAbility SENSE_WEAKNESS = new TurmoilAbility(unloc("sense_weakness"), TurmoilAbility.Type.Insane, 200, 45 * 20, (spell, caster) -> {
		RayTraceResult ray = Voidscape.getHitResultFromEyes(caster, e -> e instanceof MobEntity, 32);
		if (caster.level instanceof ServerWorld && ray instanceof EntityRayTraceResult) {
			MobEntity entity = (MobEntity) ((EntityRayTraceResult) ray).getEntity();
			return ModEffects.target(caster, entity, ModEffects.SENSE_WEAKNESS.get(), 15 * 20, 1, true);
		}
		return false;
	});

	private static String unloc(String loc) {
		return Voidscape.MODID.concat(".abilities.melee.".concat(loc));
	}

}
