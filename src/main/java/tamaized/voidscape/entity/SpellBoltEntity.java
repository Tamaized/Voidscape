package tamaized.voidscape.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;
import tamaized.voidscape.registry.ModEffects;
import tamaized.voidscape.registry.ModParticles;

public abstract class SpellBoltEntity extends AbstractArrow implements IEntityAdditionalSpawnData {

	private LivingEntity shootingEntity;
	private int particleColor;

	private int ticksInAir;
	private Vec3 startingPoint;

	protected double speed = 1D;
	protected float range = 32.0F;
	protected double maxRange = 0D;

	public SpellBoltEntity(EntityType<? extends SpellBoltEntity> type, Level level, int color) {
		super(type, level);
		startingPoint = position();
		pickup = Pickup.DISALLOWED;
		noCulling = true;
		this.particleColor = color;
	}

	public SpellBoltEntity(EntityType<? extends SpellBoltEntity> type, Level worldIn, LivingEntity shooter, int color) {
		this(type, worldIn, color);
		shootingEntity = shooter;
		setPos(shooter.getEyePosition().add(shooter.getLookAngle().normalize().scale(0.5D)));
		startingPoint = position();
		setTheVelocity(shooter.getLookAngle());
	}

	@SuppressWarnings("SuspiciousNameCombination")
	private void setTheVelocity(Vec3 dir) {
		setDeltaMovement(dir);
		if (this.xRotO == 0.0F && this.yRotO == 0.0F) {
			float f = Mth.sqrt((float) (dir.x * dir.x + dir.z * dir.z));
			this.setXRot((float) (Mth.atan2(dir.y, f) * (180D / Math.PI)));
			this.setYRot((float) (Mth.atan2(dir.x, dir.z) * (180D / Math.PI)));
			this.xRotO = this.getXRot();
			this.yRotO = this.getYRot();
			this.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), this.getXRot());
		}
	}

	@Override
	@Deprecated
	@SuppressWarnings("deprecation")
	public float getLightLevelDependentMagicValue() {
		return 1F;
	}

	@Override
	public void writeSpawnData(FriendlyByteBuf buffer) {
		buffer.writeDouble(getX());
		buffer.writeDouble(getY());
		buffer.writeDouble(getZ());
		buffer.writeFloat(range);
		buffer.writeDouble(speed);
		buffer.writeDouble(maxRange);
		buffer.writeInt(particleColor);
	}

	@Override
	public void readSpawnData(FriendlyByteBuf buffer) {
		moveTo(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
		range = buffer.readFloat();
		speed = buffer.readDouble();
		maxRange = buffer.readDouble();
		particleColor = buffer.readInt();
	}

	@Override
	public boolean isCritArrow() {
		return false;
	}

	@Override
	public void setCritArrow(boolean critical) {

	}

	@Override
	protected boolean canHitEntity(Entity entity) {
		return entity != shootingEntity && entity instanceof LivingEntity;
	}

	@Override
	public void tick() {
		// super.tick();
		baseTick();

		if (xRotO == 0.0F && yRotO == 0.0F) {
			float f = Mth.sqrt((float) (getDeltaMovement().x * getDeltaMovement().x + getDeltaMovement().z * getDeltaMovement().z));
			setYRot(yRotO = (float) (Math.atan2(getDeltaMovement().x, getDeltaMovement().z) * (180.0D / Math.PI)));
			setXRot(xRotO = (float) (Math.atan2(getDeltaMovement().y, f) * (180.0D / Math.PI)));
		}

		BlockPos blockpos = blockPosition();
		BlockState iblockstate = level().getBlockState(blockpos);

		if (!iblockstate.isAir()) {
			VoxelShape voxelshape = iblockstate.getCollisionShape(this.level(), blockpos);
			if (!voxelshape.isEmpty()) {
				for (AABB axisalignedbb : voxelshape.toAabbs()) {
					if (axisalignedbb.move(blockpos).contains(new Vec3(this.getX(), this.getY(), this.getZ()))) {
						onBlockHit(iblockstate, blockpos);
						remove(RemovalReason.DISCARDED);
						return;
					}
				}
			}
		}

		// Traveling
		++ticksInAir;
		if (maxRange > 0) {
			if (startingPoint.distanceTo(position()) >= maxRange)
				remove(RemovalReason.DISCARDED);
		} else if (ticksInAir > 20 * 5)
			remove(RemovalReason.DISCARDED);

		if (!level().isClientSide())
			for (Entity e : level().getEntities(this, getBoundingBox().inflate(1F, 1F, 1F))) {
				if (canHitEntity(e) && e instanceof LivingEntity living)
					onHit(living);
			}

		float f1 = 0.99F;
		float f4 = Mth.sqrt((float) (getDeltaMovement().x * getDeltaMovement().x + getDeltaMovement().z * getDeltaMovement().z));

		if (isInWater()) {
			for (int l = 0; l < 4; ++l) {
				f4 = 0.25F;
				level().addParticle(ParticleTypes.BUBBLE, getX() - getDeltaMovement().x * (double) f4, getY() - getDeltaMovement().y * (double) f4, getZ() - getDeltaMovement().z * (double) f4, getDeltaMovement().x, getDeltaMovement().y, getDeltaMovement().z);
			}
			f1 = 0.6F;
		}

		moveTo(getX() + getDeltaMovement().x * speed * f1, getY() + getDeltaMovement().y * speed * f1, getZ() + getDeltaMovement().z * speed * f1);
		setYRot((float) (Math.atan2(getDeltaMovement().x, getDeltaMovement().z) * (180.0D / Math.PI)));

		setXRot((float) (Mth.atan2(getDeltaMovement().y, (double) f4) * (180D / Math.PI)));
		while (getXRot() - xRotO < -180.0F) {
			xRotO -= 360.0F;
		}

		while (getXRot() - xRotO >= 180.0F) {
			xRotO += 360.0F;
		}

		while (getYRot() - yRotO < -180.0F) {
			yRotO -= 360.0F;
		}

		while (getYRot() - yRotO >= 180.0F) {
			yRotO += 360.0F;
		}

		setXRot(xRotO + (getXRot() - xRotO) * 0.2F);
		setYRot(yRotO + (getYRot() - yRotO) * 0.2F);

		if (isInWaterOrRain())
			clearFire();

		moveTo(getX(), getY(), getZ());
		checkInsideBlocks();
		if (level().isClientSide())
			level().addParticle(new ModParticles.ParticleSpellCloudData(particleColor), getX() - 0.3F + random.nextFloat() * 0.6F, getY() - 0.3F + random.nextFloat() * 0.6F, getZ() - 0.3F + random.nextFloat() * 0.6F, 0, 0, 0);
	}

	@Override
	protected void onHit(HitResult p_37260_) {
		// NO-OP - From ArrowEntity
	}

	protected void onHit(LivingEntity entity) {
		doPostHurtEffects(entity);
		remove(RemovalReason.DISCARDED);
	}

	protected void onBlockHit(BlockState state, BlockPos pos) {
		// NO-OP
	}

	@Override
	protected void doPostHurtEffects(LivingEntity entity) {
		// NO-OP
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {

	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {

	}

	@Override
	protected ItemStack getPickupItem() {
		return ItemStack.EMPTY;
	}

	@Override
	public Packet<ClientGamePacketListener> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

}
