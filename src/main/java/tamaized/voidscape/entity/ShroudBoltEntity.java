package tamaized.voidscape.entity;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;
import org.joml.Vector3fc;

public class ShroudBoltEntity extends Entity {

	private static final EntityDataAccessor<Vector3fc> OFFSET = SynchedEntityData.defineId(ShroudBoltEntity.class, EntityDataSerializers.VECTOR3);
	private static final int LIFETIME = 5;

	public ShroudBoltEntity(EntityType<?> type, Level level) {
		super(type, level);
		noPhysics = true;
	}

	public ShroudBoltEntity(EntityType<?> type, Level level, Vec3 from, Vec3 to) {
		this(type, level);
		setPos(from);
		entityData.set(OFFSET, to.subtract(from).toVector3f());
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		builder.define(OFFSET, new Vector3f());
	}

	public Vector3fc getOffset() {
		return entityData.get(OFFSET);
	}

	@Override
	public void tick() {
		super.tick();
		if (!level().isClientSide() && tickCount >= LIFETIME)
			discard();
	}

	@Override
	public boolean hurtServer(ServerLevel level, DamageSource source, float amount) {
		return false;
	}

	@Override
	protected void readAdditionalSaveData(ValueInput input) {

	}

	@Override
	protected void addAdditionalSaveData(ValueOutput output) {

	}

}
