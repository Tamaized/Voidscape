package tamaized.voidscape.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PowerableMob;
import net.minecraft.world.entity.SpawnGroupData;
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
import tamaized.voidscape.entity.ai.wrath.ChargedExplosionGoal;
import tamaized.voidscape.registry.ModAttributes;
import tamaized.voidscape.registry.ModEntities;
import tamaized.voidscape.registry.ModTools;

import javax.annotation.Nullable;

public class EntityVoidsWrath extends Monster implements PowerableMob, IEthereal {

	private static final EntityDataAccessor<Boolean> GLOWING = SynchedEntityData.defineId(EntityVoidsWrath.class, EntityDataSerializers.BOOLEAN);

	public long glowTick;
	private boolean lastGlow;

	public EntityVoidsWrath(Level level) {
		this(ModEntities.VOIDS_WRATH.get(), level);
	}

	public EntityVoidsWrath(EntityType<? extends EntityVoidsWrath> p_i48577_1_, Level p_i48577_2_) {
		super(p_i48577_1_, p_i48577_2_);
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Monster.createMonsterAttributes()
				.add(Attributes.MAX_HEALTH, 40.0D)
				.add(Attributes.FOLLOW_RANGE, 15.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.23F)
				.add(Attributes.ATTACK_DAMAGE, 3.0D)
				.add(Attributes.ARMOR, 20.0D)
				.add(ModAttributes.VOIDIC_DMG.get(), 3D);
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

	@Override
	@Nullable
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor p_33282_, DifficultyInstance p_33283_, MobSpawnType p_33284_, @Nullable SpawnGroupData p_33285_, @Nullable CompoundTag p_33286_) {
		this.populateDefaultEquipmentSlots(getRandom(), p_33283_);
		this.populateDefaultEquipmentEnchantments(getRandom(), p_33283_);
		return super.finalizeSpawn(p_33282_, p_33283_, p_33284_, p_33285_, p_33286_);
	}

	@Override
	protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance p_32136_) {
		super.populateDefaultEquipmentSlots(random, p_32136_);
		this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModTools.CHARRED_WARHAMMER.get()));
	}

	@Override
	public void lookAt(Entity entityIn, float maxYawIncrease, float maxPitchIncrease) {
		super.lookAt(entityIn, maxYawIncrease, maxPitchIncrease);
		setYHeadRot(getYRot());
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		entityData.define(GLOWING, false);
	}

	@Override
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
	public boolean hurt(DamageSource source, float amount) {
		return super.hurt(source, amount * (isPowered() ? 0.25F : 1F));
	}

	@Override
	public void knockback(double p_147241_, double p_147242_, double p_147243_) {
		if (!isPowered())
			super.knockback(p_147241_, p_147242_, p_147243_);
	}
}
