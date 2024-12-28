package tamaized.voidscape;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.monster.Zoglin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.MobSpawnEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;
import tamaized.beanification.BeanContext;
import tamaized.regutil.RegUtil;
import tamaized.voidscape.asm.ASMHooks;
import tamaized.voidscape.data.Insanity;
import tamaized.voidscape.entity.IEthereal;
import tamaized.voidscape.registry.*;
import tamaized.voidscape.dimension.VoidChunkGenerator;
import tamaized.voidscape.biome.LayeredBiomeProvider;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

@Mod(Voidscape.MODID)
public class Voidscape {

	public static final String MODID = "voidscape";

	public static final Logger LOGGER = LogManager.getLogger(MODID);

	static {
		BeanContext.init();
		RegUtil.setup();
	}

	public Voidscape() {
		busForge.addListener(MobSpawnEvent.SpawnPlacementCheck.class, event -> {
			if (event.getSpawnType() == MobSpawnType.NATURAL && Voidscape.checkForVoidDimension(event.getLevel().getLevel()) && event.getLevel().getLightEmission(event.getPos()) <= 7) {
				event.setResult(Event.Result.ALLOW);
			}
		});

		busForge.addListener(MobSpawnEvent.PositionCheck.class, event -> {
			if (event.getSpawnType() == MobSpawnType.NATURAL && Voidscape.checkForVoidDimension(event.getLevel().getLevel())) {
				Player player = event.getLevel().getNearestPlayer(event.getX(), event.getY(), event.getZ(), -1.0D, false);
				if (player != null &&
						Voidscape.isValidPositionForMob(
								event.getLevel().getLevel(),
								event.getEntity(),
								player.distanceToSqr(event.getX(), event.getY(), event.getZ()),
								BlockPos.containing(event.getX(), event.getY(), event.getZ())))
					event.setResult(Event.Result.ALLOW);
				else
					event.setResult(Event.Result.DENY);
			}
		});

		busForge.addListener(MobSpawnEvent.FinalizeSpawn.class, event -> {
			if (event.getSpawnType() == MobSpawnType.NATURAL &&
					!(event.getEntity() instanceof IEthereal) &&
					Voidscape.checkForVoidDimension(event.getEntity().level())) {
				event.getEntity().getData(ModDataAttachments.INSANITY).addInfusion(event.getEntity().getRandom().nextInt(200) + 100, event.getEntity());
			}
		});
	}

	private static boolean isValidPositionForMob(ServerLevel serverWorld_, Mob mobEntity_, double double_, BlockPos pos) {
		if (double_ > (double) (mobEntity_.getType().getCategory().getDespawnDistance() * mobEntity_.getType().getCategory().getDespawnDistance()) && mobEntity_.removeWhenFarAway(double_)) {
			return false;
		} else {
			return mobEntity_.checkSpawnObstruction(serverWorld_) &&
					(!(mobEntity_ instanceof Zoglin || mobEntity_ instanceof IEthereal) || NaturalSpawner.canSpawnAtBody(SpawnPlacements.Type.ON_GROUND, serverWorld_, pos, mobEntity_.getType()));
		}
	}

	public static HitResult getHitResultFromEyes(LivingEntity entity, Predicate<Entity> predicate, double range) {
		return getHitResultFromEyes(entity, predicate, range, 0, 0);
	}

	public static HitResult getHitResultFromEyes(LivingEntity entity, Predicate<Entity> predicate, double range, double inflateXZ, double inflateY) {
		Vec3 vector3d = entity.getEyePosition(1F);
		Vec3 vector3d1 = entity.getViewVector(1.0F);
		Vec3 vector3d2 = vector3d.add(vector3d1.x * range, vector3d1.y * range, vector3d1.z * range);
		AABB axisalignedbb = entity.getBoundingBox().expandTowards(vector3d1.scale(range)).inflate(1D, 1D, 1D);
		HitResult raytraceresult = entity.level().clip(new ClipContext(vector3d, vector3d2, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity));
		if (raytraceresult.getType() != HitResult.Type.MISS) {
			vector3d2 = raytraceresult.getLocation();
		}
		HitResult ray = getEntityHitResult(entity, vector3d, vector3d2, axisalignedbb, predicate, range * range, inflateXZ, inflateY);
		return ray == null ? raytraceresult : ray;
	}

	@Nullable
	private static EntityHitResult getEntityHitResult(Entity shooter, Vec3 startVec, Vec3 endVec, AABB boundingBox, Predicate<Entity> filter, double distance, double inflateXZ, double inflateY) {
		Level world = shooter.level();
		double d0 = distance;
		Entity entity = null;
		Vec3 vector3d = null;

		for (Entity entity1 : world.getEntities(shooter, boundingBox, filter)) {
			AABB axisalignedbb = entity1.getBoundingBox().inflate(entity1.getPickRadius()).inflate(inflateXZ, inflateY, inflateXZ);
			Optional<Vec3> optional = axisalignedbb.clip(startVec, endVec);
			if (axisalignedbb.contains(startVec)) {
				if (d0 >= 0.0D) {
					entity = entity1;
					vector3d = optional.orElse(startVec);
					d0 = 0.0D;
				}
			} else if (optional.isPresent()) {
				Vec3 vector3d1 = optional.get();
				double d1 = startVec.distanceToSqr(vector3d1);
				if (d1 < d0 || d0 == 0.0D) {
					if (entity1.getRootVehicle() == shooter.getRootVehicle() && !entity1.canRiderInteract()) {
						if (d0 == 0.0D) {
							entity = entity1;
							vector3d = vector3d1;
						}
					} else {
						entity = entity1;
						vector3d = vector3d1;
						d0 = d1;
					}
				}
			}
		}

		return entity == null ? null : new EntityHitResult(entity, vector3d);
	}

}
