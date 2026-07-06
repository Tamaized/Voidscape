package tamaized.voidscape.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
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
import tamaized.beanification.Autowired;
import tamaized.voidscape.entity.ai.wrath.ChargedExplosionGoal;
import tamaized.voidscape.registry.ModAttributes;
import tamaized.voidscape.registry.ModEntities;
import tamaized.voidscape.registry.tool.set.CharredToolSet;

public class VoidsWrathEntity extends Monster implements IEthereal {

	@Autowired
	private static ModEntities entities;

	@Autowired
	private static ModAttributes attributes;

	@Autowired
	private static CharredToolSet charredToolSet;

	private static final EntityDataAccessor<Boolean> GLOWING = SynchedEntityData.defineId(VoidsWrathEntity.class, EntityDataSerializers.BOOLEAN);

	public VoidsWrathEntity(Level level) {
		this(entities.VOIDS_WRATH.get(), level);
	}

	public VoidsWrathEntity(EntityType<? extends VoidsWrathEntity> p_i48577_1_, Level p_i48577_2_) {
		super(p_i48577_1_, p_i48577_2_);
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Monster.createMonsterAttributes()
				.add(Attributes.MAX_HEALTH, 40.0D)
				.add(Attributes.FOLLOW_RANGE, 15.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.23F)
				.add(Attributes.ATTACK_DAMAGE, 3.0D)
				.add(Attributes.ARMOR, 20.0D)
				.add(attributes.VOIDIC_DMG, 3D);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(1, new ChargedExplosionGoal(this));
		this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, false));
		this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0D));
		this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
		this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));

		this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
	}


	@SuppressWarnings("deprecation")
	@org.jetbrains.annotations.Nullable
	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnType, @org.jetbrains.annotations.Nullable SpawnGroupData spawnGroupData) {
		this.populateDefaultEquipmentSlots(getRandom(), difficulty);
		this.populateDefaultEquipmentEnchantments(level, getRandom(), difficulty);
		return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
	}

	@Override
	protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance p_32136_) {
		super.populateDefaultEquipmentSlots(random, p_32136_);
		this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(charredToolSet.CHARRED_WARHAMMER.get()));
	}

	@Override
	protected void dropCustomDeathLoot(ServerLevel level, DamageSource damageSource, boolean recentlyHit) {
		// NO-OP
	}

	@Override
	public void lookAt(Entity entityIn, float maxYawIncrease, float maxPitchIncrease) {
		super.lookAt(entityIn, maxYawIncrease, maxPitchIncrease);
		setYHeadRot(getYRot());
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(GLOWING, false);
	}

	public boolean isPowered() {
		return this.entityData.get(GLOWING);
	}

	public void markGlowing(boolean glow) {
		entityData.set(GLOWING, glow);
	}

	@Override
	public HumanoidArm getMainArm() {
		return HumanoidArm.RIGHT;
	}

	@Override
	public boolean hurtServer(ServerLevel level, DamageSource source, float amount) {
		return super.hurtServer(level, source, amount * (isPowered() ? 0.25F : 1F));
	}

	@Override
	public void knockback(double p_147241_, double p_147242_, double p_147243_) {
		if (!isPowered())
			super.knockback(p_147241_, p_147242_, p_147243_);
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return SoundEvents.BLAZE_AMBIENT;
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
		// NO-OP
	}

}
