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

import java.util.function.Function;

public final class VoidTeleporter implements ITeleporter {

	public static final VoidTeleporter INSTANCE = new VoidTeleporter();

	private VoidTeleporter() {

	}

	@Override
	public Entity placeEntity(Entity oldEntity, ServerWorld currentWorld, ServerWorld destWorld, float yaw, Function<Boolean, Entity> repositionEntity) {
		oldEntity.fallDistance = 0;
		return repositionEntity.apply(false);
	}

	public static PortalInfo position(Entity oldEntity, ServerWorld destWorld) {
		if (!Voidscape.checkForVoidDimension(destWorld)) {
			BlockPos pos = null;
			if (oldEntity instanceof PlayerEntity)
				pos = ((ServerPlayerEntity) oldEntity).func_241140_K_();
			if (pos == null)
				pos = destWorld.getHeight(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, destWorld.func_241135_u_());
			return new PortalInfo(new Vector3d(pos.getX() + 0.5F, pos.getY() + 1, pos.getZ() + 0.5F), Vector3d.ZERO, oldEntity.rotationYaw, oldEntity.rotationPitch);
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
									pos.setPos(oldEntity.getPosX() + x, oldEntity.getPosY() + y, oldEntity.getPosZ() + z);
									if (destWorld.getBlockState(pos).isTopSolid(destWorld, pos, oldEntity, Direction.UP)) {
										final int height = (int) (oldEntity.getHeight() + 1);
										for (int c = 1; c < height; c++) {
											pos.setPos(oldEntity.getPosX() + x, oldEntity.getPosY() + y + c, oldEntity.getPosZ() + z);
											if (!destWorld.isAirBlock(pos))
												continue scan;
										}
										pos.setPos(oldEntity.getPosX() + x, oldEntity.getPosY() + y, oldEntity.getPosZ() + z);
										return new PortalInfo(new Vector3d(pos.getX() + 0.5F, pos.getY() + 1, pos.getZ() + 0.5F), Vector3d.ZERO, oldEntity.rotationYaw, oldEntity.rotationPitch);
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
		return new PortalInfo(oldEntity.getPositionVec(), Vector3d.ZERO, oldEntity.rotationYaw, oldEntity.rotationPitch);
	}
}
