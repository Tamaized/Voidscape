package tamaized.voidscape.entity.abilities;

import net.minecraft.block.BlockState;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerChunkProvider;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.registry.ModDamageSource;
import tamaized.voidscape.registry.ModEntities;
import tamaized.voidscape.registry.ModParticles;
import tamaized.voidscape.turmoil.SubCapability;
import tamaized.voidscape.turmoil.abilities.TurmoilAbility;
import tamaized.voidscape.world.InstanceChunkGenerator;

import javax.annotation.Nullable;

public class EntitySpellBolt extends AbstractArrowEntity implements IEntityAdditionalSpawnData {

	private static final DataParameter<Integer> SEEK_TARGET = EntityDataManager.defineId(EntitySpellBolt.class, DataSerializers.INT);
	private static final DataParameter<Integer> COLOR = EntityDataManager.defineId(EntitySpellBolt.class, DataSerializers.INT);

	public LivingEntity shootingEntity;
	private TurmoilAbility ability;
	private int ticksInAir;
	private boolean homing;
	private boolean burst;
	private boolean healing = false;
	private Entity seekTarget;

	private float damage = 1F;
	private double speed = 1D;
	private float range = 32.0F;
	private int maxRange = -1;
	private Vector3d startingPoint;

	public EntitySpellBolt(EntityType<? extends EntitySpellBolt> type, World level) {
		super(type, level);
		startingPoint = position();
		pickup = AbstractArrowEntity.PickupStatus.DISALLOWED;
		noCulling = true;
	}

	public EntitySpellBolt(LivingEntity shooter, TurmoilAbility ability) {
		this(ModEntities.SPELL_BOLT.get(), shooter.level, shooter, shooter.getX(), shooter.getY(), shooter.getZ());
		this.ability = ability;
	}

	public EntitySpellBolt(EntityType<? extends EntitySpellBolt> type, World worldIn, LivingEntity shooter, double x, double y, double z) {
		this(type, worldIn);
		shootingEntity = shooter;
		setPos(x, y + shooter.getEyeHeight(), z);
		startingPoint = position();
		Vector3d vec = shooter.getViewVector(1.0f);
		setTheVelocity(vec.x, vec.y, vec.z);
	}

	private void setTheVelocity(double x, double y, double z) {
		setDeltaMovement(x, y, z);
		if (this.xRotO == 0.0F && this.yRotO == 0.0F) {
			float f = MathHelper.sqrt(x * x + z * z);
			this.xRot = (float) (MathHelper.atan2(y, f) * (180D / Math.PI));
			this.yRot = (float) (MathHelper.atan2(x, z) * (180D / Math.PI));
			this.xRotO = this.xRot;
			this.yRotO = this.yRot;
			this.moveTo(this.getX(), this.getY(), this.getZ(), this.yRot, this.xRot);
		}
	}

	public EntitySpellBolt homing() {
		homing = true;
		return this;
	}

	public EntitySpellBolt burst() {
		burst = true;
		return this;
	}

	public EntitySpellBolt healing() {
		healing = true;
		return this;
	}

	@Override
	public float getBrightness() {
		return 1F;
	}

	public float getDamage() {
		return damage;
	}

	public void setDamage(float d) {
		damage = d;
	}

	@Override
	public void writeSpawnData(PacketBuffer buffer) {
		buffer.writeDouble(getX());
		buffer.writeDouble(getY());
		buffer.writeDouble(getZ());
		buffer.writeFloat(range);
		buffer.writeDouble(speed);
		buffer.writeFloat(damage);
	}

