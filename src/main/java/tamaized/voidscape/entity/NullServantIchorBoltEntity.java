package tamaized.voidscape.entity;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.network.client.ClientPacketSendParticles;
import tamaized.voidscape.registry.ModDamageSource;
import tamaized.voidscape.registry.ModEntities;

public class NullServantIchorBoltEntity extends SpellBoltEntity {

	public NullServantIchorBoltEntity(EntityType<NullServantIchorBoltEntity> type, Level level) {
		super(type, level, 0xFF0000);
		speed = 0.125D;
	}

	public NullServantIchorBoltEntity(LivingEntity shooter) {
		super(ModEntities.NULL_SERVANT_ICHOR_BOLT.get(), shooter, 0xFF0000);
		speed = 0.125D;
	}

	@Override
	protected boolean canHitEntity(Entity entity) {
		return !(entity instanceof PhantomNullServantEntity) && super.canHitEntity(entity);
	}

	@Override
	protected void doPostHurtEffects(LivingEntity entity) {
		if (!level().isClientSide()) {
			ClientPacketSendParticles particles = new ClientPacketSendParticles();
			particles.queueParticle(ParticleTypes.EXPLOSION, false, position().x(), position().y(), position().z(), 0, 0, 0);
			PacketDistributor.TRACKING_ENTITY.with(this).send(particles);
		}
		level().playSound(null, blockPosition(), SoundEvents.DRAGON_FIREBALL_EXPLODE, SoundSource.HOSTILE, 4F, 1F);
		entity.hurt(ModDamageSource.getIndirectEntityDamageSource(level(), ModDamageSource.VOIDIC, this, shootingEntity), 16F);
	}

}
