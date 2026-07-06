package tamaized.voidscape.entity.ai.nullservant;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import tamaized.beanification.Autowired;
import tamaized.beanification.Configurable;
import tamaized.voidscape.entity.NullServantAugmentBlockEntity;
import tamaized.voidscape.entity.NullServantEntity;
import tamaized.voidscape.network.client.ClientPacketSendParticles;
import tamaized.voidscape.registry.ModDamageSource;

import java.util.EnumSet;

@Configurable
public class TitaniteAugmentGoal extends Goal {

	@Autowired
	private ModDamageSource damageSource;

	private final NullServantEntity parent;
	private int cooldown;
	private int tick;

	@Nullable
	private NullServantAugmentBlockEntity block1;
	@Nullable
	private NullServantAugmentBlockEntity block2;
	@Nullable
	private NullServantAugmentBlockEntity block3;

	@Nullable
	private Vec3 aoe1;
	@Nullable
	private Vec3 aoe2;

	public TitaniteAugmentGoal(NullServantEntity parent) {
		this.setFlags(EnumSet.of(Goal.Flag.MOVE));
		this.parent = parent;
	}

	@Override
	public boolean canUse() {
		return !parent.getAugmentAttack() && parent.getAugment() == NullServantEntity.AUGMENT_TITANITE && (parent.getHealth() / parent.getMaxHealth()) <= 0.5F && cooldown-- <= 0;
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
		if (block1.isRemoved())
			block1 = null;
		block2.randomPosOrDiscard();
		if (block2.isRemoved())
			block2 = null;
		block3.randomPosOrDiscard();
		if (block3.isRemoved())
			block3 = null;
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
		if (tick++ % 30 == 0)
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

	private void boom(Vec3 pos) {
		if (!(parent.level() instanceof ServerLevel serverLevel))
			return;
		parent.level().getEntities(
				(Entity) null,
				new AABB(pos.x(), pos.y(), pos.z(), pos.x() + 1, pos.y() + 1, pos.z() + 1).inflate(4D),
				e -> !(e instanceof NullServantAugmentBlockEntity) && e != parent && e.distanceToSqr(pos) <= 9D
		).forEach(e -> e.hurtServer(serverLevel, damageSource.getEntityDamageSource(e.level(), damageSource.VOIDIC, parent), 6F));
		parent.playSound(SoundEvents.GENERIC_EXPLODE.value(), 4F, (1.0F + (parent.getRandom().nextFloat() - parent.getRandom().nextFloat()) * 0.2F) * 0.7F);
		ClientPacketSendParticles particles = new ClientPacketSendParticles();
		particles.queueParticle(ParticleTypes.EXPLOSION_EMITTER, pos, Vec3.ZERO);
		PacketDistributor.sendToPlayersTrackingEntity(parent, particles);
	}

}
