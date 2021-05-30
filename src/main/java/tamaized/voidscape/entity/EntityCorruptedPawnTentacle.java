package tamaized.voidscape.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.HandSide;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.network.NetworkHooks;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.registry.ModDamageSource;
import tamaized.voidscape.registry.ModDataSerializers;
import tamaized.voidscape.registry.ModEntities;
import tamaized.voidscape.turmoil.SubCapability;

import javax.annotation.Nullable;

public class EntityCorruptedPawnTentacle extends LivingEntity {

	private static final DataParameter<Boolean> BINDING = EntityDataManager.defineId(EntityCorruptedPawnTentacle.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> FALLING = EntityDataManager.defineId(EntityCorruptedPawnTentacle.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Long> EXPLOSION_DURATION = EntityDataManager.defineId(EntityCorruptedPawnTentacle.class, ModDataSerializers.LONG);
	private final EntityCorruptedPawnBoss parent;
	private final Vector3d startingPos;
	private int deathTicks = 0;
	private Entity bindTarget;
	private long explosionTimer = -1;
	private float explosionDamage;

	public EntityCorruptedPawnTentacle(EntityType<? extends EntityCorruptedPawnTentacle> type, World level) {
		this(type, level, null, Vector3d.ZERO);
		ObfuscationReflectionHelper.getPrivateValue(AxeItem.class, null, "field_203176_a");
	}

	public EntityCorruptedPawnTentacle(World level, @Nullable EntityCorruptedPawnBoss pawn, Vector3d pos) {
		this(ModEntities.CORRUPTED_PAWN_TENTACLE.get(), level, pawn, pos);
	}

	public EntityCorruptedPawnTentacle(EntityType<? extends EntityCorruptedPawnTentacle> type, World level, @Nullable EntityCorruptedPawnBoss pawn, Vector3d pos) {
		super(type, level);
		setNoGravity(true);
		parent = pawn;
		startingPos = new Vector3d(pos.x(), pos.y() + 20F, pos.z());
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
	public ItemStack getItemBySlot(EquipmentSlotType equipmentSlotType) {
		return ItemStack.EMPTY;
	}

	@Override
	public void setItemSlot(EquipmentSlotType equipmentSlotType, ItemStack itemStack) {

	}

	@Override
	public HandSide getMainArm() {
		return HandSide.RIGHT;
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
				remove();
		}
	}

	@Override
	public void tick() {
		if (!level.isClientSide())
			setDeltaMovement(Vector3d.ZERO);
		else if (!falling())
			explosionTimer++;
		if (getHealth() <= 0)
			deathTicks++;
		else if (!level.isClientSide()) {
			if (binding()) {
				if (bindTarget == null || !bindTarget.isAlive())
					remove();
				else {
					moveTo(bindTarget.position());
					bindTarget.getCapability(SubCapability.CAPABILITY).ifPresent(cap -> cap.get(Voidscape.subCapBind).ifPresent(bind -> bind.bind(true)));
					if (explosionDamage > 0 && explosionTimer >= 0) {
						if (explosionTimer == 0) {
							level.playSound(null, blockPosition(), SoundEvents.GENERIC_EXPLODE, SoundCategory.HOSTILE, 4F, 0.25F + random.nextFloat() * 0.5F);
							if (level instanceof ServerWorld)
								for (int i = 0; i < 25; i++)
									((ServerWorld) level).sendParticles(ParticleTypes.EXPLOSION, getRandomX(1F), getRandomY(), getRandomZ(1F), 0, 0, 0, 0, 0);
							bindTarget.hurt(ModDamageSource.VOIDIC_WITH_ENTITY.apply(parent), explosionDamage);
							remove();
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
					level.playSound(null, blockPosition(), SoundEvents.GENERIC_EXPLODE, SoundCategory.HOSTILE, 4F, 0.25F + random.nextFloat() * 0.5F);
					if (level instanceof ServerWorld)
						for (int i = 0; i < 100; i++)
							((ServerWorld) level).sendParticles(ParticleTypes.EXPLOSION, getRandomX(10F), getRandomY(), getRandomZ(10F), 0, 0, 0, 0, 0);
					level.getEntitiesOfClass(PlayerEntity.class, getBoundingBox().inflate(50F)).forEach(p -> p.hurt(ModDamageSource.VOIDIC_WITH_ENTITY.apply(parent), explosionDamage));
					remove();
				}
				explosionTimer--;
			}
			if (/*parent == null || !parent.isAlive() || */deathTicks >= 20 * 5) {
				remove();
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
	public void readAdditionalSaveData(CompoundNBT compound) {

	}

	@Override
	public void addAdditionalSaveData(CompoundNBT compound) {

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
	public IPacket<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}
