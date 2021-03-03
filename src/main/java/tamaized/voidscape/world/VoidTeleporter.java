package tamaized.voidscape.world;

import net.minecraft.block.PortalInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.ITeleporter;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.party.PartyManager;

import java.util.Optional;
import java.util.function.Function;

public final class VoidTeleporter implements ITeleporter {

	public static final VoidTeleporter INSTANCE = new VoidTeleporter();

	private VoidTeleporter() {

	}

	@Override
	public Entity placeEntity(Entity oldEntity, ServerWorld currentWorld, ServerWorld destWorld, float yaw, Function<Boolean, Entity> repositionEntity) {
		if (currentWorld.getChunkSource().getGenerator() instanceof InstanceChunkGenerator && oldEntity instanceof ServerPlayerEntity)
			PartyManager.findParty((ServerPlayerEntity) oldEntity).ifPresent(party -> party.removeMember((ServerPlayerEntity) oldEntity));
		oldEntity.fallDistance = 0;
		return repositionEntity.apply(false);
	}

	@Override
	public PortalInfo getPortalInfo(Entity oldEntity, ServerWorld destWorld, Function<ServerWorld, PortalInfo> defaultPortalInfo) {
		if (!Voidscape.checkForVoidDimension(destWorld)) {
			Vector3d pos = null;
			if (oldEntity instanceof PlayerEntity) {
				Optional<Vector3d> o = PlayerEntity.findRespawnPositionAndUseSpawnBlock(destWorld, ((ServerPlayerEntity) oldEntity).getRespawnPosition(), ((ServerPlayerEntity) oldEntity).getRespawnAngle(), ((ServerPlayerEntity) oldEntity).isRespawnForced(), false);
				if (o.isPresent())
					pos = o.get();
			}
			if (pos == null) {
				BlockPos bp = destWorld.getHeightmapPos(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, destWorld.getSharedSpawnPos());
				pos = new Vector3d(bp.getX() + 0.5F, bp.getY() + 1F, bp.getZ() + 0.5F);
			}
			return new PortalInfo(pos, Vector3d.ZERO, oldEntity.yRot, oldEntity.xRot);
		} else {
			int scan = 2;
			int lastScan = 0;
			final BlockPos.Mutable pos = new BlockPos.Mutable();
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
									if (destWorld.getBlockState(pos).entityCanStandOnFace(destWorld, pos, oldEntity, Direction.UP)) {
										final int height = (int) (oldEntity.getBbHeight() + 1);
										for (int c = 1; c < height; c++) {
											pos.set(oldEntity.getX() + x, oldEntity.getY() + y + c, oldEntity.getZ() + z);
											if (!destWorld.isEmptyBlock(pos))
												continue scan;
										}
										pos.set(oldEntity.getX() + x, oldEntity.getY() + y, oldEntity.getZ() + z);
										return new PortalInfo(new Vector3d(pos.getX() + 0.5F, pos.getY() + 1, pos.getZ() + 0.5F), Vector3d.ZERO, oldEntity.yRot, oldEntity.xRot);
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
		return new PortalInfo(oldEntity.position(), Vector3d.ZERO, oldEntity.yRot, oldEntity.xRot);
	}
}
