package tamaized.voidscape.entity.abilities;

import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.registry.ModDamageSource;
import tamaized.voidscape.registry.ModEntities;
import tamaized.voidscape.registry.ModParticles;
import tamaized.voidscape.turmoil.SubCapability;
import tamaized.voidscape.turmoil.abilities.TurmoilAbility;

public class EntitySpellAura extends Entity {

	private static final DataParameter<Integer> COLOR = EntityDataManager.defineId(EntitySpellAura.class, DataSerializers.INT);
	private TurmoilAbility ability;
	private LivingEntity caster;
	private float damage;
	private boolean healing;
	private long life;

	public EntitySpellAura(EntityType<?> p_i48580_1_, World p_i48580_2_) {
		super(p_i48580_1_, p_i48580_2_);
	}

	public EntitySpellAura(TurmoilAbility ability, LivingEntity caster, int color, long life) {
		this(ModEntities.SPELL_AURA.get(), caster.level);
		moveTo(caster.position());
		this.ability = ability;
		this.caster = caster;
		entityData.set(COLOR, color);
		this.life = life;
	}

	public EntitySpellAura damage(float damage) {
		this.damage = damage;
		return this;
	}


	public EntitySpellAura healing() {
		healing = true;
		return this;
	}

	@Override
	protected void defineSynchedData() {
		entityData.define(COLOR, 0);
	}

	protected boolean canHitEntity(Entity entity) {
		return EntitySpellBolt.canHitEntity(level, entity, healing);
	}

	@Override
	public void tick() {
		if (level.isClientSide()) {
			int color = entityData.get(COLOR);
			for (int i = 0; i < 10; i++) {
				Vector3d vec = position().add(0, 1.25F + (random.nextFloat() - 0.5F), 0).add(new Vector3d(0.1D + random.nextDouble() * 2.9D, 0D, 0D).yRot((float) Math.toRadians(random.nextInt(360))));
				level.addParticle(new ModParticles.ParticleSpellCloudData((color >> 16) & 0xFF, (color >> 8) & 0xFF, color & 0xFF), vec.x, vec.y, vec.z, 0, 0, 0);
			}
			return;
		}
		if (tickCount > life || caster == null || !caster.isAlive()) {
			remove();
			return;
		}
		moveTo(caster.position());
		if (tickCount % Math.max(1, 30 - caster.getCapability(SubCapability.CAPABILITY).map(cap -> cap.get(Voidscape.subCapTurmoilStats).map(stats -> stats.stats().rechargeRate).orElse(0)).orElse(0)) == 0)
			for (Entity e : level.getEntities(this, getBoundingBox().inflate(5F, 2F, 5F), e -> e != this && (healing || e != caster) && canHitEntity(e))) {
				if (!(e instanceof LivingEntity))
					continue;
				LivingEntity entity = (LivingEntity) e;
				if (healing && !(entity instanceof MobEntity && entity.getMobType() == CreatureAttribute.UNDEAD))
					Voidscape.healTargetAndAggro(entity, caster, damage);
				else if (entity.hurt(caster == null ? ModDamageSource.VOIDIC : ModDamageSource.VOIDIC_WITH_ENTITY.apply(caster), damage))
					caster.getCapability(SubCapability.CAPABILITY).ifPresent(cap -> cap.get(Voidscape.subCapTurmoilStats).ifPresent(stats -> {
						stats.increasePowerFromAbilityDamage(caster, entity, ability);
					}));
			}
	}

	@Override
	protected void readAdditionalSaveData(CompoundNBT compound) {

	}

	@Override
	protected void addAdditionalSaveData(CompoundNBT compound) {

	}

	@Override
	public IPacket<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}
