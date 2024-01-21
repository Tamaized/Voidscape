package tamaized.voidscape.entity;

import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import tamaized.voidscape.registry.ModDamageSource;
import tamaized.voidscape.registry.ModEntities;
import tamaized.voidscape.registry.ModItems;

public class StrangePearlEntity extends AbstractHurtingProjectile implements ItemSupplier {

	private float damage = 0F;
	private ItemStack stack = new ItemStack(getDefaultItem());

	public StrangePearlEntity(EntityType<? extends StrangePearlEntity> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
	}

	public StrangePearlEntity(Level pLevel, LivingEntity pShooter) {
		this(pLevel, pShooter, Vec3.ZERO);
	}

	public StrangePearlEntity(Level pLevel, LivingEntity pShooter, Vec3 dir) {
		super(ModEntities.STRANGE_PEARL.get(), pShooter, dir.x(), dir.y(), dir.z(), pLevel);
		setPos(pShooter.getEyePosition());
	}

	@Override
	protected boolean shouldBurn() {
		return false;
	}

	@Override
	protected ParticleOptions getTrailParticle() {
		return ParticleTypes.WITCH;
	}

	public StrangePearlEntity setDamage(float damage) {
		this.damage = damage;
		return this;
	}

	protected Item getDefaultItem() {
		return ModItems.STRANGE_PEARL.get();
	}

	@Override
	public ItemStack getItem() {
		return stack;
	}

	public void setItem(Item item) {
		stack = new ItemStack(item);
	}

	private ParticleOptions getParticle() {
		return new ItemParticleOption(ParticleTypes.ITEM, getItem());
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

	protected float getGravity() {
		return 0.03F;
	}

	@Override
	protected float getInertia() {
		return 1F;
	}

	@Override
	public void tick() {
		super.tick();
		if (!isNoGravity())
			setDeltaMovement(getDeltaMovement().x(), getDeltaMovement().y() - getGravity(), getDeltaMovement().z());
	}

	@Override
	protected boolean canHitEntity(Entity entity) {
		return (!(getOwner() instanceof NullServantEntity) && entity instanceof NullServantEntity) || super.canHitEntity(entity);
	}

	@Override
	protected void onHitEntity(EntityHitResult pResult) {
		super.onHitEntity(pResult);
		pResult.getEntity().hurt(ModDamageSource.getIndirectEntityDamageSource(level(), ModDamageSource.VOIDIC, this, getOwner()), damage);
		if (pResult.getEntity() instanceof EndCrystal && getItem().is(ModItems.STRANGE_PEARL.get())) {
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
