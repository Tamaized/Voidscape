package tamaized.voidscape.entity;

import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.entity.IEntityWithComplexSpawn;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;
import tamaized.voidscape.network.client.ClientPacketSendParticles;
import tamaized.voidscape.registry.ModEntities;

public class PhantomNullServantEntity extends NullServantEntity {

	private NullServantEntity parent;

	public PhantomNullServantEntity(NullServantEntity parent) {
		this(ModEntities.NULL_SERVANT_PHANTOM.get(), parent.level());
		this.parent = parent;
	}

	public PhantomNullServantEntity(EntityType<? extends PhantomNullServantEntity> type, Level level) {
		super(type, level);
		setNoGravity(true);
		noPhysics = true;
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
		if ((pos = randomPos(level(), getRandom(), parent.position().add(0, 4, 0), this)) != null) {
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
	protected void registerGoals() {

	}

	@Override
	public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor p_33282_, DifficultyInstance p_33283_, MobSpawnType p_33284_, @Nullable SpawnGroupData p_33285_, @Nullable CompoundTag p_33286_) {
		return p_33285_;
	}

	@Override
	protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance p_32136_) {

	}

	@Override
	protected InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
		return InteractionResult.PASS;
	}

	@Override
	protected void initBossBar() {

	}

	@Override
	protected void dropAllDeathLoot(DamageSource pDamageSource) {

	}

	@Override
	protected void dropCustomDeathLoot(DamageSource p_21385_, int p_21386_, boolean p_21387_) {

	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		if (source.getDirectEntity() instanceof StrangePearlEntity && !(source.getEntity() instanceof NullServantEntity))
			return super.hurt(source, 300F);
		return false;
	}

	@Override
	public Boolean getAugmentAttack() {
		return true;
	}

	@Override
	public void tick() {
		super.tick();
		if (!level().isClientSide()) {
			if (parent == null || !parent.isAlive() || !parent.getAugmentAttack())
				discard();
			else {
				Player target = level().getNearestPlayer(this, 32D);
				if (target != null)
					lookAt(EntityAnchorArgument.Anchor.EYES, target.position().add(0, target.getBbHeight() / 2F, 0));
				if (entityData.get(AUGMENT_ATTACK) && tickCount % 15 == 0)
					playSound(SoundEvents.RESPAWN_ANCHOR_CHARGE, 0.75F, 1F);
				if (parent.getRandom().nextInt(5) == 0) {
					Vec3 dir = new Vec3(parent.getX(), parent.getY() + parent.getEyeHeight() / 2F, parent.getZ()).subtract(position()).normalize().scale(0.5D);
					ClientPacketSendParticles packet = new ClientPacketSendParticles();
					packet.queueParticle(ParticleTypes.END_ROD, false, getX(), getY() + getBbHeight() / 2F, getZ(), dir.x(), dir.y(), dir.z());
					PacketDistributor.TRACKING_ENTITY.with(this).send(packet);
				}
			}
		}
	}

	@Override
	protected void augmentClientTick() {
		if (entityData.get(AUGMENT_ATTACK)) {
			level().addParticle(
					ParticleTypes.ENCHANT,
					false,
					position().x() - 1D + getRandom().nextFloat() * 2D,
					position().y() + 0.5D + getRandom().nextFloat() * 2D,
					position().z() - 1D + getRandom().nextFloat() * 2D,
					0D,
					0D,
					0D);
		}
	}
}
