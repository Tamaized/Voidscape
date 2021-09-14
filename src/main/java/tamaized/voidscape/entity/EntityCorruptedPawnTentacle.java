package tamaized.voidscape.entity;

import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fmllegacy.network.NetworkHooks;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.registry.ModDamageSource;
import tamaized.voidscape.registry.ModDataSerializers;
import tamaized.voidscape.registry.ModEntities;
import tamaized.voidscape.turmoil.SubCapability;

import javax.annotation.Nullable;

public class EntityCorruptedPawnTentacle extends LivingEntity {

	private static final EntityDataAccessor<Boolean> BINDING = SynchedEntityData.defineId(EntityCorruptedPawnTentacle.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> FALLING = SynchedEntityData.defineId(EntityCorruptedPawnTentacle.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Long> EXPLOSION_DURATION = SynchedEntityData.defineId(EntityCorruptedPawnTentacle.class, ModDataSerializers.LONG);
	private final EntityCorruptedPawnBoss parent;
	private final Vec3 startingPos;
	private int deathTicks = 0;
	private Entity bindTarget;
	private long explosionTimer = -1;
	private float explosionDamage;

	public EntityCorruptedPawnTentacle(EntityType<? extends EntityCorruptedPawnTentacle> type, Level level) {
		this(type, level, null, Vec3.ZERO);
	}

	public EntityCorruptedPawnTentacle(Level level, @Nullable EntityCorruptedPawnBoss pawn, Vec3 pos) {
		this(ModEntities.CORRUPTED_PAWN_TENTACLE.get(), level, pawn, pos);
	}

	public EntityCorruptedPawnTentacle(EntityType<? extends EntityCorruptedPawnTentacle> type, Level level, @Nullable EntityCorruptedPawnBoss pawn, Vec3 pos) {
		super(type, level);
		setNoGravity(true);
		parent = pawn;
		startingPos = new Vec3(pos.x(), pos.y() + 20F, pos.z());
		moveTo(startingPos);
	}

	public EntityCorruptedPawnTentacle withHealth(float amount) {
		getAttribute(Attributes.MAX_HEALTH).setBaseValue(amount);
		setHealth(amount);
		return this;
	}

	public EntityCorruptedPawnTentacle explodes(long timer, float damage) {
		explosionTimer = timer;
		entityData.set(EXPLOSION_DURATION, timer);
		explosionDamage = damage;
		return this;
	}

	public double getExplosionTimer() {
		return explosionTimer;
	}

	public int getDeathTicks() {
		return deathTicks;
	}

	@Override
	public boolean isPickable() {
		return true;
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		return super.hurt(source, ModDamageSource.check(ModDamageSource.ID_VOIDIC, source) ? amount : amount * 0.01F);
	}

	@Override
	public Iterable<ItemStack> getArmorSlots() {
		return NonNullList.create();
	}

	@Override
	public ItemStack getItemBySlot(EquipmentSlot equipmentSlotType) {
		return ItemStack.EMPTY;
	}

	@Override
	public void setItemSlot(EquipmentSlot equipmentSlotType, ItemStack itemStack) {

	}

	@Override
	public HumanoidArm getMainArm() {
		return HumanoidArm.RIGHT;
	}

	@Override
	protected void tickDeath() {
		if (level.isClientSide()) {
			for (int i = 0; i < 20; ++i) {
				double d0 = this.random.nextGaussian() * 0.02D;
				double d1 = this.random.nextGaussian() * 0.02D;
				double d2 = this.random.nextGaussian() * 0.02D;
				this.level.addParticle(ParticleTypes.POOF, this.getRandomX(1.0D), this.getRandomY(), this.getRandomZ(1.0D), d0, d1, d2);
			}
			if (deathTicks >= 20 * 5)
				remove(RemovalReason.DISCARDED);
		}
	}

	@Override
	public void tick() {
		if (!level.isClientSide())
			setDeltaMovement(Vec3.ZERO);
		else if (!falling())
			explosionTimer++;
		if (getHealth() <= 0)
			deathTicks++;
		else if (!level.isClientSide()) {
			if (binding()) {
				if (bindTarget == null || !bindTarget.isAlive())
					remove(RemovalReason.DISCARDED);
				else {
					moveTo(bindTarget.position());
					bindTarget.getCapability(SubCapability.CAPABILITY).ifPresent(cap -> cap.get(Voidscape.subCapBind).ifPresent(bind -> bind.bind(true)));
					if (explosionDamage > 0 && explosionTimer >= 0) {
						if (explosionTimer == 0) {
							level.playSound(null, blockPosition(), SoundEvents.GENERIC_EXPLODE, SoundSource.HOSTILE, 4F, 0.25F + random.nextFloat() * 0.5F);
							if (level instanceof ServerLevel)
								for (int i = 0; i < 25; i++)
									((ServerLevel) level).sendParticles(ParticleTypes.EXPLOSION, getRandomX(1F), getRandomY(), getRandomZ(1F), 0, 0, 0, 0, 0);
							bindTarget.hurt(ModDamageSource.VOIDIC_WITH_ENTITY.apply(parent), explosionDamage);
							remove(RemovalReason.DISCARDED);
						}
						explosionTimer--;
					}
				}
			} else if (position().y() > startingPos.y() - 20F) {
				moveTo(position().subtract(0F, Math.min(0.1F, position().y() - (startingPos.y() - 20F)), 0F));
			} else if (falling()) {
				entityData.set(FALLING, false);
			} else if (explosionTimer >= 0) {
				if (explosionTimer == 0) {
					level.playSound(null, blockPosition(), SoundEvents.GENERIC_EXPLODE, SoundSource.HOSTILE, 4F, 0.25F + random.nextFloat() * 0.5F);
					if (level instanceof ServerLevel)
						for (int i = 0; i < 100; i++)
							((ServerLevel) level).sendParticles(ParticleTypes.EXPLOSION, getRandomX(10F), getRandomY(), getRandomZ(10F), 0, 0, 0, 0, 0);
					level.getEntitiesOfClass(Player.class, getBoundingBox().inflate(50F)).forEach(p -> p.hurt(ModDamageSource.VOIDIC_WITH_ENTITY.apply(parent), explosionDamage));
					remove(RemovalReason.DISCARDED);
				}
				explosionTimer--;
			}
			if (/*parent == null || !parent.isAlive() || */deathTicks >= 20 * 5) {
				remove(RemovalReason.DISCARDED);
			}
		}
		super.tick();
	}

	@Override
	protected void defineSynchedData() {
		entityData.define(BINDING, false);
		entityData.define(FALLING, true);
		entityData.define(EXPLOSION_DURATION, -1L);
		super.defineSynchedData();
	}

	public EntityCorruptedPawnTentacle markBinding(Entity target) {
		entityData.set(BINDING, true);
		entityData.set(FALLING, false);
		bindTarget = target;
		return this;
	}

	public boolean binding() {
		return entityData.get(BINDING);
	}

	public boolean falling() {
		return entityData.get(FALLING);
	}

	public double getExplosionDuration() {
		return entityData.get(EXPLOSION_DURATION);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {

	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {

	}

	@Override
	public boolean canBeCollidedWith() {
		return false;
	}

	@Override
	public boolean canCollideWith(Entity p_241849_1_) {
		return false;
	}

	@Override
	public void checkDespawn() {
	}

	@Override
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}
