package tamaized.voidscape.entity;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import tamaized.voidscape.registry.ModEffects;
import tamaized.voidscape.registry.ModEntities;

public class IchorBoltEntity extends SpellBoltEntity {

	public IchorBoltEntity(EntityType<IchorBoltEntity> type, Level level) {
		super(type, level, 0xFF7700);
	}

	public IchorBoltEntity(Level worldIn, LivingEntity shooter) {
		super(ModEntities.ICHOR_BOLT.get(), worldIn, shooter, 0xFF7700);
	}

	@Override
	protected void doPostHurtEffects(LivingEntity entity) {
		entity.addEffect(new MobEffectInstance(ModEffects.ICHOR.get(), 20 * 30));
	}

}
