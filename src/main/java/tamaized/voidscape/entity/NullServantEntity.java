package tamaized.voidscape.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerPlayerConnection;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.BossEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.entity.ai.nullservant.SpecialAugmentGoal;
import tamaized.voidscape.network.client.ClientPacketSendParticles;
import tamaized.voidscape.registry.*;

import java.util.UUID;

public class NullServantEntity extends Monster implements IEthereal {

	private static final EntityDataAccessor<Integer> AUGMENT = SynchedEntityData.defineId(NullServantEntity.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Boolean> AUGMENT_ATTACK = SynchedEntityData.defineId(NullServantEntity.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Vector3f> AUGMENT_ATTACK_AOE1 = SynchedEntityData.defineId(NullServantEntity.class, EntityDataSerializers.VECTOR3);
	private static final EntityDataAccessor<Vector3f> AUGMENT_ATTACK_AOE2 = SynchedEntityData.defineId(NullServantEntity.class, EntityDataSerializers.VECTOR3);
	public static final int AUGMENT_TITANITE = 1;

	private static final UUID AUGMENT_HEALTH = UUID.fromString("f65da6bd-3e6b-468a-addc-a08335a954f2");
	private static final UUID AUGMENT_ATTACK_DAMAGE = UUID.fromString("5ae68488-df12-40c6-9517-357917341afa");
	private static final UUID AUGMENT_RESISTANCE = UUID.fromString("dcf3c0df-c827-43f7-8d07-9d77b6ce0c83");

	private ServerBossEvent bossInfo;

	public NullServantEntity(Level level) {
		this(ModEntities.NULL_SERVANT.get(), level);
	}

	public NullServantEntity(EntityType<? extends NullServantEntity> type, Level level) {
		super(type, level);
		xpReward = 10;
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Monster.createMonsterAttributes()
				.add(Attributes.MAX_HEALTH, 50.0D)
				.add(Attributes.FOLLOW_RANGE, 15.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.23F)
				.add(Attributes.ATTACK_DAMAGE, 3.0D)
				.add(Attributes.ARMOR, 10.0D)
				.add(ModAttributes.VOIDIC_DMG.get(), 2.0D)
				.add(ModAttributes.VOIDIC_RES.get(), 3.0D);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
		this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
		this.goalSelector.addGoal(1, new SpecialAugmentGoal(this));
		this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, false));
		this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0D));

		this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
	}

	@Override
	@Nullable
	@Deprecated
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor p_33282_, DifficultyInstance p_33283_, MobSpawnType p_33284_, @Nullable SpawnGroupData p_33285_, @Nullable CompoundTag p_33286_) {
		this.populateDefaultEquipmentSlots(getRandom(), p_33283_);
		this.populateDefaultEquipmentEnchantments(getRandom(), p_33283_);
		return super.finalizeSpawn(p_33282_, p_33283_, p_33284_, p_33285_, p_33286_);
	}

	@Override
	protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance p_32136_) {
		super.populateDefaultEquipmentSlots(random, p_32136_);
		this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(random.nextBoolean() ? ModTools.CORRUPT_AXE.get() : ModTools.CORRUPT_SWORD.get()));
	}

	@Override
	protected InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
		if (!level().isClientSide() && getAugment() <= 0) {
			if (pPlayer.getItemInHand(pHand).is(ModItems.TITANITE_CHUNK.get())) {
				setAugment(AUGMENT_TITANITE);
				doAugmentEffectsAndTrackBossBar();
				if (pPlayer instanceof ServerPlayer player)
					ModAdvancementTriggers.ITEM_USED_ON_NULL_SERVANT_TRIGGER.trigger(player, pPlayer.getItemInHand(pHand));
				if (!pPlayer.isCreative())
					pPlayer.getItemInHand(pHand).shrink(1);
				return InteractionResult.SUCCESS;
			}
		}
		return super.mobInteract(pPlayer, pHand);
	}

	private void doAugmentEffectsAndTrackBossBar() {
		ClientPacketSendParticles particles = new ClientPacketSendParticles();
		for (int i = 0; i < 100; i++) {
			particles.queueParticle(
					ParticleTypes.END_ROD,
					false,
					position().x() - 1D + getRandom().nextFloat() * 2D,
					position().y() + 0.5D + getRandom().nextFloat() * 2D,
					position().z() - 1D + getRandom().nextFloat() * 2D,
					0D,
					0D,
					0D
			);
		}
		Voidscape.NETWORK.send(PacketDistributor.TRACKING_ENTITY.with(() -> this), particles);
		playSound(SoundEvents.ZOMBIE_VILLAGER_CONVERTED, 4F, 0.5F + getRandom().nextFloat() * 0.5F);
		if (getCommandSenderWorld().getChunkSource() instanceof ServerChunkCache serverChunkCache) {
			for(ServerPlayerConnection serverplayerconnection : serverChunkCache.chunkMap.entityMap.get(getId()).seenBy) {
				bossInfo.addPlayer(serverplayerconnection.getPlayer());
			}
		}
	}

	private void initBossBar() {
		if (getAugment() == AUGMENT_TITANITE) {
			bossInfo = new ServerBossEvent(Component.translatable("entity.voidscape.null_servant.titanite"), BossEvent.BossBarColor.GREEN, BossEvent.BossBarOverlay.PROGRESS);
		}
	}

	private void setupAugmentStats() {
		AttributeInstance attributeMaxHealth = getAttribute(Attributes.MAX_HEALTH);
		AttributeInstance attributeVoidicDamage = getAttribute(ModAttributes.VOIDIC_DMG.get());
		AttributeInstance attributeVoidicRes = getAttribute(ModAttributes.VOIDIC_RES.get());
		if (attributeMaxHealth == null || attributeVoidicDamage == null || attributeVoidicRes == null)
			return;
		attributeMaxHealth.removeModifier(AUGMENT_HEALTH);
		attributeVoidicDamage.removeModifier(AUGMENT_ATTACK_DAMAGE);
		attributeVoidicRes.removeModifier(AUGMENT_RESISTANCE);

		if (getAugment() == AUGMENT_TITANITE) {
			attributeMaxHealth.addTransientModifier(new AttributeModifier(AUGMENT_HEALTH, "Augmented Health", 50F, AttributeModifier.Operation.ADDITION));
			attributeVoidicDamage.addTransientModifier(new AttributeModifier(AUGMENT_ATTACK_DAMAGE, "Augmented Damage", 1F, AttributeModifier.Operation.ADDITION));
			attributeVoidicRes.addTransientModifier(new AttributeModifier(AUGMENT_RESISTANCE, "Augmented Resistance", 1F, AttributeModifier.Operation.ADDITION));
			ItemStack stack = ItemStack.EMPTY;
			if (getItemInHand(InteractionHand.MAIN_HAND).is(ModTools.CORRUPT_SWORD.get()))
				stack = new ItemStack(ModTools.TITANITE_SWORD.get());
			else if (getItemInHand(InteractionHand.MAIN_HAND).is(ModTools.CORRUPT_AXE.get()))
				stack = new ItemStack(ModTools.TITANITE_AXE.get());
			if (!stack.isEmpty())
				setItemSlot(EquipmentSlot.MAINHAND, stack);
		}

		setHealth(getMaxHealth());
	}

	@Override
	public void setCustomName(@Nullable Component name) {
		super.setCustomName(name);
		if (bossInfo != null)
			bossInfo.setName(this.getDisplayName());
	}

	@Override
	public void startSeenByPlayer(ServerPlayer player) {
		super.startSeenByPlayer(player);
		if (bossInfo != null)
			bossInfo.addPlayer(player);
	}

	@Override
	public void stopSeenByPlayer(ServerPlayer player) {
		super.stopSeenByPlayer(player);
		if (bossInfo != null)
			bossInfo.removePlayer(player);
	}

	@Override
	public void aiStep() {
		super.aiStep();
		if (!level().isClientSide() && bossInfo != null) {
			bossInfo.setProgress(getHealth() / getMaxHealth());
		}
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		entityData.define(AUGMENT, 0);
		entityData.define(AUGMENT_ATTACK, false);
		entityData.define(AUGMENT_ATTACK_AOE1, new Vector3f());
		entityData.define(AUGMENT_ATTACK_AOE2, new Vector3f());
	}

	public Integer getAugment() {
		return this.entityData.get(AUGMENT);
	}

	public void setAugment(int type) {
		entityData.set(AUGMENT, type);
		if (type > 0) {
			initBossBar();
			setupAugmentStats();
		}
	}

	public Boolean getAugmentAttack() {
		return entityData.get(AUGMENT_ATTACK);
	}

	public void setAugmentAttack(boolean attack) {
		entityData.set(AUGMENT_ATTACK, attack);
	}

	public void setAugmentAttackAoes(Vector3f aoe1, Vector3f aoe2) {
		entityData.set(AUGMENT_ATTACK_AOE1, aoe1);
		entityData.set(AUGMENT_ATTACK_AOE2, aoe2);
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		compound.putInt("augment", getAugment());
		super.addAdditionalSaveData(compound);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		setAugment(compound.getInt("augment")); // This first before health is read from nbt
		super.readAdditionalSaveData(compound);

		if (hasCustomName() && bossInfo != null) {
			bossInfo.setName(getDisplayName());
		}
	}

	@Override
	protected void dropCustomDeathLoot(DamageSource p_21385_, int p_21386_, boolean p_21387_) {
		if (getAugment() == AUGMENT_TITANITE) {
			this.spawnAtLocation(new ItemStack(ModItems.TITANITE_SHARD.get()));
		}
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return null;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource p_33579_) {
		return SoundEvents.BLAZE_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.BLAZE_DEATH;
	}

	@Override
	protected void playStepSound(BlockPos p_32159_, BlockState p_32160_) {
		this.playSound(this.getStepSound(), 0.15F, 1.0F);
	}

	protected SoundEvent getStepSound() {
		return SoundEvents.AMETHYST_BLOCK_STEP;
	}

	@Override
	public boolean hurt(DamageSource pSource, float pAmount) {
		return !getAugmentAttack() && super.hurt(pSource, pAmount);
	}

	@Override
	public void knockback(double pStrength, double pX, double pZ) {

	}

	@Override
	protected boolean isImmobile() {
		return !getAugmentAttack() && super.isImmobile();
	}

	@Override
	public void push(Entity pEntity) {
		if (!getAugmentAttack())
			super.push(pEntity);
	}

	@Override
	public void push(double pX, double pY, double pZ) {
		if (!getAugmentAttack())
			super.push(pX, pY, pZ);
	}

	@Override
	public boolean canBeCollidedWith() {
		return !getAugmentAttack() && super.canBeCollidedWith();
	}

	@Override
	public boolean canCollideWith(Entity pEntity) {
		return !getAugmentAttack() && super.canCollideWith(pEntity);
	}

	@Override
	protected void pushEntities() {
		if (!getAugmentAttack())
			super.pushEntities();
	}

	@Override
	public void tick() {
		super.tick();
		if (level().isClientSide()) {
			if (tickCount % 5 == 0) {
				Vec3 vec = position().add(0, 1.0F - (random.nextFloat() * 0.6F), 0).add(new Vec3(0.1D + random.nextDouble() * 0.35D, 0D, 0D).yRot((float) Math.toRadians(random.nextInt(360))));
				level().addParticle(ParticleTypes.END_ROD, vec.x, vec.y, vec.z, 0, 0, 0);
			}
			if (getAugmentAttack()) {
				level().addParticle(
						ParticleTypes.END_ROD,
						false,
						position().x() - 1D + getRandom().nextFloat() * 2D,
						position().y() + 0.5D + getRandom().nextFloat() * 2D,
						position().z() - 1D + getRandom().nextFloat() * 2D,
						0D,
						0D,
						0D);
			}
			for (int i = 0; i < 3; i++) {
				doAoeParticles(entityData.get(AUGMENT_ATTACK_AOE1));
				doAoeParticles(entityData.get(AUGMENT_ATTACK_AOE2));
			}
		}
	}

	private void doAoeParticles(Vector3f aoe) {
		if (aoe.x() != 0 || aoe.y() != 0 || aoe.z() != 0) {
			Vec3 rot = new Vec3(4, 0, 0).yRot((float) Math.toRadians(getRandom().nextFloat() * 360F)).xRot((float) Math.toRadians(getRandom().nextFloat() * 360F));
			Vec3 pos = new Vec3(aoe).add(rot);
			Vec3 dir = new Vec3(aoe.x(), aoe.y(), aoe.z()).subtract(pos).normalize().scale(0.35D);
			level().addParticle(ParticleTypes.END_ROD, false, pos.x(), pos.y(), pos.z(), dir.x(), dir.y(), dir.z());
		}
	}

	@Override
	public Packet<ClientGamePacketListener> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

}
