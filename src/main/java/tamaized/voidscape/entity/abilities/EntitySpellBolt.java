package tamaized.voidscape.entity.abilities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.player.Player;
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
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.registry.ModDamageSource;
import tamaized.voidscape.registry.ModEntities;
import tamaized.voidscape.registry.ModParticles;
import tamaized.voidscape.turmoil.SubCapability;
import tamaized.voidscape.turmoil.abilities.TurmoilAbility;
import tamaized.voidscape.world.InstanceChunkGenerator;

import javax.annotation.Nullable;

public class EntitySpellBolt extends AbstractArrow implements IEntityAdditionalSpawnData {

	private static final EntityDataAccessor<Integer> SEEK_TARGET = SynchedEntityData.defineId(EntitySpellBolt.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> COLOR = SynchedEntityData.defineId(EntitySpellBolt.class, EntityDataSerializers.INT);

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
	private Vec3 startingPoint;

	public EntitySpellBolt(EntityType<? extends EntitySpellBolt> type, Level level) {
		super(type, level);
		startingPoint = position();
		pickup = AbstractArrow.Pickup.DISALLOWED;
		noCulling = true;
	}

	public EntitySpellBolt(LivingEntity shooter, TurmoilAbility ability) {
		this(ModEntities.SPELL_BOLT.get(), shooter.level, shooter, shooter.getX(), shooter.getY(), shooter.getZ());
		this.ability = ability;
	}

	public EntitySpellBolt(EntityType<? extends EntitySpellBolt> type, Level worldIn, LivingEntity shooter, double x, double y, double z) {
		this(type, worldIn);
		shootingEntity = shooter;
		setPos(x, y + shooter.getEyeHeight(), z);
		startingPoint = position();
		Vec3 vec = shooter.getViewVector(1.0f);
		setTheVelocity(vec.x, vec.y, vec.z);
	}

	private void setTheVelocity(double x, double y, double z) {
		setDeltaMovement(x, y, z);
		if (this.xRotO == 0.0F && this.yRotO == 0.0F) {
			float f = Mth.sqrt((float) (x * x + z * z));
			this.setXRot((float) (Mth.atan2(y, f) * (180D / Math.PI)));
			this.setYRot((float) (Mth.atan2(x, z) * (180D / Math.PI)));
			this.xRotO = this.getXRot();
			this.yRotO = this.getYRot();
			this.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), this.getXRot());
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
	public float getLightLevelDependentMagicValue() {
		return 1F;
	}

	public float getDamage() {
		return damage;
	}

	public void setDamage(float d) {
		damage = d;
	}

	@Override
	public void writeSpawnData(FriendlyByteBuf buffer) {
		buffer.writeDouble(getX());
		buffer.writeDouble(getY());
		buffer.writeDouble(getZ());
		buffer.writeFloat(range);
		buffer.writeDouble(speed);
		buffer.writeFloat(damage);
	}

	@Override
	public void readSpawnData(FriendlyByteBuf buffer) {
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

	public static boolean canHitEntity(Level level, Entity entity, boolean healing) {
		return (healing && entity instanceof Mob && ((Mob) entity).getMobType() == MobType.UNDEAD) || !(level instanceof ServerLevel) || ((entity instanceof Player) == healing && !entity.isSpectator() && entity.isAlive() && entity.isPickable()) || !(((ServerChunkCache) level.getChunkSource()).getGenerator() instanceof InstanceChunkGenerator);
	}

	@Override
	protected boolean canHitEntity(Entity entity) {
		return EntitySpellBolt.canHitEntity(level, entity, healing);
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
			double scale = Mth.clamp(distanceTo(seekTarget) / 100D * 0.18D + 0.02D, 0.02D, 0.5D);
			Vec3 targetVec = new Vec3(seekTarget.getX() - this.getX(), (seekTarget.getY() + seekTarget.getEyeHeight()) - this.getY(), seekTarget.getZ() - this.getZ()).scale(scale);
			Vec3 courseVec = getDeltaMovement();
			double courseLen = courseVec.length();
			double targetLen = targetVec.length();
			double totalLen = Math.sqrt(courseLen * courseLen + targetLen * targetLen);
			Vec3 newMotion = courseVec.scale(courseLen / totalLen).add(targetVec.scale(targetLen / totalLen));
			this.setDeltaMovement(newMotion.normalize());
		}

		if (xRotO == 0.0F && yRotO == 0.0F) {
			float f = Mth.sqrt((float) (getDeltaMovement().x * getDeltaMovement().x + getDeltaMovement().z * getDeltaMovement().z));
			setYRot(yRotO = (float) (Math.atan2(getDeltaMovement().x, getDeltaMovement().z) * (180.0D / Math.PI)));
			setXRot(xRotO = (float) (Math.atan2(getDeltaMovement().y, f) * (180.0D / Math.PI)));
		}

		BlockPos blockpos = new BlockPos(getX(), getY(), getZ());
		BlockState iblockstate = level.getBlockState(blockpos);

		if (!iblockstate.isAir()) {
			VoxelShape voxelshape = iblockstate.getCollisionShape(this.level, blockpos);
			if (!voxelshape.isEmpty()) {
				for (AABB axisalignedbb : voxelshape.toAabbs()) {
					if (axisalignedbb.move(blockpos).contains(new Vec3(this.getX(), this.getY(), this.getZ()))) {
						blockHit(iblockstate, blockpos);
						remove(RemovalReason.DISCARDED);
						return;
					}
				}
			}
		}

		// Traveling
		++ticksInAir;
		if (maxRange >= 0) {
			if (startingPoint.distanceTo(position()) >= maxRange)
				remove(RemovalReason.DISCARDED);
		} else if (ticksInAir > 20 * 5)
			remove(RemovalReason.DISCARDED);

		if (!level.isClientSide())
			for (Entity e : level.getEntities(this, getBoundingBox().inflate(1F, 1F, 1F))) {
				if (e == this || e == shootingEntity || !canHitEntity(e))
					continue;
				if (burst)
					onBurst();
				else
					onHit(e);
			}

		float f1 = 0.99F;
		float f4 = Mth.sqrt((float) (getDeltaMovement().x * getDeltaMovement().x + getDeltaMovement().z * getDeltaMovement().z));

		if (isInWater()) {
			for (int l = 0; l < 4; ++l) {
				f4 = 0.25F;
				level.addParticle(ParticleTypes.BUBBLE, getX() - getDeltaMovement().x * (double) f4, getY() - getDeltaMovement().y * (double) f4, getZ() - getDeltaMovement().z * (double) f4, getDeltaMovement().x, getDeltaMovement().y, getDeltaMovement().z);
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
		if (level.isClientSide() && tickCount % 3 == 0)
			level.addParticle(ParticleTypes.WITCH, getX() - 0.3F + random.nextFloat() * 0.6F, getY() - 0.3F + random.nextFloat() * 0.6F, getZ() - 0.3F + random.nextFloat() * 0.6F, 0, -2, 0);
	}

	protected DamageSource getDamageSource(@Nullable LivingEntity attacker) {
		return attacker == null ? ModDamageSource.VOIDIC : ModDamageSource.VOIDIC_WITH_ENTITY.apply(attacker);
	}

	@Override
	protected void onHit(HitResult p_37260_) {
		// NO-OP
	}

	protected void onBurst() {
		int color = entityData.get(COLOR);
		if (level instanceof ServerLevel)
			for (int i = 0; i < 50; i++) {
				Vec3 vec = position().add(0, 1.25F + (random.nextFloat() - 0.5F), 0).add(new Vec3(0.1D + random.nextDouble() * 2.9D, 0D, 0D).yRot((float) Math.toRadians(random.nextInt(360))));
				((ServerLevel) level).sendParticles(new ModParticles.ParticleSpellCloudData((color >> 16) & 0xFF, (color >> 8) & 0xFF, color & 0xFF), vec.x, vec.y, vec.z, 0, 0, 0, 0, 1F);
			}
		level.getEntities(this, getBoundingBox().inflate(4F)).forEach(entity -> {
			if (entity != this && (healing || entity != shootingEntity) && canHitEntity(entity))
				onHit(entity);
		});
		remove(RemovalReason.DISCARDED);
	}

	protected void onHit(Entity entity) {
		DamageSource damagesource = getDamageSource(shootingEntity);

		if (entity instanceof LivingEntity && (((!healing || (entity instanceof Mob && ((Mob) entity).getMobType() == MobType.UNDEAD)) && entity.hurt(damagesource, damage)) || (healing && Voidscape.healTargetAndAggro((LivingEntity) entity, shootingEntity, damage)))) {
			LivingEntity entitylivingbase = (LivingEntity) entity;
			if (!healing)
				doPostHurtEffects(entitylivingbase);
			remove(RemovalReason.DISCARDED);
		} else {
			setDeltaMovement(getDeltaMovement().x * -0.10000000149011612D, getDeltaMovement().y * -0.10000000149011612D, getDeltaMovement().z * -0.10000000149011612D);
			setYRot(getYRot() + 180.0F);
			yRotO += 180.0F;
			ticksInAir = 0;

			if (!level.isClientSide() && getDeltaMovement().x * getDeltaMovement().x + getDeltaMovement().y * getDeltaMovement().y + getDeltaMovement().z * getDeltaMovement().z < 0.0010000000474974513D) {
				remove(RemovalReason.DISCARDED);
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
