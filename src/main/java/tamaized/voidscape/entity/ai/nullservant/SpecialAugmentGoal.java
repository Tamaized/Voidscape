package tamaized.voidscape.entity.ai.nullservant;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;
import org.joml.Vector3f;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.entity.NullServantAugmentBlockEntity;
import tamaized.voidscape.entity.NullServantEntity;
import tamaized.voidscape.entity.NullServantIchorBoltEntity;
import tamaized.voidscape.entity.StrangePearlEntity;
import tamaized.voidscape.network.client.ClientPacketSendParticles;
import tamaized.voidscape.registry.ModDamageSource;
import tamaized.voidscape.registry.ModEffects;
import tamaized.voidscape.registry.ModParticles;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class SpecialAugmentGoal extends Goal {

	private final NullServantEntity parent;
	private int cooldown;
	private int tick;

	private NullServantAugmentBlockEntity block1;
	private NullServantAugmentBlockEntity block2;
	private NullServantAugmentBlockEntity block3;

	private Vec3 aoe1;
	private Vec3 aoe2;

	public SpecialAugmentGoal(NullServantEntity parent) {
		this.setFlags(EnumSet.of(Goal.Flag.MOVE));
		this.parent = parent;
	}

	@Override
	public boolean canUse() {
		return !parent.getAugmentAttack() && parent.getAugment() > 0 && (parent.getHealth() / parent.getMaxHealth()) <= getTriggerPerc() && cooldown-- <= 0;
	}

	private float getTriggerPerc() {
		if (parent.getAugment() == NullServantEntity.AUGMENT_TITANITE)
			return 0.5F;
		else if (parent.getAugment() == NullServantEntity.AUGMENT_ICHOR)
			return 0.75F;
		else if (parent.getAugment() == NullServantEntity.AUGMENT_ASTRAL)
			return 0.9F;
		return 0;
	}

	@Override
	public boolean canContinueToUse() {
		return parent.getAugmentAttack() && (block1 != null && block1.isAlive()) || (block2 != null && block2.isAlive()) || (block3 != null && block3.isAlive());
	}

	@Override
	public void start() {
		tick = 0;
		parent.setAugmentAttack(true);
		block1 = new NullServantAugmentBlockEntity(parent);
		block2 = new NullServantAugmentBlockEntity(parent);
		block3 = new NullServantAugmentBlockEntity(parent);
		parent.level().addFreshEntity(block1);
		parent.level().addFreshEntity(block2);
		parent.level().addFreshEntity(block3);
		block1.randomPosOrDiscard();
		block2.randomPosOrDiscard();
		block3.randomPosOrDiscard();
	}

	@Override
	public void stop() {
		parent.setAugmentAttack(false);
		parent.setAugmentAttackAoes(new Vector3f(), new Vector3f());
		block1 = null;
		block2 = null;
		block3 = null;
		aoe1 = null;
		aoe2 = null;
		cooldown = 150;
	}

	@Override
	public void tick() {
		switch (parent.getAugment()) {
			case NullServantEntity.AUGMENT_TITANITE -> {
				if (tick++ % 60 == 0)
					parent.heal(1F);
				if (tick % 100 == 0) {
					if (aoe1 == null && aoe2 == null) {
						aoe1 = NullServantAugmentBlockEntity.randomPos(parent.level(), parent.getRandom(), parent.position().add(0.0D, 0.75D, 0.0D), parent);
						aoe2 = NullServantAugmentBlockEntity.randomPos(parent.level(), parent.getRandom(), parent.position().add(0.0D, 0.75D, 0.0D), parent);
						parent.setAugmentAttackAoes(aoe1 == null ? new Vector3f() : aoe1.toVector3f(), aoe2 == null ? new Vector3f() : aoe2.toVector3f());
					} else if (aoe1 != null) {
						boom(aoe1);
						parent.setAugmentAttackAoes(new Vector3f(), aoe2 == null ? new Vector3f() : aoe2.toVector3f());
						aoe1 = null;
					} else {
						boom(aoe2);
						parent.setAugmentAttackAoes(new Vector3f(), new Vector3f());
						aoe2 = null;
					}
				}
			}
			case NullServantEntity.AUGMENT_ICHOR -> {
				if (tick++ % 60 == 0)
					parent.heal(1F);
				if (tick % 50 == 0) {
					parent.playSound(SoundEvents.BLAZE_SHOOT, 4F, (1.0F + (parent.getRandom().nextFloat() - parent.getRandom().nextFloat()) * 0.2F) * 0.7F);
					parent.level().addFreshEntity(new NullServantIchorBoltEntity(parent));
				}
				if (tick % 100 == 0) {
					if (aoe1 == null && aoe2 == null) {
						aoe1 = NullServantAugmentBlockEntity.randomPos(parent.level(), parent.getRandom(), parent.position().add(0.0D, 0.75D, 0.0D), parent);
						aoe2 = NullServantAugmentBlockEntity.randomPos(parent.level(), parent.getRandom(), parent.position().add(0.0D, 0.75D, 0.0D), parent);
						parent.setAugmentAttackAoes(aoe1 == null ? new Vector3f() : aoe1.toVector3f(), aoe2 == null ? new Vector3f() : aoe2.toVector3f());
					} else if (aoe1 != null) {
						ichor(aoe1);
						parent.setAugmentAttackAoes(new Vector3f(), aoe2 == null ? new Vector3f() : aoe2.toVector3f());
						aoe1 = null;
					} else {
						ichor(aoe2);
						parent.setAugmentAttackAoes(new Vector3f(), new Vector3f());
						aoe2 = null;
					}
				}
			}
			case NullServantEntity.AUGMENT_ASTRAL -> {
				if (tick++ % 60 == 0)
					parent.heal(1F);
				if (tick % 50 == 0) {
					shoot(parent.getEyePosition());
				}
				if (tick % 75 == 0) {
					if (aoe1 == null && aoe2 == null) {
						aoe1 = NullServantAugmentBlockEntity.randomPos(parent.level(), parent.getRandom(), parent.position().add(0.0D, 0.75D, 0.0D), parent);
						aoe2 = NullServantAugmentBlockEntity.randomPos(parent.level(), parent.getRandom(), parent.position().add(0.0D, 0.75D, 0.0D), parent);
						parent.setAugmentAttackAoes(aoe1 == null ? new Vector3f() : aoe1.toVector3f(), aoe2 == null ? new Vector3f() : aoe2.toVector3f());
					} else if (aoe1 != null) {
						shoot(aoe1);
						parent.setAugmentAttackAoes(new Vector3f(), aoe2 == null ? new Vector3f() : aoe2.toVector3f());
						aoe1 = null;
					} else {
						shoot(aoe2);
						parent.setAugmentAttackAoes(new Vector3f(), new Vector3f());
						aoe2 = null;
					}
				}
				if (tick % 25 == 0) {
					List<Vec3> vecs = new ArrayList<>();
					if (block1 != null && block1.isAlive())
						vecs.add(block1.position());
					if (block2 != null && block2.isAlive())
						vecs.add(block2.position());
					if (block3 != null && block3.isAlive())
						vecs.add(block3.position());
					if (!vecs.isEmpty()) {
						shoot(vecs.get(parent.getRandom().nextInt(vecs.size())));
					}
				}
			}
		}
	}

	private void boom(Vec3 pos) {
		parent.level().getEntities(
				(Entity) null,
				new AABB(pos.x(), pos.y(), pos.z(), pos.x() + 1, pos.y() + 1, pos.z() + 1).inflate(4D),
				e -> !(e instanceof NullServantAugmentBlockEntity) && e != parent && e.distanceToSqr(pos) <= 9D
				).forEach(e -> e.hurt(ModDamageSource.getEntityDamageSource(e.level(), ModDamageSource.VOIDIC, parent), 6F));
		parent.playSound(SoundEvents.GENERIC_EXPLODE, 4F, (1.0F + (parent.getRandom().nextFloat() - parent.getRandom().nextFloat()) * 0.2F) * 0.7F);
		ClientPacketSendParticles particles = new ClientPacketSendParticles();
		particles.queueParticle(ParticleTypes.EXPLOSION_EMITTER, false, pos, Vec3.ZERO);
		Voidscape.NETWORK.send(PacketDistributor.TRACKING_ENTITY.with(() -> parent), particles);
	}

	private void ichor(Vec3 pos) {
		parent.level().getEntities(
				(Entity) null,
				new AABB(pos.x(), pos.y(), pos.z(), pos.x() + 1, pos.y() + 1, pos.z() + 1).inflate(6D),
				e -> !(e instanceof NullServantAugmentBlockEntity) && e != parent && e.distanceToSqr(pos) <= 25D
				).forEach(e -> {
					if (e instanceof LivingEntity livingEntity)
						livingEntity.addEffect(new MobEffectInstance(ModEffects.ICHOR.get(), 20 * 60 * 5));
		});
		parent.playSound(SoundEvents.BLAZE_SHOOT, 4F, (1.0F + (parent.getRandom().nextFloat() - parent.getRandom().nextFloat()) * 0.2F) * 0.7F);
		ClientPacketSendParticles particles = new ClientPacketSendParticles();
		for (int i = 0; i < 200; i++) {
			Vec3 dir = new Vec3(0.125D, 0, 0)
					.yRot((float) Math.toRadians(parent.getRandom().nextInt(360)))
					.xRot((float) Math.toRadians(parent.getRandom().nextInt(360)));
			particles.queueParticle(new ModParticles.ParticleSpellCloudData(0xFF7700), false, pos, dir.add(0, 0.25D, 0));
		}
		Voidscape.NETWORK.send(PacketDistributor.TRACKING_ENTITY.with(() -> parent), particles);
	}

	private void shoot(Vec3 pos) {
		StrangePearlEntity pearl = new StrangePearlEntity(parent.level(), parent);
		pearl.setPos(pos);
		Player player = parent.level().getNearestPlayer(pos.x(), pos.y(), pos.z(), 256, false);
		if (player != null) {
			parent.level().playSound(null, BlockPos.containing(pos), SoundEvents.BLAZE_SHOOT, SoundSource.HOSTILE, 4F, (1.0F + (parent.getRandom().nextFloat() - parent.getRandom().nextFloat()) * 0.2F) * 0.7F);
			Vec3 dir = player.getBoundingBox().getCenter().subtract(pos).normalize().scale(0.25F);
			pearl.setNoGravity(true);
			pearl.setDeltaMovement(dir);
			parent.level().addFreshEntity(pearl.setDamage(5F));
		}
	}
}
