package tamaized.voidscape.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
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
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.damagesource.DamageTypes;
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
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import tamaized.beanification.Autowired;
import tamaized.beanification.Configurable;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.entity.ai.nullservant.AstralAugmentGoal;
import tamaized.voidscape.entity.ai.nullservant.IchorAugmentGoal;
import tamaized.voidscape.entity.ai.nullservant.TitaniteAugmentGoal;
import tamaized.voidscape.network.client.ClientPacketSendParticles;
import tamaized.voidscape.registry.*;
import tamaized.voidscape.registry.item.MaterialItems;
import tamaized.voidscape.registry.tool.set.AstralToolSet;
import tamaized.voidscape.registry.tool.set.CorruptToolSet;
import tamaized.voidscape.registry.tool.set.IchorToolSet;
import tamaized.voidscape.registry.tool.set.TitaniteToolSet;

import java.util.UUID;

public class NullServantEntity extends Monster implements IEthereal {

	@Autowired
	private static ModEntities entities;

	@Autowired
	private static ModAttributes attributes;

	@Autowired
	private static CorruptToolSet corruptToolSet;

	@Autowired
	private static TitaniteToolSet titaniteToolSet;

	@Autowired
	private static IchorToolSet ichorToolSet;

	@Autowired
	private static AstralToolSet astralToolSet;

	@Autowired
	private static MaterialItems materialItems;

	@Autowired
	private static ModAdvancementTriggers advancementTriggers;

