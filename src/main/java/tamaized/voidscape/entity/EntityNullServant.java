package tamaized.voidscape.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobSpawnType;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import tamaized.voidscape.registry.ModAttributes;
import tamaized.voidscape.registry.ModEntities;
import tamaized.voidscape.registry.ModTools;

import javax.annotation.Nullable;

public class EntityNullServant extends Monster implements IEthereal {

	public EntityNullServant(Level level) {
		this(ModEntities.NULL_SERVANT.get(), level);
	}

	public EntityNullServant(EntityType<? extends EntityNullServant> type, Level level) {
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
		this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, false));
		this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0D));

		this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)));
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
		this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(random.nextBoolean() ? ModTools.CORRUPT_AXE.get() : ModTools.CORRUPT_SWORD.get()));
	}

	@Override
	protected void dropCustomDeathLoot(DamageSource p_21385_, int p_21386_, boolean p_21387_) {
		// NO-OP
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
	public void tick() {
		super.tick();
		if (level().isClientSide() && tickCount % 5 == 0) {
			Vec3 vec = position().add(0, 1.0F - (random.nextFloat() * 0.6F), 0).add(new Vec3(0.1D + random.nextDouble() * 0.35D, 0D, 0D).yRot((float) Math.toRadians(random.nextInt(360))));
			level().addParticle(ParticleTypes.END_ROD, vec.x, vec.y, vec.z, 0, 0, 0);
		}
	}

}
