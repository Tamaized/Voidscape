package tamaized.voidscape.entity;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.entity.IEntityWithComplexSpawn;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.network.client.ClientPacketSendParticles;
import tamaized.voidscape.registry.ModDamageSource;
import tamaized.voidscape.registry.ModEntities;

import java.util.ArrayList;
import java.util.Random;

public class NullServantAugmentBlockEntity extends LivingEntity implements IEntityWithComplexSpawn, IEthereal {

	private static final EntityDataAccessor<BlockState> MIMIC = SynchedEntityData.defineId(NullServantAugmentBlockEntity.class, EntityDataSerializers.BLOCK_STATE);

	private NullServantEntity parent;

	public NullServantAugmentBlockEntity(EntityType<NullServantAugmentBlockEntity> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
		setNoGravity(true);
	}

	public NullServantAugmentBlockEntity(NullServantEntity parent) {
		this(ModEntities.NULL_SERVANT_AUGMENT_BLOCK.get(), parent.level());
		this.parent = parent;
		initAugment();
	}

	public static AttributeSupplier.Builder createAttributes() {
		return LivingEntity.createLivingAttributes()
				.add(Attributes.MAX_HEALTH, 10.0D);
	}

	private void initAugment() {
		if (level().isClientSide())
			return;
		if (parent.getAugment() == NullServantEntity.AUGMENT_TITANITE) {
			entityData.set(MIMIC, random.nextBoolean() ? Blocks.GRASS_BLOCK.defaultBlockState() : Blocks.STONE.defaultBlockState());
		} else if (parent.getAugment() == NullServantEntity.AUGMENT_ICHOR) {
			entityData.set(MIMIC, random.nextBoolean() ? Blocks.CRIMSON_NYLIUM.defaultBlockState() : Blocks.NETHERRACK.defaultBlockState());
		} else if (parent.getAugment() == NullServantEntity.AUGMENT_ASTRAL) {
			entityData.set(MIMIC, Blocks.END_STONE.defaultBlockState());
		}
	}

	@Nullable
	public static Vec3 randomPos(Level level, RandomSource random, Vec3 from, Entity clipSource) {
		for (int i = 0; i < 10; i++) {
			Vec3 vec = new Vec3(5, 0.125, 0).yRot(Mth.DEG_TO_RAD * (random.nextFloat() * 360F)).add(from);
			BlockHitResult result = level.clip(new ClipContext(vec, from, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, clipSource));
			if (result.getType() != HitResult.Type.BLOCK) {
				return vec;
			}
		}
		return null;
	}

	public void randomPosOrDiscard() {
		if (level().isClientSide())
			return;
		Vec3 pos;
		if ((pos = randomPos(level(), getRandom(), parent.position(), this)) != null) {
			moveTo(pos);
			playSound(SoundEvents.ITEM_PICKUP, 1F, 0.2F + random.nextFloat() * 0.3F);
			ClientPacketSendParticles particles = new ClientPacketSendParticles();
			for (int j = 0; j < 50; j++) {
				particles.queueParticle(
						ParticleTypes.END_ROD,
						false,
						position().x() - 0.5D + random.nextFloat(),
						position().y() - 0.5D + random.nextFloat(),
						position().z() - 0.5D + random.nextFloat(),
						0D,
						0D,
						0D
				);
			}
			PacketDistributor.TRACKING_ENTITY.with(this).send(particles);
		} else {
			discard();
		}
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		entityData.define(MIMIC, Blocks.BEDROCK.defaultBlockState());
	}

	public BlockState getMimic() {
		return entityData.get(MIMIC);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		if (tag.hasUUID("parent") && level() instanceof ServerLevel serverLevel)
			if (serverLevel.getEntity(tag.getUUID("parent")) instanceof NullServantEntity p)
				parent = p;
		entityData.set(MIMIC, NbtUtils.readBlockState(level().holderLookup(Registries.BLOCK), tag.getCompound("mimic")));
		super.readAdditionalSaveData(tag);
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		if (parent != null)
			tag.putUUID("parent", parent.getUUID());
		tag.put("mimic", NbtUtils.writeBlockState(getMimic()));
		super.addAdditionalSaveData(tag);
	}

	@Override
	public void writeSpawnData(FriendlyByteBuf buffer) {
		buffer.writeInt(parent == null ? -1 : parent.getId());
	}

	@Override
	public void readSpawnData(FriendlyByteBuf additionalData) {
		if(level().getEntity(additionalData.readInt()) instanceof NullServantEntity p)
			parent = p;
	}

	@Override
	public boolean hurt(DamageSource pSource, float pAmount) {
		if (parent == null) {
			discard();
			return true;
		}
		if (pSource.is(DamageTypes.IN_WALL))
			return false;
		if (parent.getAugment() == NullServantEntity.AUGMENT_TITANITE)
			return super.hurt(pSource, pSource.is(ModDamageSource.VOIDIC) ? pAmount : pAmount * 0.1F);
		else if (parent.getAugment() == NullServantEntity.AUGMENT_ICHOR)
			return super.hurt(pSource, pSource.getEntity() == parent ? pAmount : pAmount * 0.01F);
		else if (parent.getAugment() == NullServantEntity.AUGMENT_ASTRAL)
			return super.hurt(pSource, pSource.getEntity() != parent && pSource.getDirectEntity() instanceof StrangePearlEntity ? pAmount : pAmount * 0.01F);
		else
			return super.hurt(pSource, pAmount);
	}

	@Override
	public boolean isPickable() {
		return true;
	}

	@Override
	public HumanoidArm getMainArm() {
		return HumanoidArm.RIGHT;
	}

	@Override
	public Iterable<ItemStack> getArmorSlots() {
		return new ArrayList<>();
	}

	@Override
	public ItemStack getItemBySlot(EquipmentSlot pSlot) {
		return ItemStack.EMPTY;
	}

	@Override
	public void setItemSlot(EquipmentSlot pSlot, ItemStack pStack) {

	}

	@Override
	public void knockback(double pStrength, double pX, double pZ) {

	}

	@Override
	protected boolean isImmobile() {
		return false;
	}

	@Override
	public void push(Entity pEntity) {

	}

	@Override
	public void push(double pX, double pY, double pZ) {

	}

	@Override
	public boolean canBeCollidedWith() {
		return false;
	}

	@Override
	public boolean canCollideWith(Entity pEntity) {
		return false;
	}

	@Override
	protected void pushEntities() {

	}

	@Override
	public void tick() {
		super.tick();
		if (hurtTime > 0)
			hurtTime--;
		if (!level().isClientSide() && (parent == null || !parent.isAlive() || !parent.getAugmentAttack()))
			discard();
		if (level().isClientSide() && parent != null && parent.isAlive() && random.nextInt(5) == 0) {
			Vec3 dir = new Vec3(parent.getX(), parent.getY() + parent.getEyeHeight() / 2F, parent.getZ()).subtract(position()).normalize().scale(0.5D);
			level().addParticle(ParticleTypes.END_ROD, false, getX(), getY() + getBbHeight() / 2F, getZ(), dir.x(), dir.y(), dir.z());
		}
	}

}
