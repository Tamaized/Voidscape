package tamaized.voidscape.turmoil.abilities;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.turmoil.SubCapability;
import tamaized.voidscape.world.InstanceChunkGenerator;

public class MeleeAbilities {

	public static final TurmoilAbility RUSH = new TurmoilAbility(unloc("rush"), TurmoilAbility.Toggle.None, TurmoilAbility.Type.Voidic, 200, 15 * 20, (spell, caster) -> {
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

	private static String unloc(String loc) {
		return Voidscape.MODID.concat(".abilities.melee.".concat(loc));
	}

}
