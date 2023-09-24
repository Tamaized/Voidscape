package tamaized.voidscape.entity;

import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.capability.SubCapability;
import tamaized.voidscape.registry.*;

import javax.annotation.Nullable;

public class CorruptedPawnEntity extends Mob implements IEntityAdditionalSpawnData {

	private final ServerBossEvent bossEvent = (ServerBossEvent) (new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.PURPLE, BossEvent.BossBarOverlay.PROGRESS)).setDarkenScreen(true);
	private Entity target;

	public CorruptedPawnEntity(Level level) {
		this(ModEntities.CORRUPTED_PAWN.get(), level);
	}

	public CorruptedPawnEntity(EntityType<? extends CorruptedPawnEntity> p_i48577_1_, Level p_i48577_2_) {
		super(p_i48577_1_, p_i48577_2_);
		setNoGravity(true);
		noPhysics = true;
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Monster.createMonsterAttributes()
				.add(Attributes.MAX_HEALTH, 20.0D);
	}

	@Override
	public void startSeenByPlayer(ServerPlayer player) {
		super.startSeenByPlayer(player);
		if (player == target)
			this.bossEvent.addPlayer(player);
	}

	@Override
	public void stopSeenByPlayer(ServerPlayer player) {
		super.stopSeenByPlayer(player);
		if (player == target)
			this.bossEvent.removePlayer(player);
	}

	public CorruptedPawnEntity target(Player player) {
		target = player;
		return this;
	}

	@Override
	public void lookAt(Entity entityIn, float maxYawIncrease, float maxPitchIncrease) {
		super.lookAt(entityIn, maxYawIncrease, maxPitchIncrease);
		setYHeadRot(getYRot());
	}

	public boolean shouldRender(@Nullable Player player) {
		return player != null && player.equals(target);
	}

	@Override
	public void tick() {
		bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
		if (!level().isClientSide()) {
			if (target == null || !target.isAlive())
				remove(RemovalReason.DISCARDED);
			else {
				target.getCapability(SubCapability.CAPABILITY).ifPresent(cap -> cap.get(Voidscape.subCapInsanity).ifPresent(data -> {
					if (!equals(data.getHunter()))
						remove(RemovalReason.DISCARDED);
					else {
						lookAt(EntityAnchorArgument.Anchor.EYES, target.position());
						setDeltaMovement(position().subtract(target.position()).normalize().scale(-0.5F));
					}
				}));
			}
		} else if (isAlive() && target != null) {
			target.lookAt(EntityAnchorArgument.Anchor.EYES, getEyePosition());
			lookAt(EntityAnchorArgument.Anchor.EYES, target.position());
			if (target instanceof Player target && tickCount % 5 == 0)
				level().playSound(target, blockPosition(), SoundEvents.GUARDIAN_ATTACK, SoundSource.MASTER, 4F, 0.5F);
		}
		super.tick();
	}

	@Override
	public void writeSpawnData(FriendlyByteBuf buffer) {
		buffer.writeVarInt(target == null ? -1 : target.getId());
	}

	@Override
	public void readSpawnData(FriendlyByteBuf additionalData) {
		target = level().getEntity(additionalData.readVarInt());
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		float dmg = source.is(ModDamageSource.VOIDIC) ? amount : amount * 0.1F;
		if (super.hurt(source, dmg)) {
			if (isAlive()) {
				Vec3 vec = new Vec3(0, 100, 0).xRot(getRandom().nextFloat() * 2F - 1F).yRot(getRandom().nextFloat());
				moveTo(target.getX() + vec.x(), target.getY() + vec.y(), target.getZ() + vec.z(), getYRot(), getXRot());
			}
			return true;
		}
		return false;
	}

	@Override
	protected void tickDeath() {
		super.tickDeath();
		if (!level().isClientSide()) {
			if (deathTime == 1)
				level().playSound(null, this.xo, this.yo, this.zo, SoundEvents.WITHER_DEATH, this.getSoundSource(), 0.5F, 0.25F + random.nextFloat() * 0.5F);
			if (deathTime == 20) {
				level().addFreshEntity(new ItemEntity(level(), target.getX(), target.getY(), target.getZ(), new ItemStack(ModItems.TENDRIL.get(), 1 + getRandom().nextInt(4))));
				target.getCapability(SubCapability.CAPABILITY).ifPresent(cap -> cap.get(Voidscape.subCapInsanity).ifPresent(data -> data.setParanoia(0)));
			}
		}
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
	protected void playStepSound(BlockPos p_20135_, BlockState p_20136_) {

	}

	@Override
	public Packet<ClientGamePacketListener> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	public void checkDespawn() {
	}

}
