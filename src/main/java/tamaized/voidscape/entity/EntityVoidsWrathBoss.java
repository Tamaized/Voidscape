package tamaized.voidscape.entity;

import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.BossEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.PowerableMob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.network.NetworkHooks;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.entity.ai.AITask;
import tamaized.voidscape.entity.ai.IInstanceEntity;
import tamaized.voidscape.entity.ai.pawn.AutoAttack;
import tamaized.voidscape.entity.ai.pawn.Bind;
import tamaized.voidscape.entity.ai.pawn.TankBuster;
import tamaized.voidscape.entity.ai.pawn.TentacleFall;
import tamaized.voidscape.entity.ai.wrath.Heal;
import tamaized.voidscape.registry.ModArmors;
import tamaized.voidscape.registry.ModDamageSource;
import tamaized.voidscape.registry.ModEntities;
import tamaized.voidscape.registry.ModItems;
import tamaized.voidscape.registry.ModTools;
import tamaized.voidscape.turmoil.Progression;
import tamaized.voidscape.turmoil.SubCapability;
import tamaized.voidscape.turmoil.Talk;
import tamaized.voidscape.world.Instance;
import tamaized.voidscape.world.InstanceManager;

import javax.annotation.Nullable;
import java.util.Objects;

public class EntityVoidsWrathBoss extends Mob implements IInstanceEntity, PowerableMob {

