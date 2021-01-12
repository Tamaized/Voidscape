package tamaized.voidscape.world;

import net.minecraft.block.PortalInfo;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.ITeleporter;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.turmoil.SubCapability;
import tamaized.voidscape.turmoil.Turmoil;

import java.util.function.Function;

public final class InstanceTeleporter implements ITeleporter {

	public static final InstanceTeleporter INSTANCE = new InstanceTeleporter();

	private InstanceTeleporter() {

	}

	@Override
	public Entity placeEntity(Entity oldEntity, ServerWorld currentWorld, ServerWorld destWorld, float yaw, Function<Boolean, Entity> repositionEntity) {
		oldEntity.fallDistance = 0;
		oldEntity.getCapability(SubCapability.CAPABILITY).ifPresent(cap -> cap.get(Voidscape.subCapTurmoilData).ifPresent(data -> data.setState(Turmoil.State.CLOSED)));
		return repositionEntity.apply(false);
	}

	@Override
	public PortalInfo getPortalInfo(Entity oldEntity, ServerWorld destWorld, Function<ServerWorld, PortalInfo> defaultPortalInfo) {
		return new PortalInfo(new Vector3d(oldEntity.getX(), 61, oldEntity.getZ()), Vector3d.ZERO, oldEntity.yRot, oldEntity.xRot);
	}
}
