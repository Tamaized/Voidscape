package tamaized.voidscape.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.network.NetworkHooks;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.registry.ModDamageSource;
import tamaized.voidscape.registry.ModEntities;
import tamaized.voidscape.turmoil.SubCapability;

import javax.annotation.Nullable;

public class EntityCorruptedPawnTentacle extends Entity {

	private static final DataParameter<Boolean> BINDING = EntityDataManager.defineId(EntityCorruptedPawnTentacle.class, DataSerializers.BOOLEAN);
	private final EntityCorruptedPawnBoss parent;
	private final Vector3d startingPos;
	private float health = 20F;
	private int deathTicks = 0;
	private Entity bindTarget;
	private long explosionTimer = -1;
	private float explosionDamage;

	public EntityCorruptedPawnTentacle(EntityType type, World level) {
		this(type, level, null, Vector3d.ZERO);
		ObfuscationReflectionHelper.getPrivateValue(AxeItem.class, null, "field_203176_a");
	}

	public EntityCorruptedPawnTentacle(World level, @Nullable EntityCorruptedPawnBoss pawn, Vector3d pos) {
		this(ModEntities.CORRUPTED_PAWN_TENTACLE.get(), level, pawn, pos);
	}

	public EntityCorruptedPawnTentacle(EntityType type, World level, @Nullable EntityCorruptedPawnBoss pawn, Vector3d pos) {
		super(type, level);
		parent = pawn;
		startingPos = new Vector3d(pos.x(), pos.y() + 20F, pos.z());
		moveTo(pos);
	}

	public EntityCorruptedPawnTentacle explodes(long timer, float damage) {
		explosionTimer = timer;
		explosionDamage = damage;
		return this;
	}

	public float getHealth() {
		return health;
	}

	public EntityCorruptedPawnTentacle setHealth(float hp) {
		health = hp;
		return this;
	}

	public int getDeathTicks() {
		return deathTicks;
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		if (health <= 0 || isInvulnerable() || !ModDamageSource.check(ModDamageSource.ID_VOIDIC, source))
			return false;
		health -= amount;
		return true;
	}

	@Override
	public void tick() {
		if (health <= 0)
			deathTicks++;
		else if (!level.isClientSide()) {
			if (binding()) {
				if (bindTarget == null || !bindTarget.isAlive())
					remove();
				else {
					moveTo(bindTarget.position());
					bindTarget.getCapability(SubCapability.CAPABILITY).ifPresent(cap -> cap.get(Voidscape.subCapBind).ifPresent(bind -> bind.bound = true));
				}
			} else if (position().y() > startingPos.y() - 20F) {
				moveTo(position().subtract(0F, Math.min(0.2F, position().y() - (startingPos.y() - 20F)), 0F));
				xRot++;
			}
			if (explosionTimer >= 0) {
				if (explosionTimer == 0) {
					level.getEntitiesOfClass(PlayerEntity.class, getBoundingBox().inflate(50F)).forEach(p -> p.hurt(ModDamageSource.VOIDIC_WITH_ENTITY.apply(parent), explosionDamage));
					if (level instanceof ServerWorld)
						for (int i = 0; i < 25; i++)
							((ServerWorld) level).sendParticles(ParticleTypes.EXPLOSION_EMITTER, getX() + random.nextFloat() * 20F, getY() + 2F, getZ() * 20F, 0, 0, 0, 0, 0);
				}
				explosionTimer--;

			}
		}
		if (parent == null || !parent.isAlive() || deathTicks >= 20 * 5)
			remove();
		super.tick();
	}

	@Override
	protected void defineSynchedData() {
		entityData.define(BINDING, false);
	}

	public EntityCorruptedPawnTentacle markBinding(Entity target) {
		entityData.set(BINDING, true);
		bindTarget = target;
		return this;
	}

	public boolean binding() {
		return entityData.get(BINDING);
	}

	@Override
	protected void readAdditionalSaveData(CompoundNBT compound) {

	}

	@Override
	protected void addAdditionalSaveData(CompoundNBT compound) {

	}

	@Override
	public IPacket<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}