	private static final EntityDataAccessor<Integer> AUGMENT = SynchedEntityData.defineId(NullServantEntity.class, EntityDataSerializers.INT);
	protected static final EntityDataAccessor<Boolean> AUGMENT_ATTACK = SynchedEntityData.defineId(NullServantEntity.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Vector3f> AUGMENT_ATTACK_AOE1 = SynchedEntityData.defineId(NullServantEntity.class, EntityDataSerializers.VECTOR3);
	private static final EntityDataAccessor<Vector3f> AUGMENT_ATTACK_AOE2 = SynchedEntityData.defineId(NullServantEntity.class, EntityDataSerializers.VECTOR3);

	public static final int AUGMENT_TITANITE = 1;
	public static final int AUGMENT_ICHOR = 2;
	public static final int AUGMENT_ASTRAL = 3;

	private static final Identifier AUGMENT_HEALTH = Identifier.fromNamespaceAndPath(Voidscape.MODID, "augment_health");
	private static final Identifier AUGMENT_ATTACK_DAMAGE = Identifier.fromNamespaceAndPath(Voidscape.MODID, "augment_attack_damage");
	private static final Identifier AUGMENT_RESISTANCE = Identifier.fromNamespaceAndPath(Voidscape.MODID, "augment_resistance");

	@Nullable
	private ServerBossEvent bossInfo;

	@Nullable
	private IchorAugmentGoal ichorAugmentGoal;

	public NullServantEntity(Level level) {
		this(entities.NULL_SERVANT.get(), level);
	}

	public NullServantEntity(EntityType<? extends NullServantEntity> type, Level level) {
		super(type, level);
		xpReward = 25;
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Monster.createMonsterAttributes()
				.add(Attributes.MAX_HEALTH, 50.0D)
				.add(Attributes.FOLLOW_RANGE, 15.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.23F)
				.add(Attributes.ATTACK_DAMAGE, 3.0D)
				.add(Attributes.ARMOR, 10.0D)
				.add(attributes.VOIDIC_DMG, 2.0D)
				.add(attributes.VOIDIC_RES, 3.0D);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
		this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
		this.goalSelector.addGoal(1, new TitaniteAugmentGoal(this));
		this.goalSelector.addGoal(1, ichorAugmentGoal = new IchorAugmentGoal(this));
		this.goalSelector.addGoal(1, new AstralAugmentGoal(this));
		this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, false));
		this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0D));

		this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
	}

	@Deprecated
	@Nullable
	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData) {
		this.populateDefaultEquipmentSlots(getRandom(), difficulty);
		this.populateDefaultEquipmentEnchantments(level, getRandom(), difficulty);
		return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
	}

	@Override
	protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance p_32136_) {
		super.populateDefaultEquipmentSlots(random, p_32136_);
		this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(random.nextBoolean() ? corruptToolSet.CORRUPT_AXE.get() : corruptToolSet.CORRUPT_SWORD.get()));
	}

	@Override
	protected InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
		if (!level().isClientSide() && getAugment() <= 0) {
			if (pPlayer.getItemInHand(pHand).is(materialItems.TITANITE_CHUNK.get())) {
				doAugment(AUGMENT_TITANITE, pPlayer, pHand);
				return InteractionResult.SUCCESS;
			} else if (pPlayer.getItemInHand(pHand).is(materialItems.ICHOR.get())) {
				doAugment(AUGMENT_ICHOR, pPlayer, pHand);
				return InteractionResult.SUCCESS;
			} else if (pPlayer.getItemInHand(pHand).is(materialItems.ASTRAL_ESSENCE.get())) {
				doAugment(AUGMENT_ASTRAL, pPlayer, pHand);
				return InteractionResult.SUCCESS;
			}
		}
		return super.mobInteract(pPlayer, pHand);
	}

	private void doAugment(int augment, Player player, InteractionHand hand) {
		setAugment(augment);
		doAugmentEffectsAndTrackBossBar();
		if (player instanceof ServerPlayer serverPlayer)
			advancementTriggers.ITEM_USED_ON_NULL_SERVANT_TRIGGER.get().trigger(serverPlayer, player.getItemInHand(hand));
		if (!player.isCreative())
			player.getItemInHand(hand).shrink(1);
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
		PacketDistributor.sendToPlayersTrackingEntity(this, particles);
		playSound(SoundEvents.ZOMBIE_VILLAGER_CONVERTED, 4F, 0.5F + getRandom().nextFloat() * 0.5F);
		if (bossInfo != null && getCommandSenderWorld().getChunkSource() instanceof ServerChunkCache serverChunkCache) {
			for(ServerPlayerConnection serverplayerconnection : serverChunkCache.chunkMap.entityMap.get(getId()).seenBy) {
				bossInfo.addPlayer(serverplayerconnection.getPlayer());
			}
		}
	}

	protected void initBossBar() {
		if (getAugment() == AUGMENT_TITANITE) {
			bossInfo = new ServerBossEvent(Component.translatable("entity.voidscape.null_servant.titanite"), BossEvent.BossBarColor.GREEN, BossEvent.BossBarOverlay.PROGRESS);
		} else if (getAugment() == AUGMENT_ICHOR) {
			bossInfo = new ServerBossEvent(Component.translatable("entity.voidscape.null_servant.ichor"), BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.PROGRESS);
		} else if (getAugment() == AUGMENT_ASTRAL) {
			bossInfo = new ServerBossEvent(Component.translatable("entity.voidscape.null_servant.astral"), BossEvent.BossBarColor.PINK, BossEvent.BossBarOverlay.PROGRESS);
		}
	}

	private void setupAugmentStats() {
		AttributeInstance attributeMaxHealth = getAttribute(Attributes.MAX_HEALTH);
		AttributeInstance attributeVoidicDamage = getAttribute(attributes.VOIDIC_DMG);
		AttributeInstance attributeVoidicRes = getAttribute(attributes.VOIDIC_RES);
		if (attributeMaxHealth == null || attributeVoidicDamage == null || attributeVoidicRes == null)
			return;
		attributeMaxHealth.removeModifier(AUGMENT_HEALTH);
		attributeVoidicDamage.removeModifier(AUGMENT_ATTACK_DAMAGE);
		attributeVoidicRes.removeModifier(AUGMENT_RESISTANCE);

		if (getAugment() == AUGMENT_TITANITE) {
			attributeMaxHealth.addTransientModifier(new AttributeModifier(AUGMENT_HEALTH, 50F, AttributeModifier.Operation.ADD_VALUE));
			attributeVoidicDamage.addTransientModifier(new AttributeModifier(AUGMENT_ATTACK_DAMAGE, 1F, AttributeModifier.Operation.ADD_VALUE));
			attributeVoidicRes.addTransientModifier(new AttributeModifier(AUGMENT_RESISTANCE, 1F, AttributeModifier.Operation.ADD_VALUE));
			ItemStack stack = ItemStack.EMPTY;
			if (getItemInHand(InteractionHand.MAIN_HAND).is(corruptToolSet.CORRUPT_SWORD.get()))
				stack = new ItemStack(titaniteToolSet.TITANITE_SWORD.get());
			else if (getItemInHand(InteractionHand.MAIN_HAND).is(corruptToolSet.CORRUPT_AXE.get()))
				stack = new ItemStack(titaniteToolSet.TITANITE_AXE.get());
			if (!stack.isEmpty())
				setItemSlot(EquipmentSlot.MAINHAND, stack);
		} else if (getAugment() == AUGMENT_ICHOR) {
			attributeMaxHealth.addTransientModifier(new AttributeModifier(AUGMENT_HEALTH, 150F, AttributeModifier.Operation.ADD_VALUE));
			attributeVoidicDamage.addTransientModifier(new AttributeModifier(AUGMENT_ATTACK_DAMAGE, 4F, AttributeModifier.Operation.ADD_VALUE));
			attributeVoidicRes.addTransientModifier(new AttributeModifier(AUGMENT_RESISTANCE, 2F, AttributeModifier.Operation.ADD_VALUE));
			ItemStack stack = ItemStack.EMPTY;
			if (getItemInHand(InteractionHand.MAIN_HAND).is(corruptToolSet.CORRUPT_SWORD.get()))
				stack = new ItemStack(ichorToolSet.ICHOR_SWORD.get());
			else if (getItemInHand(InteractionHand.MAIN_HAND).is(corruptToolSet.CORRUPT_AXE.get()))
				stack = new ItemStack(ichorToolSet.ICHOR_AXE.get());
			if (!stack.isEmpty())
				setItemSlot(EquipmentSlot.MAINHAND, stack);
		} else if (getAugment() == AUGMENT_ASTRAL) {
			attributeMaxHealth.addTransientModifier(new AttributeModifier(AUGMENT_HEALTH, 400F, AttributeModifier.Operation.ADD_VALUE));
			attributeVoidicDamage.addTransientModifier(new AttributeModifier(AUGMENT_ATTACK_DAMAGE, 8F, AttributeModifier.Operation.ADD_VALUE));
			attributeVoidicRes.addTransientModifier(new AttributeModifier(AUGMENT_RESISTANCE, 4F, AttributeModifier.Operation.ADD_VALUE));
			ItemStack stack = ItemStack.EMPTY;
			if (getItemInHand(InteractionHand.MAIN_HAND).is(corruptToolSet.CORRUPT_SWORD.get()))
				stack = new ItemStack(astralToolSet.ASTRAL_SWORD.get());
			else if (getItemInHand(InteractionHand.MAIN_HAND).is(corruptToolSet.CORRUPT_AXE.get()))
				stack = new ItemStack(astralToolSet.ASTRAL_AXE.get());
			if (!stack.isEmpty())
				setItemSlot(EquipmentSlot.MAINHAND, stack);
		}

		setHealth(getMaxHealth());
	}

	@Override
	public void setCustomName(@Nullable Component name) {
		super.setCustomName(name);
		Component displayName = this.getDisplayName();
		if (bossInfo != null && displayName != null)
			bossInfo.setName(displayName);
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
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(AUGMENT, 0);
		builder.define(AUGMENT_ATTACK, false);
		builder.define(AUGMENT_ATTACK_AOE1, new Vector3f());
		builder.define(AUGMENT_ATTACK_AOE2, new Vector3f());
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

		Component displayName = this.getDisplayName();
		if (hasCustomName() && bossInfo != null && displayName != null) {
			bossInfo.setName(displayName);
		}
	}

	@Override
	protected void dropCustomDeathLoot(ServerLevel level, DamageSource damageSource, boolean recentlyHit) {
		if (getAugment() == AUGMENT_TITANITE) {
			this.spawnAtLocation(new ItemStack(materialItems.TITANITE_SHARD.get()));
		} else if (getAugment() == AUGMENT_ICHOR) {
			this.spawnAtLocation(new ItemStack(materialItems.ICHOR_CRYSTAL.get()));
		} else if (getAugment() == AUGMENT_ASTRAL) {
			this.spawnAtLocation(new ItemStack(materialItems.ASTRAL_CRYSTAL.get()));
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
	public boolean hurt(DamageSource source, float amount) {
		if (source.is(DamageTypes.GENERIC_KILL))
			return super.hurt(source, amount);
		if (this instanceof PhantomNullServantEntity && source.getDirectEntity() instanceof StrangePearlEntity && !(source.getEntity() instanceof NullServantEntity))
			return super.hurt(source, amount);
		if (getAugmentAttack() && source.getEntity() instanceof PhantomNullServantEntity) {
			switch (getAugment()) {
				case AUGMENT_ICHOR -> {
					if (ichorAugmentGoal != null)
						ichorAugmentGoal.applyHit();
					super.hurt(source, amount);
				}
				case AUGMENT_ASTRAL -> {

				}
			}
		}
		return !getAugmentAttack() && super.hurt(source, amount);
	}

	@Override
	public boolean removeWhenFarAway(double distanceToClosestPlayer) {
		return false;
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
				augmentClientTick();
			}
			for (int i = 0; i < 3; i++) {
				doAoeParticles(entityData.get(AUGMENT_ATTACK_AOE1));
				doAoeParticles(entityData.get(AUGMENT_ATTACK_AOE2));
			}
		}
	}

	protected void augmentClientTick() {
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

	private void doAoeParticles(Vector3f aoe) {
		if (aoe.x() != 0 || aoe.y() != 0 || aoe.z() != 0) {
			Vec3 rot = new Vec3(4, 0, 0).yRot((float) Math.toRadians(getRandom().nextFloat() * 360F)).xRot((float) Math.toRadians(getRandom().nextFloat() * 360F));
			Vec3 pos = new Vec3(aoe).add(rot);
			Vec3 dir = new Vec3(aoe.x(), aoe.y(), aoe.z()).subtract(pos).normalize().scale(0.35D);
			level().addParticle(ParticleTypes.END_ROD, false, pos.x(), pos.y(), pos.z(), dir.x(), dir.y(), dir.z());
		}
	}

}
