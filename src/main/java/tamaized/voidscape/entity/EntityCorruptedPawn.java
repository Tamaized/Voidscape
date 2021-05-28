package tamaized.voidscape.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.HandSide;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import tamaized.voidscape.registry.ModDataSerializers;

import javax.annotation.Nullable;

public abstract class EntityCorruptedPawn extends MobEntity {

	private static final DataParameter<Integer> TENTACLES = EntityDataManager.defineId(EntityCorruptedPawn.class, DataSerializers.INT);
	private static final DataParameter<Boolean> CASTING = EntityDataManager.defineId(EntityCorruptedPawn.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Integer> RAYS = EntityDataManager.defineId(EntityCorruptedPawn.class, DataSerializers.INT);
	private static final DataParameter<Integer> RAY_TARGET = EntityDataManager.defineId(EntityCorruptedPawn.class, DataSerializers.INT);
	private static final DataParameter<Long> RAY_START = EntityDataManager.defineId(EntityCorruptedPawn.class, ModDataSerializers.LONG);
	private static final DataParameter<Long> RAY_END = EntityDataManager.defineId(EntityCorruptedPawn.class, ModDataSerializers.LONG);

	public int lastTentacleState;
	public long[] tentacleTimes = new long[8];

	public long castTick;
	private boolean lastCast;

	private Entity targetCache;

	protected EntityCorruptedPawn(EntityType<? extends EntityCorruptedPawn> p_i48577_1_, World p_i48577_2_) {
		super(p_i48577_1_, p_i48577_2_);
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

	protected void markCasting(boolean cast) {
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

	protected void setRayBits(int bits) {
		entityData.set(RAYS, bits & 0b111111111);
	}

	@Nullable
	public Entity getRayTarget() {
		final int id = entityData.get(RAY_TARGET);
		if (targetCache == null || targetCache.getId() != id)
			targetCache = id >= 0 ? level.getEntity(id) : null;
		return targetCache;
	}

	protected void setRayTarget(@Nullable Entity entity) {
		entityData.set(RAY_TARGET, entity == null ? -1 : entity.getId());
	}

	public long getRayStart() {
		return entityData.get(RAY_START);
	}

	protected void updateRayStart() {
		entityData.set(RAY_START, level.getGameTime());
	}

	public long getRayEnd() {
		return entityData.get(RAY_END);
	}

	protected void updateRayEnd(long len) {
		entityData.set(RAY_END, level.getGameTime() + len);
	}

	protected void disableTentacles(int bits) {
		setTentacleBits(getTentacleBits() | bits);
	}

	protected void enableTentacles(int bits) {
		setTentacleBits(~((~getTentacleBits()) | bits));
	}

	public boolean shouldRender(@Nullable PlayerEntity player) {
		return true;
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
	public void knockback(float p_233627_1_, double p_233627_2_, double p_233627_4_) {

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
	public IPacket<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	public void checkDespawn() {
	}

}
