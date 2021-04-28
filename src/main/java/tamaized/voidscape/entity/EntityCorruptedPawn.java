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

import javax.annotation.Nullable;

public abstract class EntityCorruptedPawn extends MobEntity {

	private static final DataParameter<Integer> TENTACLES = EntityDataManager.defineId(EntityCorruptedPawn.class, DataSerializers.INT);
	private static final DataParameter<Boolean> CASTING = EntityDataManager.defineId(EntityCorruptedPawn.class, DataSerializers.BOOLEAN);

	public int lastTentacleState;
	public long[] tentacleTimes = new long[8];

	public long castTick;
	private boolean lastCast;

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
	}

	public boolean isCasting() {
		return entityData.get(CASTING);
	}

	protected void markCasting(boolean cast) {
		entityData.set(CASTING, cast);
	}

	/**
	 * Tentacles are represented by each bit, ordered from the left most bit down, the Tentacle order starts from the very top and goes around clockwise<p /><pre>
	 * 8
	 * 1 7
	 * 2   6
	 * 3 5
	 * 4</pre>
	 */
	public int getTentacleBits() {
		return entityData.get(TENTACLES);
	}

	protected void setTentacleBits(int bits) {
		entityData.set(TENTACLES, bits & 0b11111111);
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
