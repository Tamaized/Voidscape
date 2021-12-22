package tamaized.voidscape.entity;

import net.minecraft.core.NonNullList;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import tamaized.voidscape.registry.ModDataSerializers;

import javax.annotation.Nullable;

public abstract class EntityCorruptedPawn extends Mob {

	private static final EntityDataAccessor<Integer> TENTACLES = SynchedEntityData.defineId(EntityCorruptedPawn.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Boolean> CASTING = SynchedEntityData.defineId(EntityCorruptedPawn.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Integer> RAYS = SynchedEntityData.defineId(EntityCorruptedPawn.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> RAY_TARGET = SynchedEntityData.defineId(EntityCorruptedPawn.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Long> RAY_START = SynchedEntityData.defineId(EntityCorruptedPawn.class, ModDataSerializers.LONG);
	private static final EntityDataAccessor<Long> RAY_END = SynchedEntityData.defineId(EntityCorruptedPawn.class, ModDataSerializers.LONG);

	public int lastTentacleState;
	public long[] tentacleTimes = new long[8];

	public long castTick;
	private boolean lastCast;

	private Entity targetCache;

	protected EntityCorruptedPawn(EntityType<? extends EntityCorruptedPawn> p_i48577_1_, Level p_i48577_2_) {
		super(p_i48577_1_, p_i48577_2_);
	}

	@Override
	public void lookAt(Entity entityIn, float maxYawIncrease, float maxPitchIncrease) {
		super.lookAt(entityIn, maxYawIncrease, maxPitchIncrease);
		setYHeadRot(getYRot());
	}

	@Override
	public void tick() {
		if (level.isClientSide()) {
			if (lastCast != isCasting()) {
				castTick = tickCount;
				lastCast = isCasting();
			}
			for (int i = 0; i < tentacleTimes.length; i++) {
				int shift = 7 - i;
				int cbit = (getTentacleBits() >> shift) & 0b1;
				int obit = (lastTentacleState >> shift) & 0b1;
				if (cbit != obit)
					tentacleTimes[i] = tickCount + 20 * 5;
			}
			lastTentacleState = getTentacleBits();
		}
		super.tick();
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		entityData.define(TENTACLES, 0b00000000);
		entityData.define(CASTING, false);
		entityData.define(RAYS, 0b000000000);
		entityData.define(RAY_TARGET, 0);
		entityData.define(RAY_START, 0L);
		entityData.define(RAY_END, 0L);
	}

	public boolean isCasting() {
		return entityData.get(CASTING);
	}

	public void markCasting(boolean cast) {
		entityData.set(CASTING, cast);
	}

	/**
	 * Tentacles are represented by each bit, ordered from the left most bit down, the Tentacle order starts from the very top and goes around clockwise<p /><pre>
	 *   8
	 *  1 7
	 * 2   6
	 *  3 5
	 *   4</pre>
	 */
	public int getTentacleBits() {
		return entityData.get(TENTACLES);
	}

	protected void setTentacleBits(int bits) {
		entityData.set(TENTACLES, bits & 0b11111111);
	}

	/**
	 * Ray bits are the same as {@link #getTentacleBits()} except the 9th bit is head ray
	 */
	public int getRayBits() {
		return entityData.get(RAYS);
	}

	public void setRayBits(int bits) {
		entityData.set(RAYS, bits & 0b111111111);
	}

	@Nullable
	public Entity getRayTarget() {
		final int id = entityData.get(RAY_TARGET);
		if (targetCache == null || targetCache.getId() != id)
			targetCache = id >= 0 ? level.getEntity(id) : null;
		return targetCache;
	}

	public void setRayTarget(@Nullable Entity entity) {
		entityData.set(RAY_TARGET, entity == null ? -1 : entity.getId());
	}

	public long getRayStart() {
		return entityData.get(RAY_START);
	}

	public void updateRayStart() {
		entityData.set(RAY_START, level.getGameTime());
	}

	public long getRayEnd() {
		return entityData.get(RAY_END);
	}

	public void updateRayEnd(long len) {
		entityData.set(RAY_END, level.getGameTime() + len);
	}

	public void disableTentacles(int bits) {
		setTentacleBits(getTentacleBits() | bits);
	}

	public void enableTentacles(int bits) {
		setTentacleBits(~((~getTentacleBits()) | bits));
	}

	public boolean shouldRender(@Nullable Player player) {
		return true;
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
	public void knockback(double p_147241_, double p_147242_, double p_147243_) {

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
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	public void checkDespawn() {
	}

}
