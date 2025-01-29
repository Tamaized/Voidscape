package tamaized.voidscape.dimension;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.Vec3;
import tamaized.beanification.Component;
import tamaized.voidscape.Voidscape;

import java.util.Optional;

@Component
public final class SpawnPointTeleporter {

	public DimensionTransition make(Entity entity, ServerLevel fallbackDestination) {
		if (entity instanceof ServerPlayer player) {
			return player.findRespawnPositionAndUseSpawnBlock(true, DimensionTransition.PLAY_PORTAL_SOUND);
		}

		BlockPos bp = fallbackDestination.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, fallbackDestination.getSharedSpawnPos());
		Vec3 pos = new Vec3(bp.getX() + 0.5F, bp.getY() + 1F, bp.getZ() + 0.5F);

		return new DimensionTransition(
			fallbackDestination,
			pos,
			Vec3.ZERO,
			entity.getYRot(),
			entity.getXRot(),
			DimensionTransition.PLAY_PORTAL_SOUND
		);
	}

}
