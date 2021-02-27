package tamaized.voidscape.turmoil.abilities;

import net.minecraft.entity.MobEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;
import tamaized.voidscape.Voidscape;

public class TankAbilities {

	public static final TurmoilAbility TAUNT = new TurmoilAbility(unloc("taunt"), TurmoilAbility.Toggle.None, TurmoilAbility.Type.Voidic, 50, 15 * 20, (spell, caster) -> {
		RayTraceResult ray = Voidscape.getHitResultFromEyes(caster, e -> e instanceof MobEntity, 32);
		if (caster.level instanceof ServerWorld && ray instanceof EntityRayTraceResult) {
			MobEntity entity = (MobEntity) ((EntityRayTraceResult) ray).getEntity();
			entity.setTarget(caster);
			for (int i = 0; i < 10; i++) {
				Vector3d pos = new Vector3d(0.25F + entity.getRandom().nextFloat() * 0.75F, 0, 0).yRot((float) Math.toRadians(entity.getRandom().nextInt(360))).add(entity.getX(), entity.getEyeY() - 0.5F + entity.getRandom().nextFloat(), entity.getZ());
				((ServerWorld) caster.level).sendParticles(ParticleTypes.ANGRY_VILLAGER, pos.x, pos.y, pos.z, 0, 0, 0, 0, 1);
			}
			caster.level.playSound(null, entity, SoundEvents.FIRE_AMBIENT, SoundCategory.PLAYERS, 1F, 0.75F + entity.getRandom().nextFloat() * 0.5F);
			return true;
		}
		return false;
	});

	private static String unloc(String loc) {
		return Voidscape.MODID.concat(".abilities.tank.".concat(loc));
	}

}
