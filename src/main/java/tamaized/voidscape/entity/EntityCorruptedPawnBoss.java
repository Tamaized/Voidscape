package tamaized.voidscape.entity;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.entity.ai.AITask;
import tamaized.voidscape.entity.ai.IInstanceEntity;
import tamaized.voidscape.entity.ai.pawn.AutoAttack;
import tamaized.voidscape.entity.ai.pawn.Bind;
import tamaized.voidscape.entity.ai.pawn.TankBuster;
import tamaized.voidscape.entity.ai.pawn.TentacleFall;
import tamaized.voidscape.registry.ModArmors;
import tamaized.voidscape.registry.ModBlocks;
import tamaized.voidscape.registry.ModDamageSource;
import tamaized.voidscape.registry.ModEntities;
import tamaized.voidscape.registry.ModItems;
import tamaized.voidscape.registry.ModTools;
import tamaized.voidscape.turmoil.Progression;
import tamaized.voidscape.turmoil.SubCapability;
import tamaized.voidscape.turmoil.Talk;
import tamaized.voidscape.world.Instance;
import tamaized.voidscape.world.InstanceManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EntityCorruptedPawnBoss extends EntityCorruptedPawn implements IInstanceEntity {

	public static final Vec3[] TENTACLE_POSITIONS = new Vec3[]{new Vec3(36.5, 60, 0.5), new Vec3(27.5, 60, 9.5), new Vec3(18.5, 60, 18.5), new Vec3(9.5, 60, 9.5), new Vec3(0.5, 60, 0.5), new Vec3(9.5, 60, -8.5), new Vec3(18.5, 60, -17.5), new Vec3(27.5, 60, -8.5)};
	private final ServerBossEvent bossEvent = (ServerBossEvent) (new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.PURPLE, BossEvent.BossBarOverlay.PROGRESS)).setDarkenScreen(true);
	public boolean beStill = false;
	public int aiTick;
	public Entity lockonTarget;
	public List<Entity> tentacles = new ArrayList<>();
	public List<Integer> tentacleIndicies = new ArrayList<>();
	private AITask<EntityCorruptedPawnBoss> ai;

	public EntityCorruptedPawnBoss(Level level) {
		this(ModEntities.CORRUPTED_PAWN_BOSS.get(), level);
	}

	public EntityCorruptedPawnBoss(EntityType<? extends EntityCorruptedPawn> p_i48577_1_, Level p_i48577_2_) {
		super(p_i48577_1_, p_i48577_2_);
		setNoGravity(true);
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
		return super.hurt(source, ModDamageSource.check(ModDamageSource.ID_VOIDIC, source) ? amount : amount * 0.01F);
	}

	@Override
	protected void tickDeath() {
		super.tickDeath();
		if (!level.isClientSide()) {
			if (deathTime == 1)
				level.playSound(null, this.xo, this.yo, this.zo, SoundEvents.WITHER_DEATH, this.getSoundSource(), 0.5F, 0.25F + random.nextFloat() * 0.5F);
			if (deathTime == 20) {
				level.setBlock(getRestrictCenter().above(1), Blocks.CHEST.defaultBlockState(), 3);
				level.setBlock(getRestrictCenter(), ModBlocks.EXIT_PORTAL.get().defaultBlockState(), 3);
				BlockEntity te = level.getBlockEntity(getRestrictCenter().above(1));
				if (te != null)
					te.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(cap -> cap.insertItem(0, new ItemStack(ModItems.TENDRIL.get(), level.random.nextInt(20) + 4), false));
				InstanceManager.findByLevel(level).
						ifPresent(instance -> instance.players().
								forEach(player -> player.getCapability(SubCapability.CAPABILITY).
										ifPresent(cap -> cap.get(Voidscape.subCapTurmoilData).
												ifPresent(data -> {
													if (data.getProgression() == Progression.PostPsychosis) {
														data.talk(Talk.CORRUPT_PAWN);
														level.addFreshEntity(new ItemEntity(level, player.getX(), player.getY(), player.getZ(), new ItemStack(ModItems.TENDRIL.get(), 8)));
													}
												}))));
			}
		}
	}

	@Override
	public void tick() {
		if (!level.isClientSide())
			setDeltaMovement(Vec3.ZERO);
		bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
		if (getTarget() != null && getTarget().getCapability(SubCapability.CAPABILITY).map(cap -> cap.get(Voidscape.subCapTurmoilTracked).map(data -> data.incapacitated).orElse(false)).orElse(false)) {
			getCapability(SubCapability.CAPABILITY_AGGRO).ifPresent(cap -> cap.remove(getTarget()));
			setTarget(null);
		}
		if (!level.isClientSide() && ai != null && getTarget() != null) {
			ai = ai.handle(this);
			lookAt(getTarget(), 10F, 10F);
			if (!beStill) {
				if (distanceTo(getTarget()) > 4) {
					Vec3 angle = getLookAngle().scale(0.25F);
					move(MoverType.SELF, new Vec3(angle.x(), 0, angle.z()));
				}
			}
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
		super.tick();
	}

	private void doTeleportParticles() {
		if (level instanceof ServerLevel)
			for (int i = 0; i < 50; i++) {
				double x = random.nextFloat() * (getBoundingBox().maxX - getBoundingBox().minX) + getBoundingBox().minX;
				double y = random.nextFloat() * (getBoundingBox().maxY - getBoundingBox().minY) + getBoundingBox().minY;
				double z = random.nextFloat() * (getBoundingBox().maxZ - getBoundingBox().minZ) + getBoundingBox().minZ;
				((ServerLevel) level).sendParticles(ParticleTypes.SQUID_INK, x, y, z, 0, 0, 0, 0, 0);
			}
	}

	public void teleportHome() {
		doTeleportParticles();
		moveTo(getRestrictCenter().getX() + 0.5F, getRestrictCenter().getY(), getRestrictCenter().getZ() + 0.5F);
		if (!level.isClientSide())
			level.playSound(null, this.xo, this.yo, this.zo, SoundEvents.ENDERMAN_TELEPORT, this.getSoundSource(), 4F, 0.25F + random.nextFloat() * 0.5F);
		doTeleportParticles();
	}

	@Override
	public void initInstance() {
		float hp = 400;
		( // TODO: change the ai up
				ai = new TentacleFall(0, 25F, 80 * 20, 100F, boss -> boss.getHealth() / boss.getMaxHealth() <= 0.75F)).
				next(new TentacleFall(1, 25F, 80 * 20, 100F, boss -> boss.getHealth() / boss.getMaxHealth() <= 0.5F)).
				next(new TentacleFall(2, 25F, 80 * 20, 100F, boss -> boss.getHealth() / boss.getMaxHealth() <= 0.25F)).
				next(new AITask.RandomAITask<>()).
				next(new TankBuster(8F, false, rand -> rand.nextInt(3) == 0)).
				next(new AutoAttack(4F));
		Objects.requireNonNull(getAttribute(Attributes.MAX_HEALTH)).addPermanentModifier(new AttributeModifier("Instanced Health", hp - 20, AttributeModifier.Operation.ADDITION));
		setHealth(getMaxHealth());
	}
}
