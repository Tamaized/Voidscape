package tamaized.voidscape.world;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.ITeleporter;

import java.util.function.Function;

public final class InstanceTeleporter implements ITeleporter {

	public static final InstanceTeleporter INSTANCE = new InstanceTeleporter();

	private InstanceTeleporter() {

	}

	@Override
	public Entity placeEntity(Entity oldEntity, ServerLevel currentWorld, ServerLevel destWorld, float yaw, Function<Boolean, Entity> repositionEntity) {
		oldEntity.fallDistance = 0;
		return repositionEntity.apply(false);
	}

	@Override
	public PortalInfo getPortalInfo(Entity oldEntity, ServerLevel destWorld, Function<ServerLevel, PortalInfo> defaultPortalInfo) {
		return new PortalInfo(new Vec3(oldEntity.getX(), 61, oldEntity.getZ()), Vec3.ZERO, oldEntity.getYRot(), oldEntity.getXRot());
	}
}
