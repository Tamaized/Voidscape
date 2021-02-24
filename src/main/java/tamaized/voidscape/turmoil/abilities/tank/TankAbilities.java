package tamaized.voidscape.turmoil.abilities.tank;

import net.minecraft.entity.MobEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.turmoil.abilities.TurmoilAbility;

public class TankAbilities {

	public static final TurmoilAbility TAUNT = new TurmoilAbility(unloc("taunt"), TurmoilAbility.Toggle.None, TurmoilAbility.Type.Voidic, 50, 15 * 20, (spell, caster) -> {
		RayTraceResult ray = ProjectileHelper.getHitResult(caster, e -> e instanceof MobEntity);
		if (ray instanceof EntityRayTraceResult)
			((MobEntity) ((EntityRayTraceResult) ray).getEntity()).setTarget(caster);
		// TODO: sound and particles
	});

	private static String unloc(String loc) {
		return Voidscape.MODID.concat(".abilities.tank.".concat(loc));
	}

}
