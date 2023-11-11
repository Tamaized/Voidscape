package tamaized.voidscape.world;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.util.ITeleporter;
import tamaized.voidscape.Voidscape;

import java.util.Optional;
import java.util.function.Function;

public final class VoidTeleporter implements ITeleporter {

	public static final VoidTeleporter INSTANCE = new VoidTeleporter();

	private VoidTeleporter() {

	}

	@Override
	public Entity placeEntity(Entity oldEntity, ServerLevel currentWorld, ServerLevel destWorld, float yaw, Function<Boolean, Entity> repositionEntity) {
		oldEntity.fallDistance = 0;
		return repositionEntity.apply(false);
	}

	@Override
	public PortalInfo getPortalInfo(Entity oldEntity, ServerLevel destWorld, Function<ServerLevel, PortalInfo> defaultPortalInfo) {
		if (!Voidscape.checkForVoidDimension(destWorld)) {
			Vec3 pos = null;
			if (oldEntity instanceof Player) {
				BlockPos p = ((ServerPlayer) oldEntity).getRespawnPosition();
				if (p != null) {
					Optional<Vec3> o = Player.findRespawnPositionAndUseSpawnBlock(destWorld, p, ((ServerPlayer) oldEntity).getRespawnAngle(), ((ServerPlayer) oldEntity).isRespawnForced(), false);
					if (o.isPresent())
						pos = o.get();
				}
			}
			if (pos == null) {
				BlockPos bp = destWorld.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, destWorld.getSharedSpawnPos());
				pos = new Vec3(bp.getX() + 0.5F, bp.getY() + 1F, bp.getZ() + 0.5F);
			}
			return new PortalInfo(pos, Vec3.ZERO, oldEntity.getYRot(), oldEntity.getXRot());
		} else {
			int scan = 2;
			int lastScan = 0;
			final BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
			while (true) {
				for (int y = -scan; y < scan; y++)
					for (int x = -scan; x < scan; x++)
						scan:
								for (int z = -scan; z < scan; z++) {
									if (lastScan > 0 &&

											(x >= -lastScan && x <= lastScan) &&

											(y >= -lastScan && y <= lastScan) &&

											(z >= -lastScan && z <= lastScan)

									)
										continue;
									pos.set(oldEntity.getX() + x, oldEntity.getY() + y, oldEntity.getZ() + z);
									int xOff = 0;
									int zOff = 0;
									if (pos.getX() * pos.getX() + pos.getZ() * pos.getZ() <= 33800) {
										xOff = pos.getX() > 0 ? 130 : -130;
										zOff = pos.getZ() > 0 ? 130 : -130;
									}
									pos.set(oldEntity.getX() + x + xOff, oldEntity.getY() + y, oldEntity.getZ() + z + zOff);
									if (destWorld.getBlockState(pos).entityCanStandOnFace(destWorld, pos, oldEntity, Direction.UP)) {
										final int height = (int) (oldEntity.getBbHeight() + 1);
										for (int c = 1; c < height; c++) {
											pos.set(oldEntity.getX() + x + xOff, oldEntity.getY() + y + c, oldEntity.getZ() + z + zOff);
											if (!destWorld.isEmptyBlock(pos))
												continue scan;
										}
										pos.set(oldEntity.getX() + x + xOff, oldEntity.getY() + y, oldEntity.getZ() + z + zOff);
										return new PortalInfo(new Vec3(pos.getX() + 0.5F, pos.getY() + 1, pos.getZ() + 0.5F), Vec3.ZERO, oldEntity.getYRot(), oldEntity.getXRot());
									}
								}
				lastScan = scan;
				scan *= 2;
				Voidscape.LOGGER.debug("Increasing Teleportation Scan Radius: {}", scan * 2);
				if (scan * 2 >= 1024) {
					Voidscape.LOGGER.debug("Teleportation Scan Radius exceeds 1024, giving up.");
					break;
				}
			}
		}
		return new PortalInfo(oldEntity.position(), Vec3.ZERO, oldEntity.getYRot(), oldEntity.getXRot());
	}
}
