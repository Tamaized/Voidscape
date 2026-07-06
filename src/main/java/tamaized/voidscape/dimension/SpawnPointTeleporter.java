package tamaized.voidscape.dimension;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.Vec3;
import tamaized.beanification.Component;

@Component
public final class SpawnPointTeleporter {

	public TeleportTransition make(Entity entity, ServerLevel fallbackDestination) {
		if (entity instanceof ServerPlayer player) {
			return player.findRespawnPositionAndUseSpawnBlock(true, TeleportTransition.PLAY_PORTAL_SOUND);
		}

		BlockPos bp = fallbackDestination.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, fallbackDestination.getRespawnData().pos());
		Vec3 pos = new Vec3(bp.getX() + 0.5F, bp.getY() + 1F, bp.getZ() + 0.5F);

		return new TeleportTransition(
			fallbackDestination,
			pos,
			Vec3.ZERO,
			entity.getYRot(),
			entity.getXRot(),
			TeleportTransition.PLAY_PORTAL_SOUND
		);
	}

}