	@Override
	public void readSpawnData(PacketBuffer buffer) {
		moveTo(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
		range = buffer.readFloat();
		speed = buffer.readDouble();
		damage = buffer.readFloat();
	}

	@Override
	protected void defineSynchedData() {
		entityData.define(SEEK_TARGET, -1);
		entityData.define(COLOR, 0xFFFFFF);
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
		return (healing && entity instanceof MobEntity && ((MobEntity) entity).getMobType() == CreatureAttribute.UNDEAD) || !(level instanceof ServerWorld) || ((entity instanceof PlayerEntity) == healing) || !(((ServerChunkProvider) level.getChunkSource()).getGenerator() instanceof InstanceChunkGenerator);
	}

	public EntitySpellBolt color(int color) {
		entityData.set(COLOR, color);
		return this;
	}

	public int color() {
		return entityData.get(COLOR);
	}

	@Override
	public void tick() {
		// super.tick();
		baseTick();

		if (homing && (seekTarget == null || !seekTarget.isAlive())) {
			if (level.isClientSide) {
				int id = entityData.get(SEEK_TARGET);
				if (id > -1)
					seekTarget = level.getEntity(id);
			} else {
				Entity en = null;
				for (Entity e : level.getEntities(this, getBoundingBox().inflate(10F).move(getDeltaMovement().scale(11F)))) {
					if (!(e instanceof LivingEntity) && e == this || e == shootingEntity || !canHitEntity(e))
						continue;
					if (en == null || e.distanceTo(this) < en.distanceTo(this))
						en = e;
				}
				seekTarget = en;
				if (seekTarget != null)
					entityData.set(SEEK_TARGET, seekTarget.getId());
			}
		}

		if (seekTarget != null) {
			double scale = MathHelper.clamp(distanceTo(seekTarget) / 100D * 0.18D + 0.02D, 0.02D, 0.5D);
			Vector3d targetVec = new Vector3d(seekTarget.getX() - this.getX(), (seekTarget.getY() + seekTarget.getEyeHeight()) - this.getY(), seekTarget.getZ() - this.getZ()).scale(scale);
			Vector3d courseVec = getDeltaMovement();
			double courseLen = courseVec.length();
			double targetLen = targetVec.length();
			double totalLen = Math.sqrt(courseLen * courseLen + targetLen * targetLen);
			Vector3d newMotion = courseVec.scale(courseLen / totalLen).add(targetVec.scale(targetLen / totalLen));
			this.setDeltaMovement(newMotion.normalize());
		}

		if (xRotO == 0.0F && yRotO == 0.0F) {
			float f = MathHelper.sqrt(getDeltaMovement().x * getDeltaMovement().x + getDeltaMovement().z * getDeltaMovement().z);
			yRotO = yRot = (float) (Math.atan2(getDeltaMovement().x, getDeltaMovement().z) * (180.0D / Math.PI));
			xRotO = xRot = (float) (Math.atan2(getDeltaMovement().y, f) * (180.0D / Math.PI));
		}

		BlockPos blockpos = new BlockPos(getX(), getY(), getZ());
		BlockState iblockstate = level.getBlockState(blockpos);

		if (!iblockstate.isAir(this.level, blockpos)) {
			VoxelShape voxelshape = iblockstate.getCollisionShape(this.level, blockpos);
			if (!voxelshape.isEmpty()) {
				for (AxisAlignedBB axisalignedbb : voxelshape.toAabbs()) {
					if (axisalignedbb.move(blockpos).contains(new Vector3d(this.getX(), this.getY(), this.getZ()))) {
						blockHit(iblockstate, blockpos);
						remove();
						return;
					}
				}
			}
		}

		// Traveling
		++ticksInAir;
		if (maxRange >= 0) {
			if (startingPoint.distanceTo(position()) >= maxRange)
				remove();
		} else if (ticksInAir > 20 * 5)
			remove();

		if (!level.isClientSide())
			for (Entity e : level.getEntities(this, getBoundingBox().inflate(1F, 1F, 1F))) {
				if (e == this || (!healing && e == shootingEntity) || !canHitEntity(e))
					continue;
				if (burst)
					onBurst();
				else
					onHit(e);
			}

		float f1 = 0.99F;
		float f4 = MathHelper.sqrt(getDeltaMovement().x * getDeltaMovement().x + getDeltaMovement().z * getDeltaMovement().z);

		if (isInWater()) {
			for (int l = 0; l < 4; ++l) {
				f4 = 0.25F;
				level.addParticle(ParticleTypes.BUBBLE, getX() - getDeltaMovement().x * (double) f4, getY() - getDeltaMovement().y * (double) f4, getZ() - getDeltaMovement().z * (double) f4, getDeltaMovement().x, getDeltaMovement().y, getDeltaMovement().z);
			}
			f1 = 0.6F;
		}

		moveTo(getX() + getDeltaMovement().x * speed * f1, getY() + getDeltaMovement().y * speed * f1, getZ() + getDeltaMovement().z * speed * f1);
		yRot = (float) (Math.atan2(getDeltaMovement().x, getDeltaMovement().z) * (180.0D / Math.PI));

		xRot = (float) (MathHelper.atan2(getDeltaMovement().y, (double) f4) * (180D / Math.PI));
		while (xRot - xRotO < -180.0F) {
			xRotO -= 360.0F;
		}

		while (xRot - xRotO >= 180.0F) {
			xRotO += 360.0F;
		}

		while (yRot - yRotO < -180.0F) {
			yRotO -= 360.0F;
		}

		while (yRot - yRotO >= 180.0F) {
			yRotO += 360.0F;
		}

		xRot = xRotO + (xRot - xRotO) * 0.2F;
		yRot = yRotO + (yRot - yRotO) * 0.2F;

		if (isInWaterOrRain())
			clearFire();

		moveTo(getX(), getY(), getZ());
		checkInsideBlocks();
		if (level.isClientSide() && tickCount % 3 == 0)
			level.addParticle(ParticleTypes.WITCH, getX() - 0.3F + random.nextFloat() * 0.6F, getY() - 0.3F + random.nextFloat() * 0.6F, getZ() - 0.3F + random.nextFloat() * 0.6F, 0, -2, 0);
	}

	protected DamageSource getDamageSource(@Nullable LivingEntity attacker) {
		return attacker == null ? ModDamageSource.VOIDIC : ModDamageSource.VOIDIC_WITH_ENTITY.apply(attacker);
	}

	@Override
	protected void onHit(RayTraceResult raytraceResultIn) {
		// NO-OP
	}

	protected void onBurst() {
		int color = entityData.get(COLOR);
		if (level instanceof ServerWorld)
			for (int i = 0; i < 50; i++) {
				Vector3d vec = position().add(0, 1.25F + (random.nextFloat() - 0.5F), 0).add(new Vector3d(0.1D + random.nextDouble() * 2.9D, 0D, 0D).yRot((float) Math.toRadians(random.nextInt(360))));
				((ServerWorld) level).sendParticles(new ModParticles.ParticleSpellCloudData((color >> 16) & 0xFF, (color >> 8) & 0xFF, color & 0xFF), vec.x, vec.y, vec.z, 0, 0, 0, 0, 1F);
			}
		level.getEntities(this, getBoundingBox().inflate(4F)).forEach(entity -> {
			if (entity != this && (healing || entity != shootingEntity) && canHitEntity(entity))
				onHit(entity);
		});
		remove();
	}

	protected void onHit(Entity entity) {
		DamageSource damagesource = getDamageSource(shootingEntity);

		if (entity instanceof LivingEntity && (((!healing || (entity instanceof MobEntity && ((MobEntity) entity).getMobType() == CreatureAttribute.UNDEAD)) && entity.hurt(damagesource, damage)) || (healing && Voidscape.healTargetAndAggro((LivingEntity) entity, shootingEntity, damage)))) {
			LivingEntity entitylivingbase = (LivingEntity) entity;
			if (!healing)
				doPostHurtEffects(entitylivingbase);
			remove();
		} else {
			setDeltaMovement(getDeltaMovement().x * -0.10000000149011612D, getDeltaMovement().y * -0.10000000149011612D, getDeltaMovement().z * -0.10000000149011612D);
			yRot += 180.0F;
			yRotO += 180.0F;
			ticksInAir = 0;

			if (!level.isClientSide() && getDeltaMovement().x * getDeltaMovement().x + getDeltaMovement().y * getDeltaMovement().y + getDeltaMovement().z * getDeltaMovement().z < 0.0010000000474974513D) {
				remove();
			}
		}
	}

	protected void blockHit(BlockState state, BlockPos pos) {
		if (burst)
			onBurst();
	}

	@Override
	protected void doPostHurtEffects(LivingEntity p_184548_1_) {
		if (shootingEntity != null && ability != null)
			shootingEntity.getCapability(SubCapability.CAPABILITY).ifPresent(cap -> cap.get(Voidscape.subCapTurmoilStats).ifPresent(stats -> {
				stats.increasePowerFromAbilityDamage(shootingEntity, p_184548_1_, ability);
			}));
	}

	@Override
	public void readAdditionalSaveData(CompoundNBT compound) {

	}

	@Override
	public void addAdditionalSaveData(CompoundNBT compound) {

	}

	@Override
	protected ItemStack getPickupItem() {
		return ItemStack.EMPTY;
	}

	@Override
	public IPacket<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}