	private static final EntityDataAccessor<Boolean> GLOWING = SynchedEntityData.defineId(EntityVoidsWrathBoss.class, EntityDataSerializers.BOOLEAN);
	private final ServerBossEvent bossEvent = (ServerBossEvent) (new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.PROGRESS)).setDarkenScreen(true);
	public boolean beStill = false;
	private boolean pause = false;
	public boolean noKnockback = false;
	public boolean armor = false;
	public int aiTick;
	public Entity lockonTarget;
	private AITask<EntityVoidsWrathBoss> ai;
	private Instance.InstanceType type;

	public long glowTick;
	private boolean lastGlow;

	private Entity targetCache;

	public EntityVoidsWrathBoss(Level level) {
		this(ModEntities.VOIDS_WRATH.get(), level);
	}

	public EntityVoidsWrathBoss(EntityType<? extends EntityVoidsWrathBoss> p_i48577_1_, Level p_i48577_2_) {
		super(p_i48577_1_, p_i48577_2_);
		setNoGravity(true);
	}

	@Override
	@Nullable
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor p_33282_, DifficultyInstance p_33283_, MobSpawnType p_33284_, @Nullable SpawnGroupData p_33285_, @Nullable CompoundTag p_33286_) {
		this.populateDefaultEquipmentSlots(p_33283_);
		this.populateDefaultEquipmentEnchantments(p_33283_);
		return super.finalizeSpawn(p_33282_, p_33283_, p_33284_, p_33285_, p_33286_);
	}

	@Override
	protected void populateDefaultEquipmentSlots(DifficultyInstance p_32136_) {
		super.populateDefaultEquipmentSlots(p_32136_);
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

	public void markGlowing(boolean glow) {
		entityData.set(GLOWING, glow);
	}

	@Override
	public HumanoidArm getMainArm() {
		return HumanoidArm.RIGHT;
	}

	@Override
	public void checkDespawn() {
		// NO-OP
	}

	@Override
	protected void dropCustomDeathLoot(DamageSource p_21385_, int p_21386_, boolean p_21387_) {
		// NO-OP
	}

	@Override
	public void startSeenByPlayer(ServerPlayer player) {
		super.startSeenByPlayer(player);
		this.bossEvent.addPlayer(player);
	}

	@Override
	public void stopSeenByPlayer(ServerPlayer player) {
		super.stopSeenByPlayer(player);
		this.bossEvent.removePlayer(player);
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		if (getTarget() == null && source.getDirectEntity() instanceof LivingEntity living)
			setTarget(living);
		return super.hurt(source, (ModDamageSource.check(ModDamageSource.ID_VOIDIC, source) ? amount : amount * 0.01F) * (armor ? 0.25F : 1F));
	}

	@Override
	public void knockback(double p_147241_, double p_147242_, double p_147243_) {
		if (!noKnockback)
			super.knockback(p_147241_, p_147242_, p_147243_);
	}

	@Override
	protected void tickDeath() {
		super.tickDeath();
		if (!level.isClientSide()) {
			if (deathTime == 1)
				level.playSound(null, this.xo, this.yo, this.zo, SoundEvents.BLAZE_DEATH, this.getSoundSource(), 0.5F, 0.25F + random.nextFloat() * 0.5F);
			if (deathTime == 20) {
				level.setBlock(getRestrictCenter().above(1), Blocks.CHEST.defaultBlockState(), 3);
				BlockEntity te = level.getBlockEntity(getRestrictCenter().above(1));
				if (te != null)
					te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(cap -> {
						cap.insertItem(0, new ItemStack(ModItems.CHARRED_BONE.get(), level.random.nextInt(5) + 1), false);
					});
				InstanceManager.findByLevel(level).
						ifPresent(instance -> instance.players().
								forEach(player -> player.getCapability(SubCapability.CAPABILITY).
										ifPresent(cap -> cap.get(Voidscape.subCapTurmoilData).
												ifPresent(data -> {
													if (data.getProgression() == Progression.Psychosis) {
														data.talk(Talk.CORRUPT_PAWN);
														level.addFreshEntity(new ItemEntity(level, player.getX(), player.getY(), player.getZ(), new ItemStack(ModItems.CHARRED_BONE.get(), 8)));
													}
												}))));
			}
		}
	}

	@Override
	public void tick() {
		bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
		if (getTarget() != null && getTarget().getCapability(SubCapability.CAPABILITY).map(cap -> cap.get(Voidscape.subCapTurmoilTracked).map(data -> data.incapacitated).orElse(false)).orElse(false)) {
			getCapability(SubCapability.CAPABILITY_AGGRO).ifPresent(cap -> cap.remove(getTarget()));
			setTarget(null);
		}
		if (!level.isClientSide() && ai != null && getTarget() != null) {
			ai = ai.handle(this);
			lookAt(getTarget(), 10F, 10F);
			if (!beStill) {
				if (!pause) {
					if (distanceTo(getTarget()) > 1) {
						Vec3 angle = getLookAngle().scale(0.25F);
						move(MoverType.SELF, new Vec3(angle.x(), 0, angle.z()));
					}
					if (tickCount % 40 == 0 && random.nextInt(5) == 0)
						pause = true;
				} else if (tickCount % 35 == 0 && random.nextInt(3) == 0)
					pause = false;
			} else
				pause = false;
		}
		if (!level.isClientSide() && getTarget() == null) {
			Entity closest = null;
			for (Player p : level.getEntitiesOfClass(Player.class, getBoundingBox().inflate(20F), e -> true)) {
				if (closest == null || distanceTo(p) < distanceTo(closest))
					closest = p;
			}
			if (closest != null)
				lookAt(closest, 10F, 10F);
		}

		if (level.isClientSide()) {
			if (lastGlow != isPowered()) {
				glowTick = tickCount;
				lastGlow = isPowered();
			}
		}
		super.tick();
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		type = Instance.InstanceType.fromOrdinal(tag.getCompound(Voidscape.MODID).getInt("instance") - 1);
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		CompoundTag nbt = new CompoundTag();
		if (type != null)
			nbt.putInt("instance", type.ordinal() + 1);
		tag.put(Voidscape.MODID, nbt);
	}

	@Override
	public void initInstanceType(Instance.InstanceType type) {
		this.type = type;
		float hp = 100;
		(ai = new Heal(boss -> boss.getHealth() / boss.getMaxHealth() <= 0.75F && boss.tickCount % (20 * 5) == 0 && boss.getRandom().nextInt(5) == 0))
				.next(new AITask.RepeatedAITask<>((boss, ai) -> {
					if (boss.tickCount % 20 == 0) {
						boss.getLevel().getEntities(boss, boss.getBoundingBox().inflate(1F))
								.forEach(e -> e.hurt(DamageSource.mobAttack(boss), 2F));
					}
				}));
		Objects.requireNonNull(getAttribute(Attributes.MAX_HEALTH)).addPermanentModifier(new AttributeModifier("Instanced Health", hp - 20, AttributeModifier.Operation.ADDITION));
		setHealth(getMaxHealth());
	}

	@Override
	public boolean isPowered() {
		return this.entityData.get(GLOWING);
	}
}
