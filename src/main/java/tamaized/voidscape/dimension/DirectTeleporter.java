package tamaized.voidscape.dimension;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.Vec3;
import tamaized.beanification.Component;
import tamaized.voidscape.Voidscape;

import java.util.Optional;

@Component
public final class DirectTeleporter {

	public DimensionTransition make(Entity entity, ServerLevel destination) {
		WorldBorder border = destination.getWorldBorder();
		double minX = Math.max(-2.9999872E7D, border.getMinX() + 16.0D);
		double minZ = Math.max(-2.9999872E7D, border.getMinZ() + 16.0D);
		double maxX = Math.min(2.9999872E7D, border.getMaxX() - 16.0D);
		double maxZ = Math.min(2.9999872E7D, border.getMaxZ() - 16.0D);
		double offset = DimensionType.getTeleportationScale(entity.level().dimensionType(), destination.dimensionType());
		Vec3 location = findSafeLocation(
			entity,
			destination,
			BlockPos.containing(Mth.clamp(entity.getX() * offset, minX, maxX), entity.getY(), Mth.clamp(entity.getZ() * offset, minZ, maxZ))
		).orElse(entity.position());

		return new DimensionTransition(
			destination,
			location,
			Vec3.ZERO,
			entity.getYRot(),
			entity.getXRot(),
			DimensionTransition.PLAY_PORTAL_SOUND
		);
	}

	private Optional<Vec3> findSafeLocation(Entity oldEntity, ServerLevel destWorld, BlockPos startingLocation) {
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
							pos.set(startingLocation.getX() + x, startingLocation.getY() + y, startingLocation.getZ() + z);
							int xOff = 0;
							int zOff = 0;
							if (pos.getX() * pos.getX() + pos.getZ() * pos.getZ() <= 33800) {
								xOff = pos.getX() > 0 ? 130 : -130;
								zOff = pos.getZ() > 0 ? 130 : -130;
							}
							pos.set(startingLocation.getX() + x + xOff, startingLocation.getY() + y, startingLocation.getZ() + z + zOff);
							if (destWorld.getBlockState(pos).entityCanStandOnFace(destWorld, pos, oldEntity, Direction.UP)) {
								final int height = (int) (oldEntity.getBbHeight() + 1);
								for (int c = 1; c < height; c++) {
									pos.set(startingLocation.getX() + x + xOff, startingLocation.getY() + y + c, startingLocation.getZ() + z + zOff);
									if (!destWorld.isEmptyBlock(pos))
										continue scan;
								}
								pos.set(startingLocation.getX() + x + xOff, startingLocation.getY() + y, startingLocation.getZ() + z + zOff);
								return Optional.of(new Vec3(pos.getX() + 0.5F, pos.getY() + 1, pos.getZ() + 0.5F));
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
		return Optional.empty();
	}
}
