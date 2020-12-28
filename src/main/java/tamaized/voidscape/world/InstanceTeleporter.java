package tamaized.voidscape.world;

import net.minecraft.block.PortalInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.ITeleporter;
import tamaized.voidscape.Voidscape;

import java.util.function.Function;

public final class InstanceTeleporter implements ITeleporter {

	public static final InstanceTeleporter INSTANCE = new InstanceTeleporter();

	private InstanceTeleporter() {

	}

	@Override
	public Entity placeEntity(Entity oldEntity, ServerWorld currentWorld, ServerWorld destWorld, float yaw, Function<Boolean, Entity> repositionEntity) {
		oldEntity.fallDistance = 0;
		return repositionEntity.apply(false);
	}

	@Override
	public PortalInfo getPortalInfo(Entity oldEntity, ServerWorld destWorld, Function<ServerWorld, PortalInfo> defaultPortalInfo) {
		if (!Voidscape.checkForVoidDimension(destWorld)) {
			BlockPos pos = null;
			if (oldEntity instanceof PlayerEntity)
				pos = ((ServerPlayerEntity) oldEntity).getRespawnPosition();
			if (pos == null)
				pos = destWorld.getHeightmapPos(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, destWorld.getSharedSpawnPos());
			return new PortalInfo(new Vector3d(pos.getX() + 0.5F, pos.getY() + 1, pos.getZ() + 0.5F), Vector3d.ZERO, oldEntity.yRot, oldEntity.xRot);
		}
		return new PortalInfo(new Vector3d(0, 61, 0), Vector3d.ZERO, oldEntity.yRot, oldEntity.xRot);
	}
}
