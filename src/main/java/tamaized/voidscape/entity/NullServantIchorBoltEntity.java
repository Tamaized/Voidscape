package tamaized.voidscape.entity;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import tamaized.beanification.Autowired;
import tamaized.voidscape.network.client.ClientPacketSendParticles;
import tamaized.voidscape.registry.ModDamageSource;
import tamaized.voidscape.registry.ModEntities;

public class NullServantIchorBoltEntity extends SpellBoltEntity {

	@Autowired
	private static ModEntities entities;

	@Autowired
	private static ModDamageSource damageSource;

	public NullServantIchorBoltEntity(EntityType<NullServantIchorBoltEntity> type, Level level) {
		super(type, level, 0xFF0000);
		speed = 0.125D;
	}

	public NullServantIchorBoltEntity(LivingEntity shooter) {
		super(entities.NULL_SERVANT_ICHOR_BOLT.get(), shooter, 0xFF0000);
		speed = 0.125D;
	}

	@Override
	protected boolean canHitEntity(Entity entity) {
		return !(entity instanceof PhantomNullServantEntity) && super.canHitEntity(entity);
	}

	@Override
	protected void doPostHurtEffects(LivingEntity entity) {
		if (level() instanceof ServerLevel serverLevel) {
			ClientPacketSendParticles particles = new ClientPacketSendParticles();
			particles.queueParticle(ParticleTypes.EXPLOSION, position().x(), position().y(), position().z(), 0, 0, 0);
			PacketDistributor.sendToPlayersTrackingEntity(this, particles);
			entity.hurtServer(serverLevel, damageSource.getIndirectEntityDamageSource(level(), damageSource.VOIDIC, this, shootingEntity), 16F);
		}
		level().playSound(null, blockPosition(), SoundEvents.DRAGON_FIREBALL_EXPLODE, SoundSource.HOSTILE, 4F, 1F);
	}

}
