package tamaized.voidscape.entity;

import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import tamaized.voidscape.registry.ModEntities;
import tamaized.voidscape.registry.ModItems;

public class StrangePearlEntity extends ThrowableItemProjectile {

	public StrangePearlEntity(EntityType<? extends StrangePearlEntity> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
	}

	public StrangePearlEntity(Level pLevel, LivingEntity pShooter) {
		super(ModEntities.STRANGE_PEARL.get(), pShooter, pLevel);
	}

	public StrangePearlEntity(Level pLevel, double pX, double pY, double pZ) {
		super(ModEntities.STRANGE_PEARL.get(), pX, pY, pZ, pLevel);
	}

	@Override
	protected Item getDefaultItem() {
		return ModItems.STRANGE_PEARL.get();
	}

	private ParticleOptions getParticle() {
		return new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(getDefaultItem()));
	}

	@Override
	public void handleEntityEvent(byte pId) {
		if (pId == 3) {
			ParticleOptions particleoptions = this.getParticle();

			for(int i = 0; i < 8; ++i) {
				this.level().addParticle(particleoptions, this.getRandomX(0.25D), this.getY() + random.nextFloat() * 0.25F, this.getRandomZ(0.25D), 0.0D, 0.0D, 0.0D);
			}
		}

	}

	@Override
	protected void onHitEntity(EntityHitResult pResult) {
		super.onHitEntity(pResult);
		if (pResult.getEntity() instanceof EndCrystal entity) {
			entity.hurt(this.damageSources().thrown(this, this.getOwner()), 0);
			this.level().addFreshEntity(new ItemEntity(this.level(), this.getX(), this.getY(), this.getZ(), new ItemStack(ModItems.ASTRAL_SHARDS.get())));
		}
	}

	@Override
	protected void onHit(HitResult pResult) {
		super.onHit(pResult);
		if (!this.level().isClientSide) {
			this.level().broadcastEntityEvent(this, (byte)3);
			this.discard();
			playSound(SoundEvents.AMETHYST_CLUSTER_BREAK);
		}

	}
}
