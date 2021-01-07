package tamaized.voidscape.world;

import net.minecraft.block.PortalInfo;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.ITeleporter;

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
		return new PortalInfo(new Vector3d(0, 61, 0), Vector3d.ZERO, oldEntity.yRot, oldEntity.xRot);
	}
}
